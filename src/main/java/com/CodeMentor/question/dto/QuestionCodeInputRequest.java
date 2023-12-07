package com.CodeMentor.question.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCodeInputRequest {

    private Integer questionId;
    private String languageType;
    private String questionInitContent;
    private String answerCheckContent;
}
