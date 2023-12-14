package com.CodeMentor.question.service;


import com.CodeMentor.question.dto.ConverterInputRequest;
import com.CodeMentor.question.dto.QuestionCodeInputRequest;
import com.CodeMentor.question.dto.QuestionInputRequest;
import com.CodeMentor.question.dto.TestCaseRequest;
import com.CodeMentor.question.entity.*;
import com.CodeMentor.question.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final LanguageRepository languageRepository;
    private final QuestionLanguageRepository questionLanguageRepository;
    private final QuestionTestCaseRepository questionTestCaseRepository;
    private final QuestionTestCaseDetailRepository questionTestCaseDetailRepository;
    private final CodeExecConverterRepository codeExecConverterRepository;
    private final QuestionConstraintRepository questionConstraintRepository;

    private final ConverterMapRepository converterMapRepository;

    public Integer questionInput(QuestionInputRequest request) {
        Question question = Question.builder()
                .title(request.getQuestionTitle())
                .content(request.getQuestionContent())
                .category(request.getQuestionCategory())
                .build();
        Question response = questionRepository.save(question);

        for (int i = 0; i < request.getQuestionConstraintContents().size(); i++) {
            QuestionConstraint questionConstraint = QuestionConstraint.builder()
                    .question(response)
                    .content(request.getQuestionConstraintContents().get(i))
                    .build();
            questionConstraintRepository.save(questionConstraint);
        }

        return response.getId().intValue();
    }

    public Integer testCaseInput(TestCaseRequest request) {
        Question question = questionRepository.findById((long) request.getQuestionId()).orElseThrow();

        QuestionTestCase questionTestCase = QuestionTestCase.builder()
                .question(question)
                .isExample(request.getIsExample())
                .explanation(request.getExplanation())
                .build();
        QuestionTestCase questionTestCaseResponse = questionTestCaseRepository.save(questionTestCase);

        // 각 TestCaseDetail에 ConverterMap 저장
        for (int i = 0; i < request.getTestCaseDetailDTOs().size(); i++) {
            QuestionTestCaseDetail questionTestCaseDetail = QuestionTestCaseDetail.builder()
                    .questionTestCase(questionTestCaseResponse)
                    .key(request.getTestCaseDetailDTOs().get(i).getTestCaseKey())
                    .value(request.getTestCaseDetailDTOs().get(i).getTestCaseValue())
                    .build();
            ArrayList<Integer> converterIds = request.getTestCaseDetailDTOs().get(i).getConverterIds();

            // 각 TestCaseDetailId와 ConverterId를 ConverterMap에 저장
            for (int j = 0; j < converterIds.size(); j++) {
                CodeExecConverter codeExecConverter = codeExecConverterRepository.findById((long) converterIds.get(j)).orElseThrow();
                ConverterMap converterMap = ConverterMap.builder()
                        .questionTestCaseDetail(questionTestCaseDetail)
                        .codeExecConverter(codeExecConverter)
                        .build();
                converterMapRepository.save(converterMap);
            }
            questionTestCaseDetailRepository.save(questionTestCaseDetail);
        }

        return questionTestCaseResponse.getId().intValue();
    }

    public Integer converterInput(ConverterInputRequest request) {
        Language languageEntity = languageRepository.findByType(request.getLanguageType()).orElseThrow();

        CodeExecConverter codeExecConverter = CodeExecConverter.builder()
                .language(languageEntity)
                .content(request.getCodeExecConverterContent())
                .returnType(request.getResultType())
                .methodName(request.getMethodName())
                .build();

        CodeExecConverter response = codeExecConverterRepository.save(codeExecConverter);
        return response.getId().intValue();
    }

    public Integer questionCodeInput(QuestionCodeInputRequest request) {
        Question question = questionRepository.findById((long)request.getQuestionId()).orElseThrow();
        Language language = languageRepository.findByType(request.getLanguageType()).orElseThrow();

        QuestionLanguage questionLanguage = QuestionLanguage.builder()
                .question(question)
                .language(language)
                .initContnet(request.getQuestionInitContent())
                .checkContent(request.getAnswerCheckContent())
                .build();

        QuestionLanguage response = questionLanguageRepository.save(questionLanguage);
        return response.getId().intValue();
    }

}
