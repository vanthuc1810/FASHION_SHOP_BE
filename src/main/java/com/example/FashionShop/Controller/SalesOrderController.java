package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.SalesOrderCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Services.SalesOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales-order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesOrderController {
    SalesOrderService salesOrderService;

    @PostMapping("/create")
    public ApiResponse createSalesOrder(@RequestBody SalesOrderCreationRequest request)
    {
        return salesOrderService.createSalesOrder(request);
    }

    @GetMapping()
    public ApiResponse getAllSalesOrder()
    {
        return salesOrderService.getAllSalesOrder();
    }

    @GetMapping("/{idSalesOrder}")
    public SaleOrderResponse getSalesOrderById(@PathVariable("idSalesOrder") String idSalesOrder)
    {
        return salesOrderService.getSalesOrderById(idSalesOrder);
    }

    @PutMapping("/confirm-order/{idSalesOrder}")
    public SaleOrderResponse confirmSaleOrder(@PathVariable("idSalesOrder") String idSalesOrder)
    {
        return salesOrderService.confirmSaleOrder(idSalesOrder);
    }
    @PutMapping("/confirm-pickup/{idSalesOrder}")
    public SaleOrderResponse confirmPickUp(@PathVariable("idSalesOrder") String idSalesOrder)
    {
        return salesOrderService.confirmPickUp(idSalesOrder);
    }
    @PutMapping("/cancle/{idSalesOrder}")
    public SaleOrderResponse canclePickUp(@PathVariable("idSalesOrder") String idSalesOrder)
    {
        return salesOrderService.cancleOrder(idSalesOrder);
    }


}
