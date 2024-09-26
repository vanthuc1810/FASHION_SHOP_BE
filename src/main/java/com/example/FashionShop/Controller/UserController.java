package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.request.UserCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.UserService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//MakeFinal = true vi @RequireArgsContructor se tu dong inject cac bean final
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @GetMapping("/getUsers")
    public ApiResponse getUsers(){
        return new ApiResponse().builder()
                .results(userService.getUsers())
                .build();
    }
    @GetMapping("/getUser/{idUser}")
    public ApiResponse getUserById(@PathVariable("idUser") String idUser)
    {
        return userService.getUserById(idUser);
    }
    @GetMapping("/getInfor")
    public ApiResponse getMyInfor()
    {
        return userService.getInfor();
    }

    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request)
    {
        return userService.createUser(request);
    }

    @PutMapping("/updateUser/{idUser}")
    public ApiResponse updateUser(@PathVariable("idUser") String idUser, @RequestBody UpdateUserRequest request)
    {
        return userService.updateUser(idUser, request);
    }

}
