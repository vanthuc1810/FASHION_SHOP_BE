package com.example.FashionShop.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "SELECT * FROM review WHERE id_product = :idProduct ORDER BY posted_time DESC", nativeQuery = true)
    Page<Review> findAllByIdProduct(@Param("idProduct") Integer idProduct, Pageable pageable);

    @Query(value = "SELECT * FROM review WHERE id_user = :idUser ORDER BY posted_time DESC", nativeQuery = true)
    List<Review> findAllByIdUser(@Param("idUser") Integer idUser);
}
