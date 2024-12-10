package com.example.FashionShop.Repository;

import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.CardItem;

import java.util.Objects;
import java.util.Optional;

@Repository
public interface CardItemRepository extends JpaRepository<CardItem, Integer> {
}
