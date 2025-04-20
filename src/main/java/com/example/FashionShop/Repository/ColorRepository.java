package com.example.FashionShop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, String> {
    @Query(value = "SELECT DISTINCT name_color FROM color", nativeQuery = true)
    List<String> findAllColor();
}
