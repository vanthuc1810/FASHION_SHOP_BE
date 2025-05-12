package com.example.FashionShop.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Page<Product> findAll(Pageable pageable);

    @Query(
            value =
                    "SELECT DISTINCT p.id_product, p.id_category, p.deleted, p.description, p.manufacturer, p.name, p.images, p.discount, p.price, p.unit_stock "
                            + "FROM product p "
                            + "INNER JOIN category c ON p.id_category = c.id_category "
                            + "INNER JOIN color_products cp ON cp.product_id = p.id_product "
                            + "INNER JOIN size_products sp ON sp.product_id = p.id_product "
                            + "WHERE p.discount BETWEEN :start AND :end "
                            + "AND (p.manufacturer = :manufacturer OR :manufacturer IS NULL) "
                            + "AND (c.id_category = :idCategory OR :idCategory IS NULL ) "
                            + "AND (FIND_IN_SET(cp.color_id, :colors) OR :colors IS NULL OR :colors = '') "
                            + // Kiểm tra colors
                            "AND (FIND_IN_SET(sp.size_id, :sizes) OR :sizes IS NULL OR :sizes = '') ",
            countQuery = "SELECT COUNT(DISTINCT p.id_product) " + "FROM product p "
                    + "INNER JOIN category c ON p.id_category = c.id_category "
                    + "INNER JOIN color_products cp ON cp.product_id = p.id_product "
                    + "INNER JOIN size_products sp ON sp.product_id = p.id_product "
                    + "WHERE p.discount BETWEEN :start AND :end "
                    + "AND (p.manufacturer = :manufacturer OR :manufacturer IS NULL) "
                    + "AND (c.id_category = :idCategory OR :idCategory IS NULL ) "
                    + "AND (FIND_IN_SET(cp.color_id, :colors) OR :colors IS NULL OR :colors = '') "
                    + // Kiểm tra colors
                    "AND (FIND_IN_SET(sp.size_id, :sizes) OR :sizes IS NULL OR :sizes = '') ",
            nativeQuery = true)
    Page<Product> searchProducts(
            @Param("start") int start,
            @Param("end") int end,
            @Param("manufacturer") String manufacturer,
            @Param("idCategory") String idCategory,
            @Param("colors") String colors, // Là chuỗi
            @Param("sizes") String sizes,
            Pageable pageable);

    @Query(
            value = "SELECT DISTINCT c.name FROM product p INNER JOIN category c ON p.id_category = c.id_category",
            nativeQuery = true)
    List<String> getAllCategory();

    @Query(value = "SELECT DISTINCT manufacturer from product", nativeQuery = true)
    List<String> getAllManufacturer();

    List<Product> findAllByCategory_IdCategory(Integer idCategory);

}
