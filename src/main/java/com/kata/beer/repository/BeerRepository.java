package com.kata.beer.repository;

import com.kata.beer.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    List<Beer> findByNameContainingIgnoreCase(String name);
    List<Beer> findByBreweryId(Integer breweryId);
    List<Beer> findByCategoryId(Integer categoryId);
    List<Beer> findByStyleId(Integer styleId);
}

