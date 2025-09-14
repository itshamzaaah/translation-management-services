package com.digitaltolks.translation_management_services.services;

import com.digitaltolks.translation_management_services.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Tag createTag(Tag tag);

    List<Tag> getAllTags();

    Optional<Tag> getTagById(Long id);

    Tag updateTag(Long id, Tag tagDetails);

    void deleteTag(Long id);
}