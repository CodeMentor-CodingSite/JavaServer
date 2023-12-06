package com.CodeMentor.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConverterInputRequest {

    private String languageName;
    private String codeExecConverterContent;
    private String resultType;
    private String methodName;
}
