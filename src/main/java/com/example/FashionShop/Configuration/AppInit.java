package com.example.FashionShop.Configuration;

import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppInit {
    private static final Logger log = LoggerFactory.getLogger(AppInit.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository)
    {
        return args ->
        {
            if(userRepository.findByUserName("admin").isEmpty()){
                User user = new User()
                        .builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("123Vs456@"))
                        .role("ADMIN")
                        .build();
                userRepository.save(user);
                log.warn("Admin has created");
            }
        };
    }
}
