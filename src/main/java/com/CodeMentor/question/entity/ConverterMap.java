package com.CodeMentor.question.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "converter_map")
public class ConverterMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "converter_map_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "code_exec_converter_id")
    private CodeExecConverter codeExecConverter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_test_case_detail_id")
    private QuestionTestCaseDetail questionTestCaseDetail;
}
