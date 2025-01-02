package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ShippingAddressCreationRequest;
import com.example.FashionShop.Dto.request.ShippingAddressUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;

public interface IShippingAddressService {
    ApiResponse createShippingAddres(ShippingAddressCreationRequest request);

    ApiResponse updateShippingAddress(ShippingAddressUpdateRequest request, Integer idShippingAddress);

    ApiResponse deleteShippingAddress(Integer idShippingAddress);

    ApiResponse getAllShippingAddress();

    ApiResponse getShippingAddressById(Integer idShippingAddress);
}
