package com.example.FashionShop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, String> {
    @Query(value = "SELECT DISTINCT name_size FROM size", nativeQuery = true)
    List<String> findAllSize();
}
