package com.example.production_practice.controller;

import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @Operation(summary = "Получить список всех посетителей")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно получен список посетителей",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = VisitorResponseDTO.class)))
    )
    @GetMapping
    public List<VisitorResponseDTO> getAllVisitors() {
        return visitorService.findAll();
    }

    @Operation(summary = "Получить посетителя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Посетитель найден",
                    content = @Content(schema = @Schema(implementation = VisitorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    @GetMapping("/{id}")
    public VisitorResponseDTO getVisitor(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long id) {
        return visitorService.findById(id);
    }

    @Operation(summary = "Добавить посетителя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Посетитель добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    @PostMapping
    public ResponseEntity<VisitorResponseDTO> addVisitor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового посетителя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VisitorRequestDTO.class))
            )
            @Valid @RequestBody VisitorRequestDTO visitorDTO) {
        VisitorResponseDTO dto = visitorService.save(visitorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Обновить информацию о посетителе",
            description = "Обновляет данные посетителя по ID. Все поля, кроме имени, обязательны."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Посетитель успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    @PutMapping("/{id}")
    public void updateVisitor(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления посетителя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VisitorRequestDTO.class))
            )
            @Valid @RequestBody VisitorRequestDTO visitorDTO) {
        visitorService.update(id, visitorDTO);
    }

    @Operation(summary = "Удалить посетителя по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Посетитель успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    @DeleteMapping("/{id}")
    public void deleteVisitor(
            @Parameter(description = "id посетителя", example = "1")
            @PathVariable Long id) {
        visitorService.remove(id);
    }
}
