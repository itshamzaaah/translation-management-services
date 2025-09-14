package com.digitaltolks.translation_management_services.controller;

import com.digitaltolks.translation_management_services.model.Translation;
import com.digitaltolks.translation_management_services.services.TranslationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

// TranslationController.java
@RestController
@RequestMapping("/api/translations")
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public ResponseEntity<Translation> createTranslation(
            @RequestBody Translation translation,
            @RequestParam(required = false) Set<String> tags) {
        Translation created = translationService.createTranslation(translation, tags);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Translation> updateTranslation(
            @PathVariable Long id,
            @RequestBody Translation translation,
            @RequestParam(required = false) Set<String> tags) {
        Translation updated = translationService.updateTranslation(id, translation, tags);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Translation> getTranslation(@PathVariable Long id) {
        Translation translation = translationService.getTranslation(id);
        return ResponseEntity.ok(translation);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Translation>> searchTranslations(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String locale,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String tag) {
        List<Translation> translations = translationService.searchTranslations(key, locale, content, tag);
        return ResponseEntity.ok(translations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, String>> exportTranslations(
            @RequestParam String locale) {
        Map<String, String> translations = translationService.exportTranslations(locale);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/locales")
    public ResponseEntity<List<String>> getAllLocales() {
        List<String> locales = translationService.getAllLocales();
        return ResponseEntity.ok(locales);
    }
}
