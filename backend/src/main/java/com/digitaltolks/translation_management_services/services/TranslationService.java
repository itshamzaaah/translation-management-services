package com.digitaltolks.translation_management_services.services;

import com.digitaltolks.translation_management_services.model.Translation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TranslationService {
    Translation createTranslation(Translation translation, Set<String> tagNames);

    Translation updateTranslation(Long id, Translation translationData, Set<String> tagNames);

    Translation getTranslation(Long id);

    List<Translation> searchTranslations(String key, String locale, String content, String tag);

    void deleteTranslation(Long id);

    Map<String, String> exportTranslations(String locale);

    List<String> getAllLocales();
}
