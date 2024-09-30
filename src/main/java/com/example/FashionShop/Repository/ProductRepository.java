package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM product WHERE discount BETWEEN :start AND :end AND manufacturer = :manufacturer AND id_category = :id_category", nativeQuery = true)
    List<Product> searchProducts(@Param("start") int start, @Param("end") int end, @Param("manufacturer") String manufacturer, @Param("id_category") String idCategory);

    @Query(value = "SELECT DISTINCT c.name FROM product p INNER JOIN category c ON p.id_category = c.id_category", nativeQuery = true)
    List<String> getAllCategory();

}
