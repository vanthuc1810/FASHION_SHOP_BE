package com.example.FashionShop.Services;

import java.util.List;

import com.example.FashionShop.IServices.IShippingAddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressService implements IShippingAddressService {
    ShippingAddressRepository shippingAddressRepository;
    UserRepository userRepository;

    @Override
    public ApiResponse createShippingAddres(ShippingAddressCreationRequest request) {
        // GET INFOR USER
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        ShippingAddress shippingAddress = new ShippingAddress()
                .builder()
                .address(request.getAddress())
                .user(user)
                .build();
        shippingAddressRepository.save(shippingAddress);
        return new ApiResponse().builder().results(shippingAddress).build();
    }
    @Override
    public ApiResponse updateShippingAddress(ShippingAddressUpdateRequest request, Integer idShippingAddress) {
        ShippingAddress shippingAddress = shippingAddressRepository
                .findById(idShippingAddress)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOTFOUND));
        shippingAddress.setAddress(request.getAddress());

        shippingAddress.setDefaultAddress(request.isDefaultAddress());
        shippingAddressRepository.save(shippingAddress);
        return new ApiResponse().builder().results(shippingAddress).build();
    }
    @Override
    public ApiResponse deleteShippingAddress(Integer idShippingAddress) {
        shippingAddressRepository.deleteById(idShippingAddress);
        return new ApiResponse<>().builder().message("Xóa địa chỉ thành công!").build();
    }
    @Override
    public ApiResponse getAllShippingAddress() {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        List<ShippingAddress> listShippingAddress = shippingAddressRepository.findAllByIdUser(idUser);
        return new ApiResponse().builder().results(listShippingAddress).build();
    }
    @Override
    public ApiResponse getShippingAddressById(Integer idShippingAddress) {
        ShippingAddress shippingAddress = shippingAddressRepository
                .findById(idShippingAddress)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOTFOUND));
        return new ApiResponse().builder().results(shippingAddress).build();
    }
}
