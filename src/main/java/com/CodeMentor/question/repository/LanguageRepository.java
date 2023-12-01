package com.CodeMentor.question.repository;


import com.CodeMentor.question.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByType(String type);
}
