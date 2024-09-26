package com.example.FashionShop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String idCard;
    double totalPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "card" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<CardItem> cardItems = new ArrayList<>();

    @OneToOne(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SalesOrder salesOrder;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_user")
    User user;
}
