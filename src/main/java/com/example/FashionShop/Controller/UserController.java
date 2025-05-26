package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.IServices.IUserSerive;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.UserService;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.payos.type.CheckoutResponseData;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// MakeFinal = true vi @RequireArgsContructor se tu dong inject cac bean final
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    IUserSerive userService;

    @GetMapping("/getUsers")
    public PageableResponse getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/getUser/{idUser}")
    public ApiResponse getUserById(@PathVariable("idUser") Integer idUser) {
        return userService.getUserById(idUser);
    }

    @GetMapping("/roles")
    public ApiResponse getRoles() {
        return userService.getRoles();
    }
    @GetMapping("/getInfor")
    public ApiResponse getMyInfor() {
        return userService.getInfor();
    }

    @PostMapping("/filter")
    public PageableResponse filterUser(@RequestParam String query, @RequestBody FilterUserRequest request, Pageable pageable)
    {
        return userService.filterUser(query, request, pageable);
    }

    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/updateUser")
    public ApiResponse updateUser(@RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @PutMapping("/updateRole")
    public ApiResponse updateRole(@RequestBody @Valid UpdateRoleRequest request) {
        return userService.updateRole(request);
    }

    @PutMapping("/topUpWallet")
    public CheckoutResponseData topUpWallet(@RequestBody @Valid TopUpWalletRequest request) throws Exception {
        return userService.topUpWallet(request);
    }

    @PutMapping("/setAvaiable")
    public void setAvaiable() throws Exception {
        userService.setAvaiable();
    }

    @PutMapping("/delete")
    public ApiResponse delete(@RequestBody DeleteUserRequest request) throws Exception {
        return userService.delete(request);
    }

    @PutMapping("/active")
    public ApiResponse delete(@RequestBody ActiveUserRequest request) throws Exception {
        return userService.active(request);
    }
}
