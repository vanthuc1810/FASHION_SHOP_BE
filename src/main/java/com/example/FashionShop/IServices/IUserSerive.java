package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import vn.payos.type.CheckoutResponseData;

public interface IUserSerive {
    public PageableResponse getUsers(Pageable pageable);

    public ApiResponse getUserById(Integer idUser);

    public ApiResponse getInfor();

    public ApiResponse createUser(UserCreationRequest request);

    public ApiResponse updateUser(UpdateUserRequest request);

    public ApiResponse updateRole(UpdateRoleRequest request);

    public ApiResponse getRoles();

    public PageableResponse filterUser(String query, FilterUserRequest request, Pageable pageable);

    public CheckoutResponseData topUpWallet(TopUpWalletRequest request) throws Exception;

    void setAvaiable();

    ApiResponse delete(DeleteUserRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse active(ActiveUserRequest request);

    ApiResponse updatePassword(UpdatePasswordRequest request);
}
