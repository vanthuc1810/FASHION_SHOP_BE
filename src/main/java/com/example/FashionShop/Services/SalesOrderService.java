package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.SalesOrderCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Entity.SalesOrder;
import com.example.FashionShop.Entity.ShippingAddress;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Enum.PaymentMethod;
import com.example.FashionShop.Enum.SalesOrderStatus;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.CardRepository;
import com.example.FashionShop.Repository.SalesOrderRepository;
import com.example.FashionShop.Repository.ShippingAddressRepository;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesOrderService {
    SalesOrderRepository salesOrderRepository;
    CardRepository cardRepository;
    ShippingAddressRepository shippingAddressRepository;
    UserRepository userRepository;

    public ApiResponse<SaleOrderResponse> createSalesOrder(SalesOrderCreationRequest request)
    {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        Card card = cardRepository.findById(request.getIdCard()).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        ShippingAddress shippingAddress = shippingAddressRepository.findById(request.getIdShippingAddress()).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOTFOUND));

        SalesOrder salesOrder = new SalesOrder()
                .builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .card(card)
                .paymentMethod(request.getPaymentMethod())
                .status(SalesOrderStatus.CREATED.getMessage())
                .build();
        if(salesOrder.getPaymentMethod().equals(PaymentMethod.BANK_TRANSFER.getName()))
        {
            salesOrder.setStatus(SalesOrderStatus.PENDING_PAYMENT.getMessage());
        } else if (salesOrder.getPaymentMethod().equals(PaymentMethod.CASH.getName())) {
            salesOrder.setStatus(SalesOrderStatus.CREATED.getMessage());
        } else if (salesOrder.getPaymentMethod().equals(PaymentMethod.WALLET.getName())) {
            salesOrder.setStatus(SalesOrderStatus.CREATED.getMessage());
        }
        salesOrderRepository.save(salesOrder);
        return ApiResponse.<SaleOrderResponse>builder()
                .results(new SaleOrderResponse()
                        .builder()
                        .idCard(salesOrder.getCard().getIdCard())
                        .idSalesOrder(salesOrder.getIdSalesOrder())
                        .idUser(salesOrder.getUser().getIdUser())
                        .idShippingAddress(salesOrder.getShippingAddress().getIdShippingAddress())
                        .status(salesOrder.getStatus())
                        .build())
                .build();
    }

    public ApiResponse getAllSalesOrder()
    {
        var contex = SecurityContextHolder.getContext();
        String idUser = contex.getAuthentication().getName();
        List<SalesOrder> listSalesOrder = salesOrderRepository.findByIdUser(idUser);
        return new ApiResponse()
                .builder()
                .results(listSalesOrder)
                .build();
    }

    @PostAuthorize("returnObject.idUser == authentication.name")
    public SaleOrderResponse getSalesOrderById(String idSalesOrder)
    {
        SalesOrder salesOrder = salesOrderRepository.findById(idSalesOrder).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        SaleOrderResponse saleOrderResponse = new SaleOrderResponse()
                .builder()
                .idCard(salesOrder.getCard().getIdCard())
                .idSalesOrder(salesOrder.getIdSalesOrder())
                .idShippingAddress(salesOrder.getShippingAddress().getIdShippingAddress())
                .idUser(salesOrder.getUser().getIdUser())
                .status(salesOrder.getStatus())
                .build();
        return saleOrderResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public SaleOrderResponse confirmSaleOrder(String idSalesOrder)
    {
        SaleOrderResponse saleOrderResponse = getSalesOrderById(idSalesOrder);
        SalesOrder salesOrder = salesOrderRepository.findById(saleOrderResponse.getIdSalesOrder()).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        if(salesOrder.getStatus().equals(SalesOrderStatus.CREATED.getMessage()))
        {
            salesOrder.setStatus(SalesOrderStatus.CONFIRM.getMessage());
            salesOrderRepository.save(salesOrder);
        }
        else throw new AppException(ErrorCode.COMFIRM_FAILD);
        return saleOrderResponse;
    }

    public SaleOrderResponse confirmPickUp(String idSalesOrder)
    {
        SaleOrderResponse saleOrderResponse = getSalesOrderById(idSalesOrder);
        SalesOrder salesOrder = salesOrderRepository.findById(saleOrderResponse.getIdSalesOrder()).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        if(salesOrder.getStatus().equals(SalesOrderStatus.CONFIRM.getMessage()))
        {
            salesOrder.setStatus(SalesOrderStatus.COMPLETE.getMessage());
            salesOrderRepository.save(salesOrder);
        }
        return saleOrderResponse;
    }

    public SaleOrderResponse cancleOrder(String idSalesOrder)
    {
        SaleOrderResponse saleOrderResponse = getSalesOrderById(idSalesOrder);
        SalesOrder salesOrder = salesOrderRepository.findById(saleOrderResponse.getIdSalesOrder()).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        if(salesOrder.getStatus().equals(SalesOrderStatus.CONFIRM.getMessage()))
        {
            salesOrder.setStatus(SalesOrderStatus.CANCLE.getMessage());
            salesOrderRepository.save(salesOrder);
        }
        return saleOrderResponse;
    }

}
