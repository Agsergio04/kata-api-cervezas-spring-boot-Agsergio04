package com.kata.beer.repository;

import com.kata.beer.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCatNameContainingIgnoreCase(String catName);
}

