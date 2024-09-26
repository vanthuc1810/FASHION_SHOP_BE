package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.ShippingAddressCreationRequest;
import com.example.FashionShop.Dto.request.ShippingAddressUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ShippingAddressService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shipping-address")
public class ShippingAddressController {
    ShippingAddressService shippingAddressService;


    @PostMapping("/create")
    public ApiResponse createShippingAddress(@RequestBody ShippingAddressCreationRequest request)
    {
        return shippingAddressService.createShippingAddres(request);
    }

    @PutMapping("/update/{idShippingAddress}")
    public ApiResponse updateShippingAddress(@RequestBody ShippingAddressUpdateRequest request, @PathVariable("idShippingAddress") String idShippingAddress)
    {
        return shippingAddressService.updateShippingAddress(request, idShippingAddress);
    }
    @DeleteMapping("/delete/{idShippingAddress}")
    public ApiResponse deleteShippingAddress(@PathVariable("idShippingAddress") String idShippingAddress)
    {
        return shippingAddressService.deleteShippingAddress(idShippingAddress);
    }

    @GetMapping()
    public ApiResponse getALlShippingAddress()
    {
        return shippingAddressService.getAllShippingAddress();
    }
    @GetMapping("/{idShippingAddress}")
    public ApiResponse getShippingAddressById(@PathVariable("idShippingAddress") String idShippingAddress)
    {
        return shippingAddressService.getShippingAddressById(idShippingAddress);
    }
}
