package com.digitaltolks.translation_management_services.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "translations", indexes = {
        @Index(name = "idx_key_locale", columnList = "`key`,locale"),
        @Index(name = "idx_locale", columnList = "locale")
})
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`key`", nullable = false) // Escape with backticks
    private String key;

    // Rest of the entity remains the same
    @Column(nullable = false)
    private String locale;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "translation_tags",
            joinColumns = @JoinColumn(name = "translation_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Constructors, getters, setters

    public Translation(Long id, String key, String locale, String content, Set<Tag> tags) {
        this.id = id;
        this.key = key;
        this.locale = locale;
        this.content = content;
        this.tags = tags;
    }

    public Translation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
