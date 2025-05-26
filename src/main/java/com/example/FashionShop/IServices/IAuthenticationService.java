package com.example.FashionShop.IServices;

import java.text.ParseException;

import com.example.FashionShop.Dto.request.AuthenticationRequest;
import com.example.FashionShop.Dto.request.EmailSenderRequest;
import com.example.FashionShop.Dto.request.IntrospectRequest;
import com.example.FashionShop.Dto.request.LogoutRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.AuthenticationResponse;
import com.example.FashionShop.Dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface IAuthenticationService {
    void logout(LogoutRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public String genToken(String username);

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    AuthenticationResponse refreshToken(IntrospectRequest request) throws ParseException, JOSEException;

    ApiResponse forgotPassword(EmailSenderRequest request);

}
