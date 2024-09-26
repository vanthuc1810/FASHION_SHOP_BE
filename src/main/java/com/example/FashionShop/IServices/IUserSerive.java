package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.request.UserCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;

public interface IUserSerive {
    public ApiResponse getUsers();
    public ApiResponse getUserById(String idUser);
    public ApiResponse getInfor();
    public ApiResponse createUser(UserCreationRequest request);
    public ApiResponse updateUser(String idUser, UpdateUserRequest request);
}

