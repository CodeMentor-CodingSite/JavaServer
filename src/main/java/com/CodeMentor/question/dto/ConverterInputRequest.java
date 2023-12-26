package com.CodeMentor.question.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConverterInputRequest {

    private String languageType;
    private String codeExecConverterContent;
    private String resultType;
    private String methodName;
}
