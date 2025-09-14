package com.digitaltolks.translation_management_services.controller;

import com.digitaltolks.translation_management_services.model.Tag;
import com.digitaltolks.translation_management_services.services.TagService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private TagService tagService;

    @Test
    @DisplayName("GET /api/tags -> 200 & list of tags")
    void listTags_ok() throws Exception {
        Mockito.when(tagService.getAllTags()).thenReturn(List.of(
                new Tag(1L, "urgent", null),
                new Tag(2L, "legal", null)
        ));

        mvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("urgent"));
    }

    @Test
    @DisplayName("POST /api/tags -> 200 OK")
    void create_ok() throws Exception {
        Tag req = new Tag(null, "finance", null);
        Tag resp = new Tag(10L, "finance", null);

        Mockito.when(tagService.createTag(Mockito.any())).thenReturn(resp);

        mvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("finance"));
    }

    @Test
    @DisplayName("DELETE /api/tags/{id} -> 204 no content")
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/tags/{id}", 5))
                .andExpect(status().isNoContent());

        Mockito.verify(tagService).deleteTag(5L);
    }

    @Test
    @DisplayName("GET /api/tags/{id} -> 200 OK when found")
    void getById_ok() throws Exception {
        Tag tag = new Tag(3L, "medical", null);
        Mockito.when(tagService.getTagById(3L)).thenReturn(Optional.of(tag));

        mvc.perform(get("/api/tags/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("medical"));
    }

    @Test
    @DisplayName("GET /api/tags/{id} -> 404 when not found")
    void getById_notFound() throws Exception {
        Mockito.when(tagService.getTagById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/tags/{id}", 99))
                .andExpect(status().isNotFound());
    }
}
