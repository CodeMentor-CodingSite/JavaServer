package com.CodeMentor.code.service;

import com.CodeMentor.code.dto.UserCodeRequest;
import com.CodeMentor.question.entity.*;
import com.CodeMentor.question.repository.*;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JavaCodeService {
    @Value("${CodeExecutionServer.host}")
    private String HOST;
    @Value("${CodeExecutionServer.username}")
    private String USERNAME;
    @Value("${CodeExecutionServer.private-key-path}")
    private String PRIVATE_KEY_PATH;
    @Value("${CodeExecutionServer.port}")
    private int PORT;
    private static Session session;
    private static Channel channel;
    private final QuestionRepository questionRepository;
    private final LanguageRepository languageRepository;
    private final QuestionLanguageRepository questionLanguageRepository;
    private final QuestionTestCaseRepository questionTestCaseRepository;
    private final QuestionTestCaseDetailRepository questionTestCaseDetailRepository;
    private final CodeExecConverterRepository codeExecConverterRepository;
    private final ConverterMapRepository converterMapRepository;

    public Map<Integer, String> post(UserCodeRequest codeDTO) {
        // ssh 세션 성립
        if (session == null || !session.isConnected()) {
            sshSessionOpen();
        }

        Map<Integer, String> response = new HashMap<>();

        // code 생성 및 실행
        Question question = questionRepository.findById((long) codeDTO.getQuestionId()).orElseThrow();
        Language language = languageRepository.findByType(codeDTO.getLanguage()).orElseThrow();
        QuestionLanguage questionLanguage = questionLanguageRepository.findByQuestionAndLanguage(question, language).orElseThrow();

        List<QuestionTestCase> questionTestCases = questionTestCaseRepository.findByQuestion(question);
        int testCaseIndex = 1;
        for (QuestionTestCase questionTestCase : questionTestCases) {
            String finalCode = createJavaCode(codeDTO.getUserCode(), question, language, questionLanguage, questionTestCase);

            response.put(testCaseIndex++, executeCode(finalCode));
        }

        return response;
    }

    public String createJavaCode(String userCode, Question question, Language language, QuestionLanguage questionLanguage, QuestionTestCase questionTestCase) {
        StringBuilder finalCode = new StringBuilder("echo 'public class RealMain {\n");

        List<QuestionTestCaseDetail> questionTestCaseDetails = questionTestCaseDetailRepository.findByQuestionTestCase(questionTestCase);

        Set<Long> converterIdSet = new HashSet<>();
        for (QuestionTestCaseDetail questionTestCaseDetail : questionTestCaseDetails) {
            converterIdSet.addAll(converterMapRepository.findAllByQuestionTestCaseDetail(questionTestCaseDetail).stream()
                    .map(i -> i.getCodeExecConverter().getId())
                    .collect(Collectors.toSet()));
        }

        // TestCase 각 변수의 컨버터 코드 추가 (Converter Method 중복 방지를 위해 Set 사용)
        Set<CodeExecConverter> codeExecConverters = converterIdSet.stream()
                .map(i -> codeExecConverterRepository.findAllByIdAndLanguage(i, language))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        for (CodeExecConverter codeExecConverter : codeExecConverters) {
            createConverterCode(finalCode, codeExecConverter);
        }

        // RealMain -> main
        finalCode.append(
                "    public static void main(String[] args) throws Exception{\n" +
                        "        Main main = new Main();\n" +
                        "\n");

        // TestCase 환경에 맞는 변수 코드 추가
        for (QuestionTestCaseDetail questionTestCaseDetail : questionTestCaseDetails) {
            createParameterCode(finalCode, questionTestCaseDetail, language);
        }

        // 사용자 코드 실행 및 정답 비교를 위한 코드
        finalCode.append("        ").append(questionLanguage.getCheckContent()).append("\n")
                .append("    }\n")
                .append("}\n\n");

        // 시간 및 메모리 제약조건에 맞게 Java 실행
        finalCode.append(userCode).append("' > RealMain.java && javac RealMain.java && timeout -s 9 5s java -Xmx128m RealMain");

        return finalCode.toString();
    }

    public void createConverterCode(StringBuilder javaCode, CodeExecConverter codeExecConverter) {
        javaCode.append(codeExecConverter.getContent()).append("\n\n");
    }

    public void createParameterCode(StringBuilder javaCode, QuestionTestCaseDetail questionTestCaseDetail, Language language) {
        List<Long> converterIdList = questionTestCaseDetail.getConverterMaps().stream()
                .map(i -> i.getCodeExecConverter().getId())
                .collect(Collectors.toList());

        CodeExecConverter codeExecConverter = converterIdList.stream()
                .map(i -> codeExecConverterRepository.findAllByIdAndLanguage(i, language))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(null);

        javaCode.append("        ").append(codeExecConverter.getReturnType()).append(" ")
                .append(questionTestCaseDetail.getKey()).append(" = ")
                .append(codeExecConverter.getMethodName()).append("(\"")
                .append(questionTestCaseDetail.getValue()).append("\");\n");
    }


    // ssh 세션 연결
    private void sshSessionOpen() {
        JSch jSch = new JSch();

        try {
            jSch.addIdentity(PRIVATE_KEY_PATH);

            session = jSch.getSession(USERNAME, HOST, PORT);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        System.out.println("New Session Open");
    }

    // ssh 세션 활용한 Exec 채널로 코드 실행 및 결과 반환
    public String executeCode(String finalCode) {
        String response = "";

        try {
            channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec) channel;

            String cmd = finalCode;
            channelExec.setCommand(cmd);

            StreamGobbler outputGobbler = new StreamGobbler(channelExec.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler(channelExec.getErrStream());

            new Thread(outputGobbler).start();
            new Thread(errorGobbler).start();

            channelExec.connect();

            // 출력 도착 지연 때문인지 조금 기다려야 하는 듯
            while (!outputGobbler.isDone() || !errorGobbler.isDone()) {
                Thread.sleep(100);
            }

            response = outputGobbler.getOutput();
            String errorString = errorGobbler.getOutput();

            // 에러 확인
            if (!errorString.isEmpty()) {
                return "Error: " + errorString;
            }

            // Exception 확인
        } catch (JSchException | InterruptedException | IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }

        return response;
    }

    // StreamGobbler class
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private StringBuilder output = new StringBuilder();
        private volatile boolean done = false;

        StreamGobbler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                done = true;
            }
        }

        public String getOutput() {
            return output.toString();
        }

        public boolean isDone() {
            return done;
        }
    }

}
