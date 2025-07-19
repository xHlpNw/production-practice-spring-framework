package com.example.production_practice.controller;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.RestaurantMapper;
import com.example.production_practice.service.RestaurantService;
import com.example.production_practice.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Получить список всех ресторанов")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно получен список ресторанов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestaurantResponseDTO.class)))
    )
    @GetMapping
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @Operation(summary = "Получить ресторан по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ресторан найден",
                    content = @Content(schema = @Schema(implementation = RestaurantResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    })
    @GetMapping("/{id}")
    public RestaurantResponseDTO getRestaurant(
            @Parameter(description = "id ресторана", example = "1")
            @PathVariable Long id) {
        return restaurantService.findById(id);
    }

    @Operation(summary = "Добавить ресторан")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ресторан добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    @PostMapping
    public void addRestaurant(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового ресторана",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RestaurantRequestDTO.class))
            )
            @RequestBody RestaurantRequestDTO restaurantDTO) {
        restaurantService.save(restaurantDTO);
    }

    @Operation(
            summary = "Обновить информацию о ресторане",
            description = "Обновляет данные ресторана по ID. Все поля, кроме описания, обязательны."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ресторан успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    })
    @PutMapping("/{id}")
    public void updateRestaurant(
            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления ресторана",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RestaurantRequestDTO.class))
            )
            @RequestBody RestaurantRequestDTO restaurantDTO) {
        restaurantService.update(id, restaurantDTO);
    }

    @Operation(summary = "Удалить ресторан по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ресторан успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    })
    @DeleteMapping("/{id}")
    public void deleteRestaurant(
            @Parameter(description = "id ресторана", example = "1")
            @PathVariable Long id) {
        restaurantService.remove(id);
    }

    @GetMapping("/by-rating")
    public Page<RestaurantResponseDTO> getRestaurantsByRating(
            @RequestParam BigDecimal minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return restaurantService.findByRating(minRating, pageable);
    }
}
