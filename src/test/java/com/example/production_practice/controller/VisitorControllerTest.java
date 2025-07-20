package com.example.production_practice.controller;


import com.example.production_practice.ProductionPracticeApplication;
import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.enums.Gender;
import com.example.production_practice.service.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitorController.class)
@ContextConfiguration(classes = ProductionPracticeApplication.class)
public class VisitorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitorService visitorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/visitors - success")
    void getAllVisitors_success() throws Exception {
        List<VisitorResponseDTO> visitors = List.of(
                new VisitorResponseDTO(1L, "Alice", 25, Gender.FEMALE),
                new VisitorResponseDTO(2L, "Bob", 30, Gender.MALE)
        );
        when(visitorService.findAll()).thenReturn(visitors);

        mockMvc.perform(get("/api/visitors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].gender").value("MALE"));
    }

    @Test
    @DisplayName("GET /api/visitors/{id} - found")
    void getVisitorById_found() throws Exception {
        VisitorResponseDTO visitor = new VisitorResponseDTO(1L, "Alice", 25, Gender.FEMALE);
        when(visitorService.findById(1L)).thenReturn(visitor);

        mockMvc.perform(get("/api/visitors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.gender").value("FEMALE"));
    }

    @Test
    @DisplayName("GET /api/visitors/{id} - not found")
    void getVisitorById_notFound() throws Exception {
        when(visitorService.findById(99L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(get("/api/visitors/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/visitors - success")
    void addVisitor_success() throws Exception {
        VisitorRequestDTO request = new VisitorRequestDTO("Charlie", 40, Gender.MALE);
        VisitorResponseDTO responseDTO = new VisitorResponseDTO(1L, "Charlie", 40, Gender.MALE);
        when(visitorService.save(any(VisitorRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Charlie"));
    }

    @Test
    @DisplayName("POST /api/visitors - validation fail (negative age)")
    void addVisitor_validationFail() throws Exception {
        // Возраст отрицательный
        VisitorRequestDTO request = new VisitorRequestDTO("Charlie", -5, Gender.MALE);

        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/visitors/{id} - success")
    void updateVisitor_success() throws Exception {
        VisitorRequestDTO request = new VisitorRequestDTO("UpdatedName", 35, Gender.UNKNOWN);

        doNothing().when(visitorService).update(eq(1L), any(VisitorRequestDTO.class));

        mockMvc.perform(put("/api/visitors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/visitors/{id} - not found")
    void updateVisitor_notFound() throws Exception {
        VisitorRequestDTO request = new VisitorRequestDTO("Name", 35, Gender.UNKNOWN);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"))
                .when(visitorService).update(eq(99L), any(VisitorRequestDTO.class));

        mockMvc.perform(put("/api/visitors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/visitors/{id} - success")
    void deleteVisitor_success() throws Exception {
        doNothing().when(visitorService).remove(1L);

        mockMvc.perform(delete("/api/visitors/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/visitors/{id} - not found")
    void deleteVisitor_notFound() throws Exception {
        org.mockito.Mockito.doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found"))
                .when(visitorService).remove(99L);

        mockMvc.perform(delete("/api/visitors/99"))
                .andExpect(status().isNotFound());
    }
}
