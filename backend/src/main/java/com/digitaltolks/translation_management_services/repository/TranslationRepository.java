package com.digitaltolks.translation_management_services.repository;

import com.digitaltolks.translation_management_services.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByKeyContainingIgnoreCase(String key);
    List<Translation> findByLocale(String locale);
    List<Translation> findByContentContainingIgnoreCase(String content);

    @Query("SELECT t FROM Translation t JOIN t.tags tag WHERE tag.name = :tagName")
    List<Translation> findByTagName(@Param("tagName") String tagName);

    @Query("SELECT t FROM Translation t WHERE t.locale = :locale")
    List<Translation> findAllByLocale(@Param("locale") String locale);

    @Query("SELECT DISTINCT t.locale FROM Translation t")
    List<String> findAllLocales();

    boolean existsByKeyAndLocale(String key, String locale);
}
