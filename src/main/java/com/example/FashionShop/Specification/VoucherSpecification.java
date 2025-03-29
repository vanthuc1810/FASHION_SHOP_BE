package com.example.FashionShop.Specification;

import com.example.FashionShop.Entity.Voucher;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VoucherSpecification {
    public static Specification<Voucher> hasExpirationDate() {
        return (root, query, criteriaBuilder) -> {
            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();
            // Trả về các bản ghi có expirationDate nhỏ hơn ngày hôm nay
            return criteriaBuilder.lessThan(root.get("expirationDate"), today);
        };
    }
    public static Specification<Voucher> hasStartDate(){
        return (root, query, criteriaBuilder) -> {
            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();
            // Trả về các bản ghi có expirationDate nhỏ hơn ngày hôm nay
            return criteriaBuilder.equal(root.get("startDate"), today);
        };
    }

    public static Specification<Voucher> hasInActive(){
        return (root, query, criteriaBuilder) -> {
            // Trả về các bản ghi có expirationDate nhỏ hơn ngày hôm nay
            return criteriaBuilder.equal(root.get("isActive"), false);
        };
    }
}
