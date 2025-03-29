package com.example.FashionShop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserName(String username);

    Optional<User> findByUserName(String userName);

    List<User> findAllByRole(String role);
}
