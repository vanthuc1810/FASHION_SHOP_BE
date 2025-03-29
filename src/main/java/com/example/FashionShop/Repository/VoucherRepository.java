package com.example.FashionShop.Repository;

import com.example.FashionShop.Entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> , JpaSpecificationExecutor<Voucher> {
}
