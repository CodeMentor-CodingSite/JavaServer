package com.CodeMentor.code.service;

import com.CodeMentor.code.dto.UserCodeRequest;
import com.CodeMentor.question.entity.*;
import com.CodeMentor.question.repository.*;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PythonCodeService {

    private final QuestionRepository questionRepository;
    private final QuestionTestCaseRepository questionTestCaseRepository;
    private final QuestionLanguageRepository questionLanguageRepository;
    private final LanguageRepository languageRepository;
    private final CodeExecConverterRepository codeExecConverterRepository;
    private final ConverterMapRepository converterMapRepository;

    @Value("${CodeExecutionServer.username}")
    private String USERNAME;
    @Value("${CodeExecutionServer.host}")
    private String HOST;
    @Value("${CodeExecutionServer.port}")
    private int PORT;
    @Value("${CodeExecutionServer.private-key-path}")
    private String PRIVATE_KEY_PATH;


    public ArrayList<String> executePythonScript(UserCodeRequest request) throws Exception {

        ArrayList<String> testCaseResults = new ArrayList<>();

        int questionId = request.getQuestionId();
        String pythonScript = request.getUserCode();
        String language = request.getLanguage();

        Question question = questionRepository.findById((long) questionId).orElseThrow();
        List<QuestionTestCase> questionTestCases = questionTestCaseRepository.findByQuestion(question);
        Language languageEntity = languageRepository.findByType(language).orElseThrow();
        String answerCheck = questionLanguageRepository.findByQuestionAndLanguage(question, languageEntity).orElseThrow().getCheckContent();

        //각 TestCases 마다
        for (QuestionTestCase questionTestCase : questionTestCases) {
            String testCasePythonScript = pythonScript;
            ArrayList<String> testCaseKeyList = new ArrayList<>();
            ArrayList<String> testCaseValueList = new ArrayList<>();

            //각 TestCasesDetail 마다
            for (int i = 0; i < questionTestCase.getQuestionTestCaseDetails().size(); i++) {
                testCaseKeyList.add(questionTestCase.getQuestionTestCaseDetails().get(i).getKey());
                testCaseValueList.add(questionTestCase.getQuestionTestCaseDetails().get(i).getValue());

                List<ConverterMap> allLanguageConverters = converterMapRepository.findAllByQuestionTestCaseDetail(
                        questionTestCase.getQuestionTestCaseDetails().get(i));

                // key에 따른 converterContent, converterMethodName 가져오기
                String converterContent = "";
                String converterMethodName = "";
                Long languageId = 2L; // Assuming language_id is of type Long
                for (ConverterMap converterMap : allLanguageConverters) {
                    CodeExecConverter converter = converterMap.getCodeExecConverter(); // Assuming the method to get code_exec_converter_id
                    if (converter.getLanguage().equals(languageId)) {
                        converterContent = converter.getContent();
                        converterMethodName = converter.getMethodName();
                        break;
                    }
                }
                // 스크립트에 convert content 추가
                testCasePythonScript = testCasePythonScript + "\n" + converterContent;
                // 변수를 convert content를 통해 변환
                testCasePythonScript = testCasePythonScript + "\n" + testCaseKeyList.get(i) + " = " + converterMethodName + "(" + testCaseKeyList.get(i) + ")";

                testCasePythonScript = testCasePythonScript + "\n" + testCaseKeyList.get(i) + " = " + testCaseValueList.get(i);

            }

            testCasePythonScript = testCasePythonScript + "\n" + answerCheck;

            testCaseResults.add(sendPythonScript(testCasePythonScript));
        }

        return testCaseResults;
    }

    public String sendPythonScript(String pythonScript) throws Exception {
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        try {
            // Setup SSH connection
            jsch.addIdentity(PRIVATE_KEY_PATH);
            session = jsch.getSession(USERNAME, HOST, PORT);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Command to run Python script in Docker
            String command = "python3 -c \"" + pythonScript + "\"";

            // Execute command
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            // Initialize StreamGobblers for concurrent stream reading
            StreamGobbler outputGobbler = new StreamGobbler(channel.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler(channel.getErrStream());

            // Start StreamGobblers in separate threads
            new Thread(outputGobbler).start();
            new Thread(errorGobbler).start();

            // Start the channel
            channel.connect();

            // Wait for streams to finish
            while (!outputGobbler.isDone() || !errorGobbler.isDone()) {
                Thread.sleep(100);
            }

            // Fetch output and error
            String outputString = outputGobbler.getOutput();
            String errorString = errorGobbler.getOutput();

            System.out.println("outputString: " + outputString);
            System.out.println("errorString: " + errorString);

            if (!errorString.isEmpty()) {
                return "Error: " + errorString;
            }
            return outputString; // Return the standard output if no error occurred
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
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
