package com.example.FashionShop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Integer>, JpaSpecificationExecutor<SalesOrder> {
    @Query(value = "SELECT * FROM sales_order WHERE id_user = :idUser", nativeQuery = true)
    List<SalesOrder> findByIdUser(@Param("idUser") String idUser);

    @Query(value = "select DIStinct status from sales_order", nativeQuery = true)
    List<String> findAllStatus();

    @Query(value = "select DIStinct payment_method from sales_order", nativeQuery = true)
    List<String> findAllPaymentMethod();

    @Query(value = "SELECT * FROM sales_order WHERE id_user = :userId ORDER BY time_created DESC LIMIT 1", nativeQuery = true)
    SalesOrder findLatestByUserId(@Param("userId") Integer userId);
}
