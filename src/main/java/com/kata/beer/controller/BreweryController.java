package com.kata.beer.controller;

import com.kata.beer.model.Brewery;
import com.kata.beer.service.BreweryService;
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
@Tag(name = "Breweries", description = "API de consulta de cervecerías")
@CrossOrigin(origins = "*")
public class BreweryController {

    private final BreweryService breweryService;

    @Autowired
    public BreweryController(BreweryService breweryService) {
        this.breweryService = breweryService;
    }

    @GetMapping("/breweries")
    @Operation(summary = "Obtener todas las cervecerías", description = "Retorna una lista de todas las cervecerías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cervecerías obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Brewery>> getAllBreweries() {
        List<Brewery> breweries = breweryService.getAllBreweries();
        return ResponseEntity.ok(breweries);
    }

    @GetMapping("/brewerie/{id}")
    @Operation(summary = "Obtener cervecería por ID", description = "Retorna una cervecería específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cervecería encontrada"),
        @ApiResponse(responseCode = "404", description = "Cervecería no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Brewery> getBreweryById(@PathVariable Integer id) {
        Brewery brewery = breweryService.getBreweryById(id);
        return ResponseEntity.ok(brewery);
    }
}

