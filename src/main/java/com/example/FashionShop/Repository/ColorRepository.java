package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.Color;
import com.example.FashionShop.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, String> {
    @Query(value = "SELECT DISTINCT color_id FROM color_products", nativeQuery = true)
    List<String> findAllColor();
}
