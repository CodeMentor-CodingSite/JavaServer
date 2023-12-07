package com.CodeMentor.question.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseRequest {

    private Integer questionId;
    private Boolean isExample;
    private String explanation;
    private ArrayList<TestCaseDetailDTO> testCaseDetailDTOs;
}
