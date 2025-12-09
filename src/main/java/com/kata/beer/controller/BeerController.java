package com.kata.beer.controller;

import com.kata.beer.model.Beer;
import com.kata.beer.service.BeerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Beers", description = "API de gestión de cervezas")
@CrossOrigin(origins = "*")
public class BeerController {

    private final BeerService beerService;

    @Autowired
    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/beers")
    @Operation(summary = "Obtener todas las cervezas", description = "Retorna una lista de todas las cervezas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cervezas obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Beer>> getAllBeers() {
        List<Beer> beers = beerService.getAllBeers();
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/beer/{id}")
    @Operation(summary = "Obtener cerveza por ID", description = "Retorna una cerveza específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cerveza encontrada"),
        @ApiResponse(responseCode = "404", description = "Cerveza no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Beer> getBeerById(@PathVariable Integer id) {
        Beer beer = beerService.getBeerById(id);
        return ResponseEntity.ok(beer);
    }

    @PostMapping("/beer")
    @Operation(summary = "Crear nueva cerveza", description = "Crea una nueva cerveza en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cerveza creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Beer> createBeer(@Valid @RequestBody Beer beer) {
        Beer newBeer = beerService.createBeer(beer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBeer);
    }

    @PutMapping("/beer/{id}")
    @Operation(summary = "Actualizar cerveza", description = "Actualiza completamente una cerveza existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cerveza actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cerveza no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Beer> updateBeer(
            @PathVariable Integer id,
            @Valid @RequestBody Beer beerDetails) {
        Beer updatedBeer = beerService.updateBeer(id, beerDetails);
        return ResponseEntity.ok(updatedBeer);
    }

    @PatchMapping("/beer/{id}")
    @Operation(summary = "Actualizar parcialmente cerveza", description = "Actualiza parcialmente una cerveza existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cerveza actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cerveza no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Beer> partialUpdateBeer(
            @PathVariable Integer id,
            @RequestBody Beer beerDetails) {
        Beer updatedBeer = beerService.partialUpdateBeer(id, beerDetails);
        return ResponseEntity.ok(updatedBeer);
    }

    @DeleteMapping("/beer/{id}")
    @Operation(summary = "Eliminar cerveza", description = "Elimina una cerveza del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cerveza eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cerveza no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteBeer(@PathVariable Integer id) {
        beerService.deleteBeer(id);
        return ResponseEntity.noContent().build();
    }
}

