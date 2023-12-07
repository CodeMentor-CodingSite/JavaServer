package com.CodeMentor.question.entity;

import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "question_test_case")
public class QuestionTestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_test_case_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "is_example", columnDefinition = "TINYINT(1)")
    private Boolean isExample;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @OneToMany(mappedBy = "questionTestCase")
    private List<QuestionTestCaseDetail> questionTestCaseDetails;
}
