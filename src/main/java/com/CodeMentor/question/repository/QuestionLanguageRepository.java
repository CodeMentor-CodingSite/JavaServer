package com.CodeMentor.question.repository;



import com.CodeMentor.question.entity.Language;
import com.CodeMentor.question.entity.Question;
import com.CodeMentor.question.entity.QuestionLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionLanguageRepository extends JpaRepository<QuestionLanguage, Long> {
    Optional<QuestionLanguage> findByQuestionAndLanguage(Question question, Language language);
}
