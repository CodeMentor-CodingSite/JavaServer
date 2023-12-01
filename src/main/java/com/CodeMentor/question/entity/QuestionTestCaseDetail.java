package com.CodeMentor.question.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "question_test_case_detail")
public class QuestionTestCaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_test_case_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_test_case_id")
    private QuestionTestCase questionTestCase;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "code_exec_converter_id")
    private CodeExecConverter codeExecConverter;

    @Column(name = "test_case_key", columnDefinition = "VARCHAR(100)")
    private String key;

    @Column(name = "test_case_value", columnDefinition = "VARCHAR(100)")
    private String value;
}
