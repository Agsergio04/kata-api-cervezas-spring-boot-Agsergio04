package com.kata.beer.repository;

import com.kata.beer.model.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StyleRepository extends JpaRepository<Style, Integer> {
    List<Style> findByStyleNameContainingIgnoreCase(String styleName);
    List<Style> findByCatId(Integer catId);
}

