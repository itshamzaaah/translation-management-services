package com.digitaltolks.translation_management_services.utils;

import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.model.Translation;
import com.digitaltolks.translation_management_services.repository.TagRepository;
import com.digitaltolks.translation_management_services.repository.TranslationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    @Test
    @DisplayName("run() seeds initial data when 'load-test-data' argument is provided")
    void run_seeds() throws Exception {
        TagRepository tagRepo = mock(TagRepository.class);
        TranslationRepository trRepo = mock(TranslationRepository.class);

        DataLoader loader = new DataLoader(trRepo, tagRepo);
        loader.run("load-test-data");

        ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
        verify(tagRepo, atLeastOnce()).save(tagCaptor.capture());
        assertThat(tagCaptor.getAllValues()).isNotEmpty();

        verify(trRepo, atLeastOnce()).save(any(Translation.class));
    }
}
