package com.digitaltolks.translation_management_services.service;

import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.model.Translation;
import com.digitaltolks.translation_management_services.repository.TagRepository;
import com.digitaltolks.translation_management_services.repository.TranslationRepository;
import com.digitaltolks.translation_management_services.services.impl.TranslationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TranslationServiceImplTest {

    @Mock
    TranslationRepository translationRepository;

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TranslationServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createTranslation() persists new translation with tags")
    void create_ok() {
        Translation input = new Translation();
        input.setKey("hello");
        input.setLocale("en");
        input.setContent("Hello");

        Tag urgent = new Tag(1L, "urgent");
        when(tagRepository.findByNameIn(Set.of("urgent"))).thenReturn(List.of(urgent));
        when(translationRepository.existsByKeyAndLocale("hello", "en")).thenReturn(false);
        when(translationRepository.save(any(Translation.class))).thenAnswer(inv -> {
            Translation t = inv.getArgument(0);
            t.setId(7L);
            return t;
        });

        Translation saved = service.createTranslation(input, Set.of("urgent"));

        assertThat(saved.getId()).isEqualTo(7L);
        assertThat(saved.getTags()).contains(urgent);
        verify(tagRepository).findByNameIn(Set.of("urgent"));
        verify(translationRepository).save(any(Translation.class));
    }

    @Test
    @DisplayName("createTranslation() throws if duplicate key+locale")
    void create_duplicate() {
        Translation input = new Translation();
        input.setKey("hello");
        input.setLocale("en");
        input.setContent("Hello");

        when(translationRepository.existsByKeyAndLocale("hello", "en")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.createTranslation(input, Set.of()));
    }

    @Test
    @DisplayName("updateTranslation() updates content and tags")
    void update_ok() {
        Translation existing = new Translation(1L, "hello", "en", "old content", Set.of());
        Translation updateData = new Translation();
        updateData.setContent("new content");

        Tag finance = new Tag(2L, "finance");

        when(translationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tagRepository.findByNameIn(Set.of("finance"))).thenReturn(List.of(finance));
        when(translationRepository.save(any(Translation.class))).thenAnswer(inv -> inv.getArgument(0));

        Translation updated = service.updateTranslation(1L, updateData, Set.of("finance"));

        assertThat(updated.getContent()).isEqualTo("new content");
        assertThat(updated.getTags()).contains(finance);
    }

    @Test
    @DisplayName("getTranslation() returns entity when found")
    void getTranslation_found() {
        Translation t = new Translation(2L, "bye", "en", "Goodbye", Set.of());
        when(translationRepository.findById(2L)).thenReturn(Optional.of(t));

        Translation found = service.getTranslation(2L);

        assertThat(found.getContent()).isEqualTo("Goodbye");
    }

    @Test
    @DisplayName("getTranslation() throws when not found")
    void getTranslation_notFound() {
        when(translationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getTranslation(99L));
    }

    @Test
    @DisplayName("searchTranslations() delegates by key")
    void search_byKey() {
        when(translationRepository.findByKeyContainingIgnoreCase("hello")).thenReturn(List.of(
                new Translation(10L, "hello", "en", "Hello", Set.of())
        ));

        List<Translation> result = service.searchTranslations("hello", null, null, null);

        assertThat(result).hasSize(1);
        verify(translationRepository).findByKeyContainingIgnoreCase("hello");
    }

    @Test
    @DisplayName("deleteTranslation() calls repository delete")
    void delete_ok() {
        doNothing().when(translationRepository).deleteById(5L);

        service.deleteTranslation(5L);

        verify(translationRepository).deleteById(5L);
    }

    @Test
    @DisplayName("exportTranslations() returns map of key->content")
    void export_ok() {
        List<Translation> translations = List.of(
                new Translation(1L, "hello", "en", "Hello", Set.of()),
                new Translation(2L, "bye", "en", "Goodbye", Set.of())
        );

        when(translationRepository.findAllByLocale("en")).thenReturn(translations);

        Map<String, String> exported = service.exportTranslations("en");

        assertThat(exported).containsEntry("hello", "Hello")
                .containsEntry("bye", "Goodbye");
    }

    @Test
    @DisplayName("getAllLocales() delegates to repo")
    void getAllLocales_ok() {
        when(translationRepository.findAllLocales()).thenReturn(List.of("en", "sv"));

        List<String> locales = service.getAllLocales();

        assertThat(locales).containsExactly("en", "sv");
    }
}
