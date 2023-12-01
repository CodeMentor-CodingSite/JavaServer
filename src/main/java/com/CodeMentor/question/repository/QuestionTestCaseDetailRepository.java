package com.CodeMentor.question.repository;



import com.CodeMentor.question.entity.QuestionTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTestCaseDetailRepository<QuestionTestCaseDetail> extends JpaRepository<QuestionTestCaseDetail, Long> {
    List<QuestionTestCaseDetail> findByQuestionTestCase(QuestionTestCase questionTestCase);
}
