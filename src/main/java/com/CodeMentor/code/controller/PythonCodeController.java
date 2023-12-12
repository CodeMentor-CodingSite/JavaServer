package com.CodeMentor.code.controller;

import com.CodeMentor.code.dto.UserCodeRequest;
import com.CodeMentor.code.service.PythonCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/python")
public class PythonCodeController {

    private final PythonCodeService pythonCodeService;


    @PostMapping ("/execute")
    public ArrayList<String> executePython(@RequestBody UserCodeRequest userRequest) throws Exception {
        return pythonCodeService.executePythonScript(userRequest);
    }
}
