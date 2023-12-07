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
@Table(name = "code_exec_converter")
public class CodeExecConverter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_exec_converter_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "code_exec_converter_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "return_type", columnDefinition = "VARCHAR(30)")
    private String returnType;

    @Column(name = "method_name", columnDefinition = "TEXT")
    private String methodName;

    @OneToMany(mappedBy = "codeExecConverter")
    private List<ConverterMap> converterMaps;
}
