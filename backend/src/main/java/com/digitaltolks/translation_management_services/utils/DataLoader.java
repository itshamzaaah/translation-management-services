package com.digitaltolks.translation_management_services.utils;

import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.model.Translation;
import com.digitaltolks.translation_management_services.repository.TagRepository;
import com.digitaltolks.translation_management_services.repository.TranslationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// DataLoader.java
@Component
public class DataLoader implements CommandLineRunner {
    private final TranslationRepository translationRepository;
    private final TagRepository tagRepository;

    public DataLoader(TranslationRepository translationRepository,
                      TagRepository tagRepository) {
        this.translationRepository = translationRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only run if explicitly requested
        if (args.length > 0 && "load-test-data".equals(args[0])) {
            loadTestData();
        }
    }

    private void loadTestData() {
        // Create some common tags
        Tag webTag = createTagIfNotExists("web");
        Tag mobileTag = createTagIfNotExists("mobile");
        Tag desktopTag = createTagIfNotExists("desktop");

        List<String> locales = Arrays.asList("en", "fr", "es", "de", "it");
        List<Tag> tags = Arrays.asList(webTag, mobileTag, desktopTag);

        // Create 100,000+ test translations
        for (int i = 0; i < 100000; i++) {
            String key = "key." + i;
            String locale = locales.get(i % locales.size());
            String content = "Translation content for key " + i + " in " + locale;

            Translation translation = new Translation();
            translation.setKey(key);
            translation.setLocale(locale);
            translation.setContent(content);

            // Assign random tags
            Set<Tag> translationTags = new HashSet<>();
            if (i % 3 == 0) translationTags.add(webTag);
            if (i % 5 == 0) translationTags.add(mobileTag);
            if (i % 7 == 0) translationTags.add(desktopTag);

            translation.setTags(translationTags);

            translationRepository.save(translation);

            if (i % 1000 == 0) {
                System.out.println("Created " + i + " translations");
                translationRepository.flush();
            }
        }

        System.out.println("Test data loading completed");
    }

    private Tag createTagIfNotExists(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    return tagRepository.save(tag);
                });
    }
}