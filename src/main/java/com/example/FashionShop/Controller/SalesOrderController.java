package com.example.FashionShop.Controller;

import jakarta.validation.Valid;
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
    public ApiResponse getAllSalesOrder(
            @RequestParam(required = false) Integer idUser,
            @RequestParam(required = false) Integer idProduct,
            @RequestParam(required = false) Integer idShippingAddress) {
        return salesOrderService.getAllSalesOrder();
    }

    @GetMapping("/{idSalesOrder}")
    public SaleOrderResponse getSalesOrderById(@PathVariable("idSalesOrder") Integer idSalesOrder) {
        return salesOrderService.getSalesOrderById(idSalesOrder);
    }

    @PutMapping("/complete/{idSalesOrder}")
    public SaleOrderResponse completeSaleOrder(@PathVariable("idSalesOrder") Integer idSalesOrder)
    {
        return salesOrderService.completeSaleOrder(idSalesOrder);
    }

    @PutMapping("/cancle/{idSalesOrder}")
    public SaleOrderResponse cancleSaleOrder(@PathVariable("idSalesOrder") Integer idSalesOrder)
    {
        return salesOrderService.cancleOrder(idSalesOrder);
    }
}
