package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardItemRepository extends JpaRepository<CardItem, String> {
}
