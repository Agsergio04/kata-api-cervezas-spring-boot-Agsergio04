package com.kata.beer.repository;

import com.kata.beer.model.Brewery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BreweryRepository extends JpaRepository<Brewery, Integer> {
    List<Brewery> findByNameContainingIgnoreCase(String name);
    List<Brewery> findByCity(String city);
    List<Brewery> findByCountry(String country);
}

