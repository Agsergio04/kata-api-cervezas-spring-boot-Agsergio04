package com.kata.beer.service;

import com.kata.beer.exception.ResourceNotFoundException;
import com.kata.beer.model.Style;
import com.kata.beer.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class StyleService {

    private final StyleRepository styleRepository;

    @Autowired
    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    /**
     * Obtiene todos los estilos
     */
    @Transactional(readOnly = true)
    public List<Style> getAllStyles() {
        return styleRepository.findAll();
    }

    /**
     * Obtiene un estilo por su ID
     */
    @Transactional(readOnly = true)
    public Style getStyleById(Integer id) {
        return styleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estilo no encontrado con id: " + id));
    }
}

