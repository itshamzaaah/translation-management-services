    package com.digitaltolks.translation_management_services.services.impl;

    import com.digitaltolks.translation_management_services.model.Tag;
    import com.digitaltolks.translation_management_services.model.Translation;
    import com.digitaltolks.translation_management_services.repository.TagRepository;
    import com.digitaltolks.translation_management_services.repository.TranslationRepository;
    import com.digitaltolks.translation_management_services.services.TranslationService;
    import jakarta.transaction.Transactional;
    import org.springframework.stereotype.Service;

    import java.util.*;
    import java.util.stream.Collectors;

    @Service
    @Transactional
    public class TranslationServiceImpl implements TranslationService {

        private final TranslationRepository translationRepository;
        private final TagRepository tagRepository;

        public TranslationServiceImpl(TranslationRepository translationRepository,
                                      TagRepository tagRepository) {
            this.translationRepository = translationRepository;
            this.tagRepository = tagRepository;
        }

        @Override
        public Translation createTranslation(Translation translation, Set<String> tagNames) {
            if (translationRepository.existsByKeyAndLocale(translation.getKey(), translation.getLocale())) {
                throw new IllegalArgumentException("Translation with this key and locale already exists");
            }

            Set<Tag> tags = processTags(tagNames);
            translation.setTags(tags);

            return translationRepository.save(translation);
        }

        @Override
        public Translation updateTranslation(Long id, Translation translationData, Set<String> tagNames) {
            Translation translation = translationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Translation not found"));

            translation.setContent(translationData.getContent());

            if (tagNames != null) {
                Set<Tag> tags = processTags(tagNames);
                translation.setTags(tags);
            }

            return translationRepository.save(translation);
        }

        @Override
        @Transactional
        public Translation getTranslation(Long id) {
            return translationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Translation not found"));
        }

        @Override
        @Transactional
        public List<Translation> searchTranslations(String key, String locale, String content, String tag) {
            if (key != null) {
                return translationRepository.findByKeyContainingIgnoreCase(key);
            } else if (locale != null) {
                return translationRepository.findByLocale(locale);
            } else if (content != null) {
                return translationRepository.findByContentContainingIgnoreCase(content);
            } else if (tag != null) {
                return translationRepository.findByTagName(tag);
            } else {
                return translationRepository.findAll();
            }
        }

        @Override
        public void deleteTranslation(Long id) {
            translationRepository.deleteById(id);
        }

        @Override
        @Transactional
        public Map<String, String> exportTranslations(String locale) {
            List<Translation> translations = translationRepository.findAllByLocale(locale);

            return translations.stream()
                    .collect(Collectors.toMap(Translation::getKey, Translation::getContent));
        }

        @Override
        @Transactional
        public List<String> getAllLocales() {
            return translationRepository.findAllLocales();
        }

        // ---- Private Helper ----
        private Set<Tag> processTags(Set<String> tagNames) {
            Set<Tag> tags = new HashSet<>();

            if (tagNames != null && !tagNames.isEmpty()) {
                List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
                tags.addAll(existingTags);

                Set<String> existingTagNames = existingTags.stream()
                        .map(Tag::getName)
                        .collect(Collectors.toSet());

                for (String tagName : tagNames) {
                    if (!existingTagNames.contains(tagName)) {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        tags.add(tagRepository.save(newTag));
                    }
                }
            }

            return tags;
        }
    }
