package com.example.FashionShop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.FashionShop.Entity.User;

import javax.swing.text.html.Option;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {
    boolean existsByUserName(String username);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(String role);

    @Query(value = "SELECT DISTINCT role from user", nativeQuery = true)
    List<String> getRoles();

    boolean existsByEmail(String email);
}
