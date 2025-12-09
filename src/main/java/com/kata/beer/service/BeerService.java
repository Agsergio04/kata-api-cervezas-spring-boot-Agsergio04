package com.kata.beer.service;

import com.kata.beer.exception.ResourceNotFoundException;
import com.kata.beer.model.Beer;
import com.kata.beer.repository.BeerRepository;
import com.kata.beer.repository.BreweryRepository;
import com.kata.beer.repository.CategoryRepository;
import com.kata.beer.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class BeerService {

    private final BeerRepository beerRepository;
    private final BreweryRepository breweryRepository;
    private final CategoryRepository categoryRepository;
    private final StyleRepository styleRepository;

    @Autowired
    public BeerService(BeerRepository beerRepository,
                      BreweryRepository breweryRepository,
                      CategoryRepository categoryRepository,
                      StyleRepository styleRepository) {
        this.beerRepository = beerRepository;
        this.breweryRepository = breweryRepository;
        this.categoryRepository = categoryRepository;
        this.styleRepository = styleRepository;
    }

    /**
     * Obtiene todas las cervezas
     */
    @Transactional(readOnly = true)
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    /**
     * Obtiene una cerveza por su ID
     */
    @Transactional(readOnly = true)
    public Beer getBeerById(Integer id) {
        return beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cerveza no encontrada con id: " + id));
    }

    /**
     * Crea una nueva cerveza
     */
    public Beer createBeer(Beer beer) {
        validateBeerRelationships(beer);
        return beerRepository.save(beer);
    }

    /**
     * Actualiza completamente una cerveza existente
     */
    public Beer updateBeer(Integer id, Beer beerDetails) {
        Beer beer = getBeerById(id);

        validateBeerRelationships(beerDetails);

        beer.setName(beerDetails.getName());
        beer.setBrewery(beerDetails.getBrewery());
        beer.setCategory(beerDetails.getCategory());
        beer.setStyle(beerDetails.getStyle());
        beer.setAbv(beerDetails.getAbv());
        beer.setIbu(beerDetails.getIbu());
        beer.setSrm(beerDetails.getSrm());
        beer.setUpc(beerDetails.getUpc());
        beer.setFilepath(beerDetails.getFilepath());
        beer.setDescript(beerDetails.getDescript());

        return beerRepository.save(beer);
    }

    /**
     * Actualiza parcialmente una cerveza existente
     */
    public Beer partialUpdateBeer(Integer id, Beer beerDetails) {
        Beer beer = getBeerById(id);

        if (beerDetails.getName() != null) {
            beer.setName(beerDetails.getName());
        }

        if (beerDetails.getBrewery() != null) {
            validateBreweryExists(beerDetails.getBrewery().getId());
            beer.setBrewery(beerDetails.getBrewery());
        }

        if (beerDetails.getCategory() != null) {
            validateCategoryExists(beerDetails.getCategory().getId());
            beer.setCategory(beerDetails.getCategory());
        }

        if (beerDetails.getStyle() != null) {
            validateStyleExists(beerDetails.getStyle().getId());
            beer.setStyle(beerDetails.getStyle());
        }

        if (beerDetails.getAbv() != null) {
            beer.setAbv(beerDetails.getAbv());
        }

        if (beerDetails.getIbu() != null) {
            beer.setIbu(beerDetails.getIbu());
        }

        if (beerDetails.getSrm() != null) {
            beer.setSrm(beerDetails.getSrm());
        }

        if (beerDetails.getUpc() != null) {
            beer.setUpc(beerDetails.getUpc());
        }

        if (beerDetails.getFilepath() != null) {
            beer.setFilepath(beerDetails.getFilepath());
        }

        if (beerDetails.getDescript() != null) {
            beer.setDescript(beerDetails.getDescript());
        }

        return beerRepository.save(beer);
    }

    /**
     * Elimina una cerveza
     */
    public void deleteBeer(Integer id) {
        Beer beer = getBeerById(id);
        beerRepository.delete(beer);
    }

    /**
     * Valida que las relaciones de la cerveza existan
     */
    private void validateBeerRelationships(Beer beer) {
        if (beer.getBrewery() == null || beer.getBrewery().getId() == null) {
            throw new IllegalArgumentException("La cerveza debe tener una cervecería asociada");
        }

        if (beer.getCategory() == null || beer.getCategory().getId() == null) {
            throw new IllegalArgumentException("La cerveza debe tener una categoría asociada");
        }

        if (beer.getStyle() == null || beer.getStyle().getId() == null) {
            throw new IllegalArgumentException("La cerveza debe tener un estilo asociado");
        }

        validateBreweryExists(beer.getBrewery().getId());
        validateCategoryExists(beer.getCategory().getId());
        validateStyleExists(beer.getStyle().getId());
    }

    private void validateBreweryExists(Integer breweryId) {
        breweryRepository.findById(breweryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cervecería no encontrada con id: " + breweryId));
    }

    private void validateCategoryExists(Integer categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + categoryId));
    }

    private void validateStyleExists(Integer styleId) {
        styleRepository.findById(styleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estilo no encontrado con id: " + styleId));
    }
}

