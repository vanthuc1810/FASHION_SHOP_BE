package com.example.FashionShop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.ShippingAddress;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {
    @Query(
            value = "SELECT * FROM shipping_address WHERE id_user =  :idUser",
            nativeQuery = true)
    List<ShippingAddress> findAllByIdUser(@Param("idUser") String idUser);
}
