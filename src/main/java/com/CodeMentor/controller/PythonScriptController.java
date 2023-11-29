package com.CodeMentor.controller;

import com.CodeMentor.service.PythonScriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/python")
public class PythonScriptController {

    private final PythonScriptService pythonScriptService;

//    @PostMapping("/execute")
    @GetMapping("/execute1")
    public ResponseEntity<String> executePythonScript(String pythonScript) {
        pythonScript = "print('Hello World')";
        try{
            String result = pythonScriptService.executePythonScript(pythonScript);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/execute2")
    public ResponseEntity<String> executePythonScript2(String pythonScript) {
        pythonScript = "print('Hello World'";
        try{
            String result = pythonScriptService.executePythonScript(pythonScript);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
