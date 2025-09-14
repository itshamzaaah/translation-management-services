package com.digitaltolks.translation_management_services.service;

import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.repository.TagRepository;
import com.digitaltolks.translation_management_services.services.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createTag() should persist entity")
    void createTag_ok() {
        Tag tag = new Tag();
        tag.setName("Urgent");

        when(tagRepository.save(any(Tag.class))).thenAnswer(inv -> {
            Tag t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        Tag created = service.createTag(tag);

        ArgumentCaptor<Tag> captor = ArgumentCaptor.forClass(Tag.class);
        verify(tagRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Urgent");
        assertThat(created.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getAllTags() should return list of tags")
    void getAllTags_ok() {
        when(tagRepository.findAll()).thenReturn(List.of(new Tag(1L, "finance")));

        List<Tag> tags = service.getAllTags();

        assertThat(tags).hasSize(1);
        assertThat(tags.get(0).getName()).isEqualTo("finance");
    }

    @Test
    @DisplayName("getTagById() should return tag when found")
    void getTagById_found() {
        Tag tag = new Tag(2L, "legal");
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));

        Optional<Tag> found = service.getTagById(2L);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("legal");
    }

    @Test
    @DisplayName("getTagById() should return empty when not found")
    void getTagById_notFound() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Tag> found = service.getTagById(99L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("updateTag() should update and return entity")
    void updateTag_ok() {
        Tag existing = new Tag(3L, "old");
        Tag update = new Tag();
        update.setName("new");

        when(tagRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(tagRepository.save(any(Tag.class))).thenAnswer(inv -> inv.getArgument(0));

        Tag updated = service.updateTag(3L, update);

        assertThat(updated.getName()).isEqualTo("new");
        verify(tagRepository).save(existing);
    }

    @Test
    @DisplayName("updateTag() should throw when not found")
    void updateTag_notFound() {
        Tag update = new Tag();
        update.setName("ghost");

        when(tagRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateTag(404L, update));
    }

    @Test
    @DisplayName("deleteTag() should call repository")
    void deleteTag_ok() {
        doNothing().when(tagRepository).deleteById(5L);

        service.deleteTag(5L);

        verify(tagRepository).deleteById(5L);
    }
}
