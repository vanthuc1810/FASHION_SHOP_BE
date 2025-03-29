package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.request.VoucherCreationRequest;
import com.example.FashionShop.Dto.request.VoucherUpdateRequest;
import com.example.FashionShop.Dto.response.VoucherResponse;
import com.example.FashionShop.Entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    @Mapping(source = "request.discountPercentage", target = "discountPercentage", defaultValue = "0.0f")
    @Mapping(source = "request.discountAmount", target = "discountAmount", defaultValue = "0.0f")
    @Mapping(source = "request.minOrderValue", target = "minOrderValue", defaultValue = "0.0f")
    @Mapping(source = "request.startDate", target = "startDate")
    @Mapping(source = "request.expirationDate", target = "expirationDate")
    Voucher toVoucher(VoucherCreationRequest request);

    @Mapping(expression = "java(voucher.isActive())", target = "isActive")
    VoucherResponse toVoucherResponse(Voucher voucher);

    @Mapping(target = "discountPercentage", expression = "java(updateFieldIfNotNull(request.getDiscountPercentage(), voucher.getDiscountPercentage()))")
    @Mapping(target = "discountAmount", expression = "java(updateFieldIfNotNull(request.getDiscountAmount(), voucher.getDiscountAmount()))")
    @Mapping(target = "minOrderValue", expression = "java(updateFieldIfNotNull(request.getMinOrderValue(), voucher.getMinOrderValue()))")
    @Mapping(target = "expirationDate", expression = "java(updateFieldIfNotNull(request.getExpirationDate(), voucher.getExpirationDate()))")
    @Mapping(target = "startDate", expression = "java(updateFieldIfNotNull(request.getStartDate(), voucher.getStartDate()))")
    Voucher updateVoucher(@MappingTarget Voucher voucher, VoucherUpdateRequest request);

    default <T> T updateFieldIfNotNull(T newValue, T oldValue) {
        return (newValue == "" || newValue == null) ? oldValue : newValue;
    }
}
