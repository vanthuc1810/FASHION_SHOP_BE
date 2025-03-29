package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.VoucherCreationRequest;
import com.example.FashionShop.Dto.request.VoucherUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.VoucherResponse;
import com.example.FashionShop.Entity.Voucher;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IVoucherService;
import com.example.FashionShop.Mapper.VoucherMapper;
import com.example.FashionShop.Repository.VoucherRepository;
import com.example.FashionShop.Specification.VoucherSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService implements IVoucherService {
    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse create(VoucherCreationRequest request){
        Voucher voucher = voucherMapper.toVoucher(request);
        System.out.println(request.getExpirationDate());
        // Gen code
        String code = RandomStringUtils.randomAlphabetic(8).toUpperCase();
        voucher.setCode(code);
        // Save voucher
        voucherRepository.save(voucher);
        VoucherResponse voucherResponse = voucherMapper.toVoucherResponse(voucher);
        return ApiResponse
                .builder()
                .results(voucherResponse)
                .build();
    };

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse disableVoucher(Integer idVoucher){
        Voucher voucher = voucherRepository.findById(idVoucher).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOTFOUND));
        voucher.setActive(false);
        voucherRepository.save(voucher);

        VoucherResponse voucherResponse = voucherMapper.toVoucherResponse(voucher);
        return ApiResponse
                .builder()
                .results(voucherResponse)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateVoucher(VoucherUpdateRequest request, Integer idVoucher){
        Voucher voucher = voucherRepository.findById(idVoucher).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOTFOUND));
        Voucher voucherUpdate = voucherMapper.updateVoucher(voucher, request);
        voucherRepository.save(voucherUpdate);
        VoucherResponse voucherResponse = voucherMapper.toVoucherResponse(voucherUpdate);
        return ApiResponse
                .builder()
                .results(voucherResponse)
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void disableVoucher(){
        Specification<Voucher> getDisableVoucher = VoucherSpecification.hasExpirationDate();
        List<Voucher> listVoucher = voucherRepository.findAll(getDisableVoucher);
        for(Voucher voucher : listVoucher)
        {
            voucher.setActive(false);
            voucherRepository.save(voucher);
            System.out.println("Disabled Voucher");
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void enableVoucher(){
        Specification<Voucher> hasStartDate = VoucherSpecification.hasStartDate();
        Specification<Voucher> hasInActive = VoucherSpecification.hasInActive();
        Specification<Voucher> spec = hasStartDate.and(hasInActive);
        List<Voucher> listVoucher = voucherRepository.findAll(spec);
        for(Voucher voucher : listVoucher)
        {
            voucher.setActive(true);
            voucherRepository.save(voucher);
            System.out.println("Enable Voucher");
        }
    }
}
