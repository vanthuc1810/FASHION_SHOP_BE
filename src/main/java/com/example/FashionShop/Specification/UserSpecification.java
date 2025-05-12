package com.example.FashionShop.Specification;

import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecification {

    public static Specification<User> hasDeleted(List<Boolean> delete){
        return (root, query, criteriaBuilder) ->
        {
            if(delete.size() == 0)
            {
                return criteriaBuilder.conjunction();
            }else
            {
                Predicate hasDeleted = root.get("deleted").in(delete);
                return hasDeleted;
            }
        };
    }

    public static Specification<User> hasCurrentStatus(List<Boolean> currentStatus){
        return (root, query, criteriaBuilder) ->
        {
            if(currentStatus.size() == 0)
            {
                return criteriaBuilder.conjunction();
            }else {
                Predicate hasCurrentStatus = root.get("available").in(currentStatus);
                return hasCurrentStatus;
            }
        };
    }

    public static Specification<User> hasRole(List<String> roles) {
        return (root, query, criteriaBuilder) ->
        {
            if(roles.size() == 0)
            {
                return criteriaBuilder.conjunction();
            }else
            {
                Predicate hasRoles = root.get("role").in(roles);
                return hasRoles;
            }
        };
    }

    public static Specification<User> nameContains(String keyword) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
}
