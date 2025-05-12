package com.example.FashionShop.Specification;

import com.example.FashionShop.Entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class SaleOrderSpecification {
    public static Specification<SalesOrder> hasTimeBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("timeFinished"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("timeFinished"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("timeFinished"), end);
            }
            return criteriaBuilder.conjunction(); // Không có điều kiện
        };
    }
    public static Specification<SalesOrder> hasManufracturer(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return an empty conjunction if name is null or empty
            } else {
                // Join to relevant tables
                Join<SalesOrder, Card> cardJoin = root.join("card", JoinType.INNER);
                Join<Card, CardItem> cardItemJoin = cardJoin.join("cardItems", JoinType.INNER);
                Join<CardItem, Product> productJoin = cardItemJoin.join("product", JoinType.INNER);

                // Create the condition
                Predicate hasManufacturer = criteriaBuilder.equal(productJoin.get("manufacturer"), name);
                return hasManufacturer;
            }
        };
    }
    public static Specification<SalesOrder> hasIdCategory(Integer idCategory) {
        return (root, query, criteriaBuilder) -> {
            if (idCategory == null) {
                return criteriaBuilder.conjunction(); // Return an empty conjunction if name is null or empty
            } else {
                // Join to relevant tables
                Join<SalesOrder, Card> cardJoin = root.join("card", JoinType.INNER);
                Join<Card, CardItem> cardItemJoin = cardJoin.join("cardItems", JoinType.INNER);
                Join<CardItem, Product> productJoin = cardItemJoin.join("product", JoinType.INNER);
                Join<Product, Category> categoryJoin = productJoin.join("category", JoinType.INNER);


                // Create the condition
                Predicate hasIdCategory = criteriaBuilder.equal(categoryJoin.get("idCategory"), idCategory);
                return hasIdCategory;
            }
        };
    }
    public static Specification<SalesOrder> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return an empty conjunction if name is null or empty
            } else {
                // Create the condition
                Predicate hasStatus = criteriaBuilder.equal(root.get("status"), status);
                return hasStatus;
            }
        };
    }
    public static Specification<SalesOrder> hasIdUser(Integer idUser) {
        return (root, query, criteriaBuilder) -> {
            if (idUser == null) {
                return criteriaBuilder.conjunction(); // Return an empty conjunction if name is null or empty
            } else {
                // Join table
                Join<User, SalesOrder> userJoin = root.join("user", JoinType.INNER);
                // Create the condition
                Predicate hasIdUser = criteriaBuilder.equal(userJoin.get("idUser"), idUser);
                return hasIdUser;
            }
        };
    }

    public static Specification<SalesOrder> hasStatusList(List<String> status) {
        return (root, query, criteriaBuilder) ->
        {
            if(status.size() == 0)
            {
                return criteriaBuilder.conjunction();
            }else
            {
                Predicate hasStatusList = root.get("status").in(status);
                return hasStatusList;
            }
        };
    }

    public static Specification<SalesOrder> hasPaymentMethodList(List<String> paymentMethods) {
        return (root, query, criteriaBuilder) ->
        {
            if(paymentMethods.size() == 0)
            {
                return criteriaBuilder.conjunction();
            }else
            {
                Predicate hasPaymentMethodList = root.get("paymentMethod").in(paymentMethods);
                return hasPaymentMethodList;
            }
        };
    }

    public static Specification<SalesOrder> hasMinTotalAmount(Double minAmount) {
        return (root, query, criteriaBuilder) -> {
            if(minAmount == null)
            {
                return criteriaBuilder.conjunction();
            }else
            {
                Join<SalesOrder, Card> cardJoin = root.join("card", JoinType.INNER);
                return criteriaBuilder.greaterThanOrEqualTo(cardJoin.get("totalPrice"), minAmount);
            }
        };
    }


    public static Specification<SalesOrder> hasMaxTotalAmount(Double maxAmount) {
        return (root, query, criteriaBuilder) -> {
            System.out.println("Max amount = " + maxAmount);
            if(maxAmount == null)
            {
                return criteriaBuilder.conjunction();
            }else
            {
                Join<SalesOrder, Card> cardJoin = root.join("card", JoinType.INNER);
                return criteriaBuilder.lessThanOrEqualTo(cardJoin.get("totalPrice"), maxAmount);
            }
        };
    }

}
