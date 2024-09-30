package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.response.UserResponse;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IUserSerive;
import com.example.FashionShop.Mapper.UserMapper;
import com.example.FashionShop.Dto.request.UserCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Builder

public class UserService implements IUserSerive {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse<>().builder().results(users).build();
    }

    @Override
    public ApiResponse getUserById(String idUser) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        return new ApiResponse().builder().results(user).build();
    }

    @Override
    @PostAuthorize("returnObject.results.idUser == authentication.name")
    public ApiResponse getInfor() {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponse userResponse = userMapper.toUserResponse(user);
        return new ApiResponse().builder().results(userResponse).build();
    }

    @Override
    public ApiResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        userRepository.save(user);
        return new ApiResponse<>().builder().results(user).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateUser(String idUser, UpdateUserRequest request) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        user = userMapper.updateUser(user, request);
        userRepository.save(user);
        return new ApiResponse().builder().results(user).build();
    }
}
