package com.CodeMentor.code.controller;

import com.CodeMentor.code.dto.UserCodeRequest;
import com.CodeMentor.code.service.JavaCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JavaCodeController {
    private final JavaCodeService javaCodeService;

    @PostMapping("/api/code/java")
    public Map<Integer, String> testPost(@RequestBody UserCodeRequest userCodeRequest){
        return javaCodeService.post(userCodeRequest);
    }
}
