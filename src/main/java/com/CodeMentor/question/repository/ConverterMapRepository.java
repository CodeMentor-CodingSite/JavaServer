package com.CodeMentor.question.repository;


import com.CodeMentor.question.entity.CodeExecConverter;
import com.CodeMentor.question.entity.ConverterMap;
import com.CodeMentor.question.entity.Question;
import com.CodeMentor.question.entity.QuestionTestCaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConverterMapRepository extends JpaRepository<ConverterMap, Long> {
    List<ConverterMap> findAllByQuestionTestCaseDetail(QuestionTestCaseDetail questionTestCaseDetail);
}
