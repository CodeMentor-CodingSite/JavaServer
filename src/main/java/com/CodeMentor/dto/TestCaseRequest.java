package com.CodeMentor.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseRequest {

    private String questionId;
    private Boolean isExample;
    private String explanation;
    private ArrayList<TestCaseDetailDTO> testCaseDetailDTOs;
}
