package com.digitaltolks.translation_management_services.controller;

import com.digitaltolks.translation_management_services.config.SecurityConfig;
import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.model.Translation;
import com.digitaltolks.translation_management_services.services.TranslationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TranslationController.class, excludeAutoConfiguration = SecurityConfig.class)
class TranslationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private TranslationService translationService;

    @Test
    @DisplayName("GET /api/translations/{id} -> 200 OK")
    void getById_ok() throws Exception {
        Translation t = new Translation(1L, "greeting", "en", "hello", Set.of());
        Mockito.when(translationService.getTranslation(1L)).thenReturn(t);

        mvc.perform(get("/api/translations/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.key").value("greeting"))
                .andExpect(jsonPath("$.locale").value("en"))
                .andExpect(jsonPath("$.content").value("hello"));
    }

    @Test
    @DisplayName("POST /api/translations -> 201 Created")
    void create_ok() throws Exception {
        Translation req = new Translation(null, "welcome", "en", "hi there", Set.of());
        Translation resp = new Translation(10L, "welcome", "en", "hi there", Set.of());

        Mockito.when(translationService.createTranslation(Mockito.any(), Mockito.any()))
                .thenReturn(resp);

        mvc.perform(post("/api/translations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.key").value("welcome"))
                .andExpect(jsonPath("$.content").value("hi there"));
    }

    @Test
    @DisplayName("GET /api/translations/search?key=... -> 200 filtered")
    void search_ok() throws Exception {
        Translation t = new Translation(5L, "farewell", "en", "goodbye", Set.of(new Tag(1L, "urgent", null)));

        Mockito.when(translationService.searchTranslations("farewell", null, null, null))
                .thenReturn(List.of(t));

        mvc.perform(get("/api/translations/search").param("key", "farewell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].key").value("farewell"))
                .andExpect(jsonPath("$[0].content").value("goodbye"));
    }

    @Test
    @DisplayName("DELETE /api/translations/{id} -> 204 No Content")
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/translations/{id}", 7))
                .andExpect(status().isNoContent());

        Mockito.verify(translationService).deleteTranslation(7L);
    }
}
