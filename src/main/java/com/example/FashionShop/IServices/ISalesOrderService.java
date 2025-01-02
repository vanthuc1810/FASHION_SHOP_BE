package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.SalesOrderCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PostAuthorize;

import java.time.LocalDateTime;
import java.util.List;

public interface ISalesOrderService {
    ApiResponse<SaleOrderResponse> createSalesOrder(SalesOrderCreationRequest request);

    ApiResponse getAllSalesOrder();

    @PostAuthorize("returnObject.idUser.toString() == authentication.name")
    SaleOrderResponse getSalesOrderById(Integer idSalesOrder);

    @Transactional
    SaleOrderResponse cancleOrder(Integer idSalesOrder);

    @PostAuthorize("returnObject.idUser.toString() == authentication.name")
    SaleOrderResponse completeSaleOrder(Integer idSalesOrder);

    List<SaleOrderResponse> getSaleOrdersBySpec(LocalDateTime start, LocalDateTime end, String name, Integer idCategory, String status);

    List<SaleOrderResponse> getSaleOrdersByManufracturer(String name);

    List<SaleOrderResponse> getSaleOrdersByIdCategory(Integer idCategory);
}
