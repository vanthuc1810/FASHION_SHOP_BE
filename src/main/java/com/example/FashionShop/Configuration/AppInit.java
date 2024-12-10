package com.example.FashionShop.Configuration;

import com.example.FashionShop.Entity.Size;
import com.example.FashionShop.Repository.SizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Repository.UserRepository;
import vn.payos.PayOS;

@Configuration
public class AppInit {
    private static final Logger log = LoggerFactory.getLogger(AppInit.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                User user = new User()
                        .builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("123Vs456@"))
                        .role("ADMIN")
                        .build();
                userRepository.save(user);
                log.warn("Admin has created");
            }
            if (sizeRepository.findById("Small").isEmpty()) {
                Size size = new Size()
                        .builder()
                        .nameSize("Small")
                        .build();
                sizeRepository.save(size);
                log.warn("Small size has created");
            }
            if (sizeRepository.findById("Medium").isEmpty()) {
                Size size = new Size()
                        .builder()
                        .nameSize("Medium")
                        .build();
                sizeRepository.save(size);
                log.warn("Medium size has created");
            }
            if (sizeRepository.findById("Large").isEmpty()) {
                Size size = new Size()
                        .builder()
                        .nameSize("Large")
                        .build();
                sizeRepository.save(size);
                log.warn("Large size has created");
            }
        };
    }



}
