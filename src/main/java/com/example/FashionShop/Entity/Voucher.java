package com.example.FashionShop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idVoucher;
    @Column(unique = true)
    String code;
    Float discountPercentage;
    Float discountAmount;
    Float minOrderValue;
    LocalDate expirationDate;
    LocalDate startDate;
    boolean isActive = false;

    @OneToMany(mappedBy = "voucher")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<SalesOrder> salesOrders = new ArrayList<>();
}
