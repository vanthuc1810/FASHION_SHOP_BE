package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.VoucherCreationRequest;
import com.example.FashionShop.Dto.request.VoucherUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IVoucherService {
    ApiResponse create(VoucherCreationRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse disableVoucher(Integer idVoucher);

    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse updateVoucher(VoucherUpdateRequest request, Integer idVoucher);

    void disableVoucher();

    @Scheduled(cron = "0 * * * * *")
    void enableVoucher();
}
