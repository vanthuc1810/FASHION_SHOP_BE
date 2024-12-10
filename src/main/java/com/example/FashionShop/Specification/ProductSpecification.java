package com.example.FashionShop.Specification;

import com.example.FashionShop.Entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + description + "%");
    }

    public static Specification<Product> hasIdCategory(int idCategory){
        return (root, query, criteriaBuilder) ->
        {
            // Join bang
            Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
            // Tao cac dieu kien
            Predicate hasIdCategory = criteriaBuilder.equal(categoryJoin.get("idCategory"), idCategory);
            return hasIdCategory;
        };
    }

    public static Specification<Product> hasPriceInRange(Long minPrice, Long maxPrice) {
        return (root, query, criteriaBuilder) ->
        {
            // Tao cac dieu kien
            Predicate hasPriceInRange = criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            return hasPriceInRange;
        };
    }

    public static Specification<Product> hasManufacturer(String nameManuFacturer) {
        return (root, query, criteriaBuilder) ->
        {
            // Tao cac dieu kien
            Predicate hasManufacturer = criteriaBuilder.equal(root.get("manufacturer"), nameManuFacturer);
            return hasManufacturer;
        };
    }
    public static Specification<Product> hasColors(List<String> colors) {
        return (root, query, criteriaBuilder) ->
        {
            // Join bang
            Join<Product, ColorProduct> colorProductJoin = root.join("colorProducts", JoinType.INNER);

            Join<ColorProduct, Color>   colorJoin = colorProductJoin.join("color", JoinType.INNER);
            // Tao cac dieu kien
            Predicate hasColors = colorJoin.get("nameColor").in(colors);
            return hasColors;
        };
    }

    public static Specification<Product> hasSizes(List<String> sizes) {
        return (root, query, criteriaBuilder) ->
        {
            // Join bang
            Join<Product, SizeProduct> sizeProductJoin = root.join("sizeProducts", JoinType.INNER);

            Join<SizeProduct, Size>   sizeJoin = sizeProductJoin.join("size", JoinType.INNER);
            // Tao cac dieu kien
            Predicate hasSizes = sizeJoin.get("nameSize").in(sizes);
            return hasSizes;
        };
    }

}
