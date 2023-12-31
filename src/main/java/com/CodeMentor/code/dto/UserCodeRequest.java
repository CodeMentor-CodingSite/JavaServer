package com.CodeMentor.code.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCodeRequest {
    private String userCode;
    private int questionId;
    private String language;
}
