package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.TopUpWalletRequest;
import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.request.UserCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import vn.payos.type.CheckoutResponseData;

public interface IUserSerive {
    public ApiResponse getUsers();

    public ApiResponse getUserById(Integer idUser);

    public ApiResponse getInfor();

    public ApiResponse createUser(UserCreationRequest request);

    public ApiResponse updateUser(UpdateUserRequest request);

    public CheckoutResponseData topUpWallet(TopUpWalletRequest request) throws Exception;

    void setAvaiable();
}
