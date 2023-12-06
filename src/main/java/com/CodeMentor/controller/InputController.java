package com.CodeMentor.controller;

import com.CodeMentor.dto.ConverterInputRequest;
import com.CodeMentor.dto.QuestionCodeInputRequest;
import com.CodeMentor.dto.QuestionInputRequest;
import com.CodeMentor.dto.TestCaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/input/")
public class InputController {

    @PostMapping("/question")
    public ResponseEntity<String> questionInput(QuestionInputRequest request) {
        return ResponseEntity.ok("success");
    }

    @PostMapping("/testcase")
    public ResponseEntity<String> testCaseInput(TestCaseRequest request) {
        return ResponseEntity.ok("success");
    }

    @PostMapping("/converter")
    public ResponseEntity<String> converterInput(ConverterInputRequest request) {
        return ResponseEntity.ok("success");
    }

    @PostMapping("/question-code")
    public ResponseEntity<String> questionCodeInput(QuestionCodeInputRequest request) {
        return ResponseEntity.ok("success");
    }
}
