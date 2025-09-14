package com.digitaltolks.translation_management_services.repository;

import com.digitaltolks.translation_management_services.model.Translation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TranslationRepositoryTest {

    @Autowired
    TranslationRepository repo;

    @Test
    @DisplayName("save + findAll works")
    void save_and_find() {
        Translation t = new Translation();
        t.setKey("hello");
        t.setLocale("en");
        t.setContent("Hello World");
        repo.save(t);

        List<Translation> all = repo.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getKey()).isEqualTo("hello");
    }

    @Test
    @DisplayName("findByContentContainingIgnoreCase works")
    void search_like_content() {
        Translation t1 = new Translation();
        t1.setKey("greeting");
        t1.setLocale("en");
        t1.setContent("hej there");
        repo.save(t1);

        Translation t2 = new Translation();
        t2.setKey("world");
        t2.setLocale("en");
        t2.setContent("värld wide");
        repo.save(t2);

        List<Translation> res = repo.findByContentContainingIgnoreCase("hej");
        assertThat(res).extracting(Translation::getContent).contains("hej there");
    }
}
