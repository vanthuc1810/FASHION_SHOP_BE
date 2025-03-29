package com.example.FashionShop.Controller;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import com.example.FashionShop.Dto.request.RefreshRequest;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.AuthenticationRequest;
import com.example.FashionShop.Dto.request.IntrospectRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.AuthenticationResponse;
import com.example.FashionShop.Dto.response.IntrospectResponse;
import com.example.FashionShop.Services.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) throws AccessDeniedException {
        var result = authenticationService.authenticate(authenticationRequest);
        ApiResponse apiReponse = new ApiResponse();
        apiReponse.setResults(result);
        return apiReponse;
    }
    @PostMapping("logout")
    public void logout()
    {
        authenticationService.logout();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> instrospect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        ApiResponse apiReponse = new ApiResponse();
        apiReponse.setResults(result);
        return apiReponse;
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshToken(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return authenticationService.refreshToken(request);
    }

}
