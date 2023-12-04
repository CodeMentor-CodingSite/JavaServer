package com.CodeMentor.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeExecutionService {


    @Value("${CodeExecutionServer.username}")
    private String USERNAME;
    @Value("${CodeExecutionServer.host}")
    private String HOST;
    @Value("${CodeExecutionServer.port}")
    private int PORT;
    @Value("${CodeExecutionServer.private-key-path}")
    private String PRIVATE_KEY_PATH;


    public String executePythonScript(int questionId, String pythonScript) {

        // Todo : questionId를 통해, DB에서 값 가져오기
        String codeExecConverterContent = "converting_code";
        String testCaseKey = "test_case_key";
        String testCaseValue = "test_case_value";

        // Todo : 테스트 후 삭제
        ArrayList<String> testCaseKeyList = new ArrayList<>();
        testCaseKeyList.add("nums");
        testCaseKeyList.add("target");
        testCaseKeyList.add("answer");
        ArrayList<String> testCaseValueList = new ArrayList<>();
        testCaseValueList.add("[2,7,11,15]");
        testCaseValueList.add("9");
        testCaseValueList.add("[0,1]");

        //Add test case
        for (int i = 0; i < testCaseKeyList.size(); i++) {
            pythonScript = pythonScript + "\n" + testCaseKeyList.get(i) + " = " + testCaseValueList.get(i);
        }


        pythonScript = pythonScript + "\n" + "print(answer == Solution().twoSum(nums, target))";


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
