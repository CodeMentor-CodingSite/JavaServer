package com.CodeMentor.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCodeInputRequest {

    private String questionId;
    private String languageName;
    private String questionInitContent;
    private String answerCheckContent;
}
