package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.CancleSaleOrdersRequest;
import com.example.FashionShop.Dto.request.CompleteSaleOrdersRequest;
import com.example.FashionShop.Dto.request.FilterOrderRequest;
import com.example.FashionShop.Dto.response.PageableResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.SalesOrderCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Services.SalesOrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/sales-order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesOrderController {
    SalesOrderService salesOrderService;

    @PostMapping("/create")
    public ApiResponse createSalesOrder(@RequestBody @Valid SalesOrderCreationRequest request) {
        return salesOrderService.createSalesOrder(request);
    }

    @GetMapping()
    public PageableResponse getAllSalesOrder(
            @RequestParam(required = false) Integer idUser,
            @RequestParam(required = false) Integer idProduct,
            @RequestParam(required = false) Integer idShippingAddress,
            Pageable pageable) {

        return salesOrderService.getAllSalesOrder(pageable);
    }


    @GetMapping("/status")
    public ApiResponse getStatus() {
        return salesOrderService.getStatus();
    }

    @GetMapping("/paymentMethod")
    public ApiResponse getPaymentMethod() {
        return salesOrderService.getPaymentMethod();
    }

    @PostMapping("/admin/getAll")
    public PageableResponse getAllSalesOrder(Pageable pageable, @RequestBody FilterOrderRequest request) {
        return salesOrderService.getSaleOrders(pageable, request);
    }

    @GetMapping("/{idSalesOrder}")
    public SaleOrderResponse getSalesOrderById(@PathVariable("idSalesOrder") Integer idSalesOrder) {
        return salesOrderService.getSalesOrderById(idSalesOrder);
    }

    @PutMapping("/checkout/{idSalesOrder}")
    public SaleOrderResponse checkout(@PathVariable("idSalesOrder") Integer idSalesOrder)
    {
        return salesOrderService.checkout(idSalesOrder);
    }

    @PutMapping("/complete/{idSalesOrder}")
    public SaleOrderResponse completeSaleOrder(@PathVariable("idSalesOrder") Integer idSalesOrder)
    {
        return salesOrderService.completeSaleOrder(idSalesOrder);
    }

    @PutMapping("/complete")
    public ApiResponse completeSaleOrderList(@RequestBody CompleteSaleOrdersRequest request)
    {
        return salesOrderService.completeSaleOrderList(request);
    }

    @PutMapping("/cancle")
    public ApiResponse cancleSaleOrderList(@RequestBody CancleSaleOrdersRequest request)
    {
        return salesOrderService.cancleSaleOrderList(request);
    }

    @PutMapping("/cancle/{idSalesOrder}")
    public SaleOrderResponse cancleSaleOrder(@PathVariable("idSalesOrder") Integer idSalesOrder)
    {
        return salesOrderService.cancleOrder(idSalesOrder);
    }
}
