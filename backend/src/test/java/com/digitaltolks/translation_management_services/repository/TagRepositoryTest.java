package com.digitaltolks.translation_management_services.repository;

import com.digitaltolks.translation_management_services.model.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TagRepositoryTest {

    @Autowired
    TagRepository repo;

    @Test
    @DisplayName("save + findByName works")
    void save_and_lookup() {
        Tag t = new Tag();
        t.setName("Urgent");
        repo.save(t);

        Optional<Tag> found = repo.findByName("Urgent");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Urgent");
    }

    @Test
    @DisplayName("findByNameIn returns multiple tags")
    void find_by_name_in() {
        Tag t1 = new Tag();
        t1.setName("Finance");
        repo.save(t1);

        Tag t2 = new Tag();
        t2.setName("Legal");
        repo.save(t2);

        List<Tag> found = repo.findByNameIn(Arrays.asList("Finance", "Legal", "Other"));
        assertThat(found).extracting(Tag::getName)
                .containsExactlyInAnyOrder("Finance", "Legal");
    }
}
