package com.CodeMentor.question.entity;

import lombok.*;

import javax.persistence.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "is_example", columnDefinition = "TINYINT")
    private Boolean isExample;

    @Column(name = "explanation", columnDefinition = "VARCHAR(500)")
    private String explanation;
}
