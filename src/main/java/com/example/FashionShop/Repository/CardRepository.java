package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.CardItem;
import com.example.FashionShop.Entity.Category;
import com.example.FashionShop.Entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.Card;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    @Query(value = "SELECT * FROM card where id_user = :idUser", nativeQuery = true)
    List<Card> findAllByIdUser(@Param("idUser") Integer idUser);
}
