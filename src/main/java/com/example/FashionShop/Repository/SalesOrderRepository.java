package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
    @Query(value = "SELECT * FROM sales_order WHERE id_user = :idUser", nativeQuery = true)
    List<SalesOrder> findByIdUser(@Param("idUser") String idUser);
}
