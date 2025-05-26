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

    public static Specification<Product> nameContains(String keyword) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Product> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + description + "%");
    }

    public static Specification<Product> hasIdCategory(List<Integer> idCategorys){
        return (root, query, criteriaBuilder) ->
        {
            if (idCategorys == null || idCategorys.isEmpty()) {
            {
                return criteriaBuilder.conjunction();
            }
            }else {
                // Join bang
                Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                // Tao cac dieu kien
                Predicate hasIdCategory = categoryJoin.get("idCategory").in(idCategorys);
                return hasIdCategory;
            }
        };
    }

    public static Specification<Product> hasIdProduct(List<Integer> idProducts){
        return (root, query, criteriaBuilder) ->
        {
            if (idProducts == null || idProducts.isEmpty()) {
                {
                    return criteriaBuilder.conjunction();
                }
            }else {
                // Tao cac dieu kien
                Predicate hasIdProduct = root.get("idProduct").in(idProducts);
                return hasIdProduct;
            }
        };
    }

    public static Specification<Product> hasDeleted(List<Boolean> delete){
        return (root, query, criteriaBuilder) ->
        {
            if(delete == null || delete.isEmpty()){
                return criteriaBuilder.conjunction(); // Không thêm điều kiện
            }
            Predicate hasDeleted = root.get("deleted").in(delete);
            return hasDeleted;
        };
    }

    public static Specification<Product> hasPriceInRange(double minPrice, double maxPrice) {
        return (root, query, criteriaBuilder) ->
        {
            if (minPrice >= maxPrice)
            {
                return criteriaBuilder.conjunction(); // Không thêm điều kiện
            }else {
                // Tao cac dieu kien
                Predicate hasPriceInRange = criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
                return hasPriceInRange;
            }
        };
    }

    public static Specification<Product> hasManufacturer(List<String> nameManuFacturer) {
        return (root, query, criteriaBuilder) -> {
            // Kiểm tra nếu list là null hoặc rỗng
            if (nameManuFacturer == null || nameManuFacturer.isEmpty()) {
                System.out.println("Khong co");
                return criteriaBuilder.conjunction(); // Không thêm điều kiện nếu danh sách trống hoặc null
            } else {
                // Tạo điều kiện kiểm tra manufacturer trong danh sách
                System.out.println(nameManuFacturer.get(0));
                Predicate hasManufacturer = root.get("manufacturer").in(nameManuFacturer);
                return hasManufacturer;
            }
        };
    }

    public static Specification<Product> findManufracture() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.isNotNull(root.get("manufacturer"));
        };
    }

    public static Specification<Product> hasColors(List<String> colors) {
        return (root, query, criteriaBuilder) -> {
            // Kiểm tra null hoặc rỗng
            if (colors == null || colors.isEmpty()) {
                return criteriaBuilder.conjunction(); // Không thêm điều kiện
            } else {
                // Join bảng
                Join<Product, ColorProduct> colorProductJoin = root.join("colorProducts", JoinType.INNER);
                Join<ColorProduct, Color> colorJoin = colorProductJoin.join("color", JoinType.INNER);

                // Tạo điều kiện
                Predicate hasColors = colorJoin.get("nameColor").in(colors);
                return hasColors;
            }
        };
    }

    public static Specification<Product> hasSizes(List<String> sizes) {
        return (root, query, criteriaBuilder) ->
        {
            // Kiểm tra null hoặc rỗng
            if (sizes == null || sizes.isEmpty()) {
                return criteriaBuilder.conjunction(); // Không thêm điều kiện
            } else {
                // Join bang
                Join<Product, SizeProduct> sizeProductJoin = root.join("sizeProducts", JoinType.INNER);

                Join<SizeProduct, Size>   sizeJoin = sizeProductJoin.join("size", JoinType.INNER);
                // Tao cac dieu kien
                Predicate hasSizes = sizeJoin.get("nameSize").in(sizes);
                return hasSizes;
            }
        };
    }

}
