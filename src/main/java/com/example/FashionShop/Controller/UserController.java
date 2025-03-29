package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.TopUpWalletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.request.UserCreationRequest;
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
    UserService userService;

    @GetMapping("/getUsers")
    public ApiResponse getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/getUser/{idUser}")
    public ApiResponse getUserById(@PathVariable("idUser") Integer idUser) {
        return userService.getUserById(idUser);
    }

    @GetMapping("/getInfor")
    public ApiResponse getMyInfor() {
        return userService.getInfor();
    }

    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/updateUser")
    public ApiResponse updateUser(@RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @PutMapping("/topUpWallet")
    public CheckoutResponseData topUpWallet(@RequestBody @Valid TopUpWalletRequest request) throws Exception {
        return userService.topUpWallet(request);
    }

    @PutMapping("/setAvaiable")
    public void setAvaiable() throws Exception {
        userService.setAvaiable();
    }
}
