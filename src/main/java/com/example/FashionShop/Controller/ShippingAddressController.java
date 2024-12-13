package com.example.FashionShop.Controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.ShippingAddressCreationRequest;
import com.example.FashionShop.Dto.request.ShippingAddressUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ShippingAddressService;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shipping-address")
public class ShippingAddressController {
    ShippingAddressService shippingAddressService;

    @PostMapping("/create")
    public ApiResponse createShippingAddress(@RequestBody @Valid ShippingAddressCreationRequest request) {
        return shippingAddressService.createShippingAddres(request);
    }

    @PutMapping("/update/{idShippingAddress}")
    public ApiResponse updateShippingAddress(
            @RequestBody @Valid ShippingAddressUpdateRequest request,
            @PathVariable("idShippingAddress") Integer idShippingAddress) {
        return shippingAddressService.updateShippingAddress(request, idShippingAddress);
    }

    @DeleteMapping("/delete/{idShippingAddress}")
    public ApiResponse deleteShippingAddress(@PathVariable("idShippingAddress") Integer idShippingAddress) {
        return shippingAddressService.deleteShippingAddress(idShippingAddress);
    }

    @GetMapping()
    public ApiResponse getALlShippingAddress() {
        return shippingAddressService.getAllShippingAddress();
    }

    @GetMapping("/{idShippingAddress}")
    public ApiResponse getShippingAddressById(@PathVariable("idShippingAddress") Integer idShippingAddress) {
        return shippingAddressService.getShippingAddressById(idShippingAddress);
    }
}
