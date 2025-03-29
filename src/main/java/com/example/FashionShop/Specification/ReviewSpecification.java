package com.example.FashionShop.Specification;

import com.example.FashionShop.Entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ReviewSpecification {
    public static Specification<Review> hasId(Integer idReview) {
        return (root, query, criteriaBuilder) ->
        {
            if(idReview == null)
            {
                return criteriaBuilder.conjunction();
            }else {
                return criteriaBuilder.equal(root.get("idReview"), idReview);
            }
        };
    }

    public static Specification<Review> hasIdProduct(Integer idProduct) {
        return (root, query, criteriaBuilder) -> {
            // Kiểm tra null hoặc rỗng
            if (idProduct == null) {
                return criteriaBuilder.conjunction(); // Không thêm điều kiện
            } else {
                // Join bảng
                Join<Review, Product> productJoin = root.join("product", JoinType.INNER);
                // Tạo điều kiện
                Predicate hasIdProduct = criteriaBuilder.equal(productJoin.get("idProduct"), idProduct);
                return hasIdProduct;
            }
        };
    }
    public static Specification<Review> hasIdUser(Integer idUser){
        return (root, query, criteriaBuilder) -> {
          if(idUser == null)
          {
              return criteriaBuilder.conjunction();
          }else {
              Join<Review, User> userJoin = root.join("user", JoinType.INNER);
              // Tao dieu kien
              Predicate hasIdUser = criteriaBuilder.equal(userJoin.get("idUser"), idUser);
              return hasIdUser;
          }
        };
    }
}
