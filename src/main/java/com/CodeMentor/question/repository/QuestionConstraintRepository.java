package com.CodeMentor.question.repository;


import com.CodeMentor.question.entity.Question;
import com.CodeMentor.question.entity.QuestionConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionConstraintRepository extends JpaRepository<QuestionConstraint, Long> {

}
