package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, String> {
    @Query(value = "SELECT DISTINCT size_id FROM size_products", nativeQuery = true)
    List<String> findAllSize();
}
