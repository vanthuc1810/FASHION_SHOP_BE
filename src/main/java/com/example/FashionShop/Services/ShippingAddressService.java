package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.ShippingAddressCreationRequest;
import com.example.FashionShop.Dto.request.ShippingAddressUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.ShippingAddress;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.ShippingAddressRepository;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ShippingAddressService {
    ShippingAddressRepository shippingAddressRepository;
    UserRepository userRepository;
    public ApiResponse createShippingAddres(ShippingAddressCreationRequest request)
    {
        // GET INFOR USER
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        ShippingAddress shippingAddress = new ShippingAddress()
                .builder()
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .country(request.getCountry())
                .user(user)
                .build();
        shippingAddressRepository.save(shippingAddress);
        return new ApiResponse()
                .builder()
                .results(shippingAddress)
                .build();
    }
    public ApiResponse updateShippingAddress(ShippingAddressUpdateRequest request, String idShippingAddress)
    {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(idShippingAddress).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOTFOUND));
        shippingAddress.setAddress(request.getAddress());
        shippingAddress.setCity(request.getCity());
        shippingAddress.setState(request.getState());
        shippingAddress.setCountry(request.getCountry());
        shippingAddress.setZipcode(request.getZipcode());
        shippingAddress.setDefaultAddress(request.isDefaultAddress());
        shippingAddressRepository.save(shippingAddress);
        return new ApiResponse()
                .builder()
                .results(shippingAddress)
                .build();
    }
    public ApiResponse deleteShippingAddress(String idShippingAddress)
    {
        shippingAddressRepository.deleteById(idShippingAddress);
        return new ApiResponse<>()
                .builder()
                .message("Xóa địa chỉ thành công!")
                .build();
    }
    public ApiResponse getAllShippingAddress()
    {
        List<ShippingAddress> listShippingAddress = shippingAddressRepository.findAll();
        return new ApiResponse().builder().results(listShippingAddress).build();
    }
    public ApiResponse getShippingAddressById(String idShippingAddress)
    {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(idShippingAddress).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOTFOUND));
        return new ApiResponse().builder().results(shippingAddress).build();
    }
}
