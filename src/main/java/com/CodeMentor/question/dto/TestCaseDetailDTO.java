package com.CodeMentor.question.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseDetailDTO {

    private String testCaseKey;
    private String testCaseValue;
    private ArrayList<Integer> converterIds;
}
