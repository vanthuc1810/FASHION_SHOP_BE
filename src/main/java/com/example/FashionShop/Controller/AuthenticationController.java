package com.example.FashionShop.Controller;

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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationService.authenticate(authenticationRequest);
        ApiResponse apiReponse = new ApiResponse();
        apiReponse.setResults(result);
        return apiReponse;
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> instrospect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        ApiResponse apiReponse = new ApiResponse();
        apiReponse.setResults(result);
        return apiReponse;
    }
}
