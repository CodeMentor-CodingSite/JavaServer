package com.CodeMentor.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionInputRequest {

    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private ArrayList<String> questionConstraintContents;
}
