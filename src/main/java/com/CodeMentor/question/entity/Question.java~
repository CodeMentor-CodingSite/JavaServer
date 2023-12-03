package com.CodeMentor.question.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(name = "question_title", columnDefinition = "VARCHAR(50)")
    private String title;

    @Column(name = "question_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "question_category", columnDefinition = "VARCHAR(40)")
    private String category;

    @Column(name = "question_time", columnDefinition = "BIGINT")
    private Long time;

    @Column(name = "question_memory", columnDefinition = "BIGINT")
    private Long memory;

//    @Builder.Default
    @OneToMany(mappedBy = "question")
    private List<QuestionTestCase> questionTestCases;
}
