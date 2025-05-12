package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.CancleSaleOrdersRequest;
import com.example.FashionShop.Dto.request.CompleteSaleOrdersRequest;
import com.example.FashionShop.Dto.request.FilterOrderRequest;
import com.example.FashionShop.Dto.request.SalesOrderCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;

import java.time.LocalDateTime;
import java.util.List;

public interface ISalesOrderService {
    ApiResponse<SaleOrderResponse> createSalesOrder(SalesOrderCreationRequest request);

    PageableResponse getAllSalesOrder(Pageable pageable);

    ApiResponse getStatus();


    ApiResponse getPaymentMethod();

    SaleOrderResponse checkout(Integer idOrder);

    ApiResponse completeSaleOrderList(CompleteSaleOrdersRequest request);

    ApiResponse cancleSaleOrderList(CancleSaleOrdersRequest request);

    @PostAuthorize("returnObject.idUser.toString() == authentication.name")
    SaleOrderResponse getSalesOrderById(Integer idSalesOrder);

    @Transactional
    SaleOrderResponse cancleOrder(Integer idSalesOrder);

    @PostAuthorize("returnObject.idUser.toString() == authentication.name")
    SaleOrderResponse completeSaleOrder(Integer idSalesOrder);

    List<SaleOrderResponse> getSaleOrdersBySpec(LocalDateTime start, LocalDateTime end, String name, Integer idCategory, String status, Integer idUser);

    List<SaleOrderResponse> getSaleOrdersByManufracturer(String name);

    List<SaleOrderResponse> getSaleOrdersByIdCategory(Integer idCategory);

    PageableResponse getSaleOrders (Pageable pageable, FilterOrderRequest request);
}
