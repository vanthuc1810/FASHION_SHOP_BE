package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Entity.SalesOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleOrderMapper {
    @Mapping(source = "card.idCard", target = "idCard")
    @Mapping(source = "user.idUser", target = "idUser")
    @Mapping(source = "shippingAddress.idShippingAddress", target = "idShippingAddress")
    SaleOrderResponse toSaleOrderResponse(SalesOrder salesOrder);
}
