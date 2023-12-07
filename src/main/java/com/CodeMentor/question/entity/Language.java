package com.CodeMentor.question.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    @Column(name = "language_type", columnDefinition = "VARCHAR(10)")
    private String type;

    @OneToMany(mappedBy = "language")
    private List<QuestionLanguage> questionLanguages;

    @OneToMany(mappedBy = "language")
    private List<CodeExecConverter> codeExecConverters;
}
