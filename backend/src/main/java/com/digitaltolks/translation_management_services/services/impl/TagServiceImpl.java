package com.digitaltolks.translation_management_services.services.impl;

import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.repository.TagRepository;
import com.digitaltolks.translation_management_services.services.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag updateTag(Long id, Tag tagDetails) {
        return tagRepository.findById(id).map(tag -> {
            tag.setName(tagDetails.getName());
            return tagRepository.save(tag);
        }).orElseThrow(() -> new RuntimeException("Tag not found with id " + id));
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
