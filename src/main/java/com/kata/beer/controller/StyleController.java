package com.kata.beer.controller;

import com.kata.beer.model.Style;
import com.kata.beer.service.StyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Styles", description = "API de consulta de estilos")
@CrossOrigin(origins = "*")
public class StyleController {

    private final StyleService styleService;

    @Autowired
    public StyleController(StyleService styleService) {
        this.styleService = styleService;
    }

    @GetMapping("/styles")
    @Operation(summary = "Obtener todos los estilos", description = "Retorna una lista de todos los estilos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estilos obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Style>> getAllStyles() {
        List<Style> styles = styleService.getAllStyles();
        return ResponseEntity.ok(styles);
    }

    @GetMapping("/style/{id}")
    @Operation(summary = "Obtener estilo por ID", description = "Retorna un estilo específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estilo encontrado"),
        @ApiResponse(responseCode = "404", description = "Estilo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Style> getStyleById(@PathVariable Integer id) {
        Style style = styleService.getStyleById(id);
        return ResponseEntity.ok(style);
    }
}

