package com.example.FashionShop.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.FashionShop.IServices.ISalesOrderService;
import com.example.FashionShop.Mapper.SaleOrderMapper;
import com.example.FashionShop.Specification.SaleOrderSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.SalesOrderCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesOrderService implements ISalesOrderService {
    SalesOrderRepository salesOrderRepository;
    CardRepository cardRepository;
    ShippingAddressRepository shippingAddressRepository;
    UserRepository userRepository;
    SaleOrderMapper saleOrderMapper;

    @Override
    public ApiResponse<SaleOrderResponse> createSalesOrder(SalesOrderCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        Card card = cardRepository
                .findById(request.getIdCard())
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        ShippingAddress shippingAddress = shippingAddressRepository
                .findById(request.getIdShippingAddress())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOTFOUND));

        SalesOrder salesOrder = new SalesOrder()
                .builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .card(card)
                .timeCreated(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .status(SalesOrderStatus.CREATED.name())
                .build();
        if (salesOrder.getPaymentMethod().equals(PaymentMethod.BANK_TRANSFER.getName())) {
            salesOrder.setStatus(SalesOrderStatus.PENDING_PAYMENT.name());
        } else if (salesOrder.getPaymentMethod().equals(PaymentMethod.CASH.getName())) {
            salesOrder.setStatus(SalesOrderStatus.CREATED.name());
        } else if (salesOrder.getPaymentMethod().equals(PaymentMethod.WALLET.getName())) {
            salesOrder.setStatus(SalesOrderStatus.CREATED.name());
        }
        salesOrderRepository.save(salesOrder);
        return ApiResponse.<SaleOrderResponse>builder()
                .results(SaleOrderResponse
                        .builder()
                        .idCard(salesOrder.getCard().getIdCard())
                        .idSalesOrder(salesOrder.getIdSalesOrder())
                        .idUser(salesOrder.getUser().getIdUser())
                        .idShippingAddress(salesOrder.getShippingAddress().getIdShippingAddress())
                        .status(salesOrder.getStatus())
                        .build())
                .build();
    }
    @Override
    public ApiResponse getAllSalesOrder() {
        var contex = SecurityContextHolder.getContext();
        String idUser = contex.getAuthentication().getName();
        List<SalesOrder> listSalesOrder = salesOrderRepository.findByIdUser(idUser);
        List<SaleOrderResponse> results = new ArrayList<>();
        for (SalesOrder salesOrder: listSalesOrder)
        {
            SaleOrderResponse response = SaleOrderResponse
                    .builder()
                    .idSalesOrder(salesOrder.getIdSalesOrder())
                    .idCard(salesOrder.getCard().getIdCard())
                    .idShippingAddress(salesOrder.getShippingAddress().getIdShippingAddress())
                    .idUser(salesOrder.getUser().getIdUser())
                    .status(salesOrder.getStatus())
                    .paymentMethod(salesOrder.getPaymentMethod())
                    .build();
            results.add(response);
        }
        return new ApiResponse().builder().results(results).build();
    }

    @PostAuthorize("returnObject.idUser.toString() == authentication.name")
    @Override
    public SaleOrderResponse getSalesOrderById(Integer idSalesOrder) {
        var contex = SecurityContextHolder.getContext();
        String idUser = contex.getAuthentication().getName();

        SalesOrder salesOrder = salesOrderRepository
                .findById(idSalesOrder)
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
        return saleOrderResponse;
    }



    @Transactional
    @Override
    public SaleOrderResponse cancleOrder(Integer idSalesOrder) {
        SalesOrder salesOrder = salesOrderRepository.findById(idSalesOrder).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        if(salesOrder.getStatus().equals(SalesOrderStatus.IN_PROGRESS.name()))
        {
            salesOrder.setStatus(SalesOrderStatus.CANCLE.name());
            salesOrderRepository.save(salesOrder);
        }
        /// Hoan tien cho nguoi dung
        User user = userRepository.findById(salesOrder.getUser().getIdUser()).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        float currentWallet = user.getWallet();
        user.setWallet(currentWallet + salesOrder.getCard().getTotalPrice());
        ///
        return SaleOrderResponse
                .builder()
                .idSalesOrder(salesOrder.getIdSalesOrder())
                .idUser(salesOrder.getUser().getIdUser())
                .paymentMethod(salesOrder.getPaymentMethod())
                .status(salesOrder.getStatus())
                .idShippingAddress(salesOrder.getShippingAddress().getIdShippingAddress())
                .idCard(salesOrder.getCard().getIdCard())
                .build();
    }

    @PostAuthorize("returnObject.idUser.toString() == authentication.name")
    @Override
    public SaleOrderResponse completeSaleOrder(Integer idSalesOrder) {
        SalesOrder salesOrder = salesOrderRepository.findById(idSalesOrder).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        if(salesOrder.getStatus().equals(SalesOrderStatus.IN_PROGRESS.name()))
        {
            salesOrder.setStatus(SalesOrderStatus.COMPLETE.name());
            salesOrderRepository.save(salesOrder);
        }
        return SaleOrderResponse
                .builder()
                .idSalesOrder(salesOrder.getIdSalesOrder())
                .idUser(salesOrder.getUser().getIdUser())
                .paymentMethod(salesOrder.getPaymentMethod())
                .status(salesOrder.getStatus())
                .idShippingAddress(salesOrder.getShippingAddress().getIdShippingAddress())
                .idCard(salesOrder.getCard().getIdCard())
                .build();
    }
    @Override
    public List<SaleOrderResponse> getSaleOrdersBySpec(LocalDateTime start, LocalDateTime end, String name, Integer idCategory, String status)
    {
        Specification<SalesOrder> specHasTime = Specification.where(SaleOrderSpecification.hasTimeBetween(start, end));
        Specification<SalesOrder> specHasName = Specification.where(SaleOrderSpecification.hasManufracturer(name));
        Specification<SalesOrder> specHasIdCategory = Specification.where(SaleOrderSpecification.hasIdCategory(idCategory));
        Specification<SalesOrder> specHasSatus = Specification.where(SaleOrderSpecification.hasStatus(status));

        Specification<SalesOrder> spec = specHasTime
                                        .and(specHasName)
                                        .and(specHasIdCategory)
                                        .and(specHasSatus);
        Sort sort = Sort.by(Sort.Direction.ASC, "timeFinished");
        List<SalesOrder> listSaleOrder = salesOrderRepository.findAll(spec, sort);
        List<SaleOrderResponse> listSaleOrderResponse = new ArrayList<>();

        for(SalesOrder salesOrder : listSaleOrder)
        {
            SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
            listSaleOrderResponse.add(saleOrderResponse);
        }
        return listSaleOrderResponse;
    }
    @Override
    public List<SaleOrderResponse> getSaleOrdersByManufracturer(String name)
    {
        Specification<SalesOrder> spec = Specification.where(SaleOrderSpecification.hasManufracturer(name));

        List<SalesOrder> listSaleOrder = salesOrderRepository.findAll(spec);
        List<SaleOrderResponse> listSaleOrderResponse = new ArrayList<>();

        for(SalesOrder salesOrder : listSaleOrder)
        {
            SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
            listSaleOrderResponse.add(saleOrderResponse);
        }
        return listSaleOrderResponse;
    }
    @Override
    public List<SaleOrderResponse> getSaleOrdersByIdCategory(Integer idCategory)
    {
        Specification<SalesOrder> spec = Specification.where(SaleOrderSpecification.hasIdCategory(idCategory));

        List<SalesOrder> listSaleOrder = salesOrderRepository.findAll(spec);
        List<SaleOrderResponse> listSaleOrderResponse = new ArrayList<>();

        for(SalesOrder salesOrder : listSaleOrder)
        {
            SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
            listSaleOrderResponse.add(saleOrderResponse);
        }
        return listSaleOrderResponse;
    }

}
