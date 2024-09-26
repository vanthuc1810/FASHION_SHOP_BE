package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.AuthenticationRequest;
import com.example.FashionShop.Dto.request.IntrospectRequest;
import com.example.FashionShop.Dto.response.AuthenticationResponse;
import com.example.FashionShop.Dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public String genToken(String username);
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

}
