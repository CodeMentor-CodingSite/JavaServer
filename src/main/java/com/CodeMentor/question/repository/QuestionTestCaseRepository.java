package com.CodeMentor.question.repository;


import com.CodeMentor.question.entity.Language;
import com.CodeMentor.question.entity.Question;
import com.CodeMentor.question.entity.QuestionTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTestCaseRepository extends JpaRepository<QuestionTestCase, Long> {
    List<QuestionTestCase> findByQuestionAndLanguage(Question question, Language language);
}
