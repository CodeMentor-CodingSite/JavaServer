package com.CodeMentor.question.repository;


import com.CodeMentor.question.entity.CodeExecConverter;
import com.CodeMentor.question.entity.ConverterMap;
import com.CodeMentor.question.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CodeExecConverterRepository extends JpaRepository<CodeExecConverter, Long> {
    @Query("SELECT ce FROM CodeExecConverter ce WHERE ce.converterMaps IN :converterMaps AND ce.language = :language")
    Set<CodeExecConverter> findAllByConverterMapsAndLanguage(@Param("converterMaps") List<ConverterMap> converterMaps, @Param("language") Language language);

    @Query("SELECT ce FROM CodeExecConverter ce WHERE ce.converterMaps IN :converterMaps AND ce.language = :language")
    Optional<CodeExecConverter> findByConverterMapsAndLanguage(@Param("converterMaps") List<ConverterMap> converterMaps, @Param("language") Language language);
}
