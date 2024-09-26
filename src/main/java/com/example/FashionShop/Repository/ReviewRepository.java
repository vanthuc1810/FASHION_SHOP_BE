package com.example.FashionShop.Repository;

import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    @Query(value = "SELECT * FROM review WHERE id_product = :idProduct ORDER BY posted_time DESC", nativeQuery = true)
    List<Review> findAllByIdProduct(@Param("idProduct") String idProduct);

    @Query(value = "SELECT * FROM review WHERE id_user = :idUser ORDER BY posted_time DESC", nativeQuery = true)
    List<Review> findAllByIdUser(@Param("idUser") String idUser);
}
