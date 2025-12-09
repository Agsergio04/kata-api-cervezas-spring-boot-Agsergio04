package com.kata.beer.service;

import com.kata.beer.exception.ResourceNotFoundException;
import com.kata.beer.model.Brewery;
import com.kata.beer.repository.BreweryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class BreweryService {

    private final BreweryRepository breweryRepository;

    @Autowired
    public BreweryService(BreweryRepository breweryRepository) {
        this.breweryRepository = breweryRepository;
    }

    /**
     * Obtiene todas las cervecerías
     */
    @Transactional(readOnly = true)
    public List<Brewery> getAllBreweries() {
        return breweryRepository.findAll();
    }

    /**
     * Obtiene una cervecería por su ID
     */
    @Transactional(readOnly = true)
    public Brewery getBreweryById(Integer id) {
        return breweryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cervecería no encontrada con id: " + id));
    }
}

