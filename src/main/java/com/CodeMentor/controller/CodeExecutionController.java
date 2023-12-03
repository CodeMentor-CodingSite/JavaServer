package com.CodeMentor.controller;

import com.CodeMentor.service.CodeExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/python")
public class CodeExecutionController {

    private final CodeExecutionService codeExecutionService;

//    @PostMapping("/execute")
    @GetMapping("/execute1")
    public ResponseEntity<String> executePythonScript(String pythonScript) {
        pythonScript = "class Solution():\n    def twoSum(self, nums, target):\n        \n        for i in range(len(nums)):\n            for j in range(i+1,len(nums)):\n                if nums[j]==target-nums[i]:\n                    return [i,j]\n                if nums[j] + nums[i] > target:\n                    break"
        ;
        try{
            String result = codeExecutionService.executePythonScript(1,pythonScript);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/execute2")
    public ResponseEntity<String> executePythonScript2(String pythonScript) {
        pythonScript = "print('Hello World'";
        try{
            String result = codeExecutionService.executePythonScript(1, pythonScript);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

//    @PostMapping("/execute")
//    public ResponseEntity<String> executePythonScript(Integer questionId, String pythonScript) {
    @GetMapping("/execute")
    public ResponseEntity<String> executePythonScript(Integer questionId, String pythonScript) {

        // Todo : 테스트 후 삭제
        questionId = 1;
        pythonScript =
                "\nclass Solution():" +
                "\n    def twoSum(self, nums, target):" +
                "\n        for i in range(len(nums)):" +
                "\n            for j in range(i+1,len(nums)):" +
                "\n                if nums[j]==target-nums[i]:" +
                "\n                    return [i,j]" +
                "\n                if nums[j] + nums[i] > target:" +
                "\n                    break";

        try{
//            String result = codeExecutionService.executePythonScript(questionId, pythonScript);
            String result = codeExecutionService.executePythonScript(questionId, pythonScript);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
