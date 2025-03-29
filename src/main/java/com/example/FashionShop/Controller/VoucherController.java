package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.VoucherCreationRequest;
import com.example.FashionShop.Dto.request.VoucherUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.VoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/voucher")
public class VoucherController {
    VoucherService voucherService;
    @PostMapping("/create")
    public ApiResponse createVoucher(@RequestBody @Valid VoucherCreationRequest request){
        return voucherService.create(request);
    }

    @PutMapping("/disable/{idVoucher}")
    public ApiResponse disableVoucher(@PathVariable("idVoucher") Integer idVoucher){
        return voucherService.disableVoucher(idVoucher);
    }
    @PutMapping("/update/{idVoucher}")
    public ApiResponse updateVoucher(@RequestBody @Valid VoucherUpdateRequest request, @PathVariable("idVoucher") Integer idVoucher){
        return voucherService.updateVoucher(request, idVoucher);
    }
}
