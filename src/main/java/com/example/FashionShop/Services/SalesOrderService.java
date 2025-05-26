package com.example.FashionShop.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.FashionShop.Configuration.PageUtil;
import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Entity.*;
import com.example.FashionShop.IServices.ISalesOrderService;
import com.example.FashionShop.Mapper.SaleOrderMapper;
import com.example.FashionShop.Repository.*;
import com.example.FashionShop.Specification.SaleOrderSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Enum.PaymentMethod;
import com.example.FashionShop.Enum.SalesOrderStatus;
import com.example.FashionShop.Exception.AppException;

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
    ProductRepository productRepository;

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
            if(salesOrder.getCard().getTotalPrice() > user.getWallet())
            {
                salesOrder.setStatus(SalesOrderStatus.CREATED.name());
                salesOrderRepository.save(salesOrder);
                throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
            }
            // check so luong
            List<CardItem> cardItemList = salesOrder.getCard().getCardItems();
            List<Product> productList = new ArrayList<>();
            for(CardItem cardItem : cardItemList){
                Product product = cardItem.getProduct();
                int quantity = cardItem.getQuantity();
                int productQuantity = product.getQuantity();
                if(productQuantity < quantity){
                    throw new AppException(ErrorCode.QUANTITY_INVALID);
                }
                product.setQuantity(productQuantity - quantity);
                productList.add(product);
            }
            productRepository.saveAll(productList);
            salesOrder.setStatus(SalesOrderStatus.IN_PROGRESS.name());
            user.setWallet(user.getWallet() - salesOrder.getCard().getTotalPrice());
            userRepository.save(user);
        }
        salesOrderRepository.save(salesOrder);

        SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
        return ApiResponse.<SaleOrderResponse>builder()
                .results(saleOrderResponse)
                .build();
    }


    @Override
    public PageableResponse getAllSalesOrder(Pageable pageable) {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();

        // Lấy tất cả đơn hàng của người dùng
        List<SalesOrder> listSalesOrder = salesOrderRepository.findByIdUser(idUser);

        // Sort danh sách SalesOrder theo id (tăng dần hoặc giảm dần)
         listSalesOrder.sort(Comparator.comparing(SalesOrder::getIdSalesOrder).reversed()); // Giảm dần

        // Chuyển danh sách thành trang
        Page<SalesOrder> pageOrigin = PageUtil.toPage(listSalesOrder, pageable);
        listSalesOrder = pageOrigin.getContent();

        // Map từ SalesOrder sang SaleOrderResponse
        List<SaleOrderResponse> results = new ArrayList<>();
        for (SalesOrder salesOrder : listSalesOrder) {
            SaleOrderResponse response = saleOrderMapper.toSaleOrderResponse(salesOrder);
            results.add(response);
        }

        return PageableResponse
                .builder()
                .results(results)
                .size(pageOrigin.getSize())
                .totalElements(pageOrigin.getTotalElements())
                .totalPages(pageOrigin.getTotalPages())
                .number(pageOrigin.getNumber())
                .build();
    }

    @Override
    public ApiResponse getStatus() {
        List<String> listStatus = salesOrderRepository.findAllStatus();
        return new ApiResponse()
                .builder()
                .results(listStatus)
                .build();
    }

    @Override
    public ApiResponse getPaymentMethod() {
        List<String> listStatus = salesOrderRepository.findAllPaymentMethod();
        return new ApiResponse()
                .builder()
                .results(listStatus)
                .build();
    }

    @Override
    public SaleOrderResponse checkout(Integer idOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idUser = Integer.valueOf(authentication.getName());
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        SalesOrder salesOrder = salesOrderRepository.findById(idOrder).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
        if(salesOrder.getPaymentMethod().equals(PaymentMethod.WALLET.getName()))
        {
            float wallet = user.getWallet();
            float cost = salesOrder.getCard().getTotalPrice();
            if(wallet < cost)
            {
                throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
            }
            user.setWallet(wallet - cost);
            salesOrder.setStatus(SalesOrderStatus.IN_PROGRESS.name());
            userRepository.save(user);
            salesOrder = salesOrderRepository.save(salesOrder);
        }
        SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
        return saleOrderResponse;
    }

    @Override
    public ApiResponse completeSaleOrderList(CompleteSaleOrdersRequest request) {
        List<SalesOrder> salesOrderList = salesOrderRepository.findAllById(request.getIdOrders());
        salesOrderList.stream().map(salesOrder -> {
            if (salesOrder.getStatus().equals(SalesOrderStatus.IN_PROGRESS.name())){
                salesOrder.setStatus(SalesOrderStatus.COMPLETE.name());
                salesOrder.setTimeFinished(LocalDateTime.now());
            }
            return salesOrder;
        }).collect(Collectors.toList());
        List<SaleOrderResponse> saleOrderResponseList = salesOrderList.stream().map(saleOrderMapper::toSaleOrderResponse).collect(Collectors.toList());
        salesOrderRepository.saveAll(salesOrderList);
        return ApiResponse
                .builder()
                .results(saleOrderResponseList)
                .build();
    }

    @Override
    public ApiResponse cancleSaleOrderList(CancleSaleOrdersRequest request) {
        List<SalesOrder> salesOrderList = salesOrderRepository.findAllById(request.getIdOrders());
        salesOrderList.stream().map(salesOrder -> {
            if (salesOrder.getStatus().equals(SalesOrderStatus.CREATED.name()) || salesOrder.getStatus().equals(SalesOrderStatus.PENDING_PAYMENT.name())){
                salesOrder.setStatus(SalesOrderStatus.CANCLE.name());
                salesOrder.setTimeFinished(LocalDateTime.now());
            }
            return salesOrder;
        }).collect(Collectors.toList());
        List<SaleOrderResponse> saleOrderResponseList = salesOrderList.stream().map(saleOrderMapper::toSaleOrderResponse).collect(Collectors.toList());
        salesOrderRepository.saveAll(salesOrderList);
        return ApiResponse
                .builder()
                .results(saleOrderResponseList)
                .build();
    }

    @Override
    public ApiResponse inProgressSaleOrderList(InProgressOrdersRequest request) {
        List<SalesOrder> salesOrderList = salesOrderRepository.findAllById(request.getIdOrders());
        for (SalesOrder salesOrder : salesOrderList) {
            if (salesOrder.getPaymentMethod().equals(PaymentMethod.CASH.getName()) &&
                    salesOrder.getStatus().equals(SalesOrderStatus.CREATED.name())) {

                List<CardItem> cardItemList = salesOrder.getCard().getCardItems();
                List<Product> productList = new ArrayList<>();
                boolean hasInsufficientStock = false;

                for (CardItem cardItem : cardItemList) {
                    Product product = cardItem.getProduct();
                    int requireQuantity = cardItem.getQuantity();
                    int productQuantity = product.getQuantity();

                    if (productQuantity < requireQuantity) {
                        hasInsufficientStock = true;
                        break; // Ra khỏi vòng lặp cardItem
                    }
                    product.setQuantity(productQuantity - requireQuantity);
                    productList.add(product);
                }

                if (hasInsufficientStock) {
                    continue; // Bỏ qua đơn hàng này, xử lý đơn tiếp theo
                }

                productRepository.saveAll(productList);

                // Nếu đủ hàng thì cập nhật trạng thái
                salesOrder.setStatus(SalesOrderStatus.IN_PROGRESS.name());
            }
        }
        List<SaleOrderResponse> saleOrderResponseList = salesOrderList.stream().map(saleOrderMapper::toSaleOrderResponse).collect(Collectors.toList());
        salesOrderRepository.saveAll(salesOrderList);
        return ApiResponse
                .builder()
                .results(saleOrderResponseList)
                .build();
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
        User user = userRepository.findById(salesOrder.getUser().getIdUser()).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        if(salesOrder.getStatus().equals(SalesOrderStatus.IN_PROGRESS.name()) && salesOrder.getPaymentMethod().equals(PaymentMethod.BANK_TRANSFER.getName()))
        {
            salesOrder.setStatus(SalesOrderStatus.CANCLE.name());
            salesOrder = salesOrderRepository.save(salesOrder);
            float currentWallet = user.getWallet();
            user.setWallet(currentWallet + salesOrder.getCard().getTotalPrice());
        }
        if(salesOrder.getStatus().equals(SalesOrderStatus.CREATED.name()))
        {
            if(salesOrder.getPaymentMethod().equals(PaymentMethod.WALLET.getName()))
            {
                salesOrder.setStatus(SalesOrderStatus.CANCLE.name());
                salesOrder = salesOrderRepository.save(salesOrder);
                float currentWallet = user.getWallet();
                user.setWallet(currentWallet + salesOrder.getCard().getTotalPrice());
            }
            if(salesOrder.getPaymentMethod().equals(PaymentMethod.CASH.getName()))
            {
                salesOrder.setStatus(SalesOrderStatus.CANCLE.name());
                salesOrder = salesOrderRepository.save(salesOrder);
            }
        }
        /// Hoan tien cho nguoi dung
        userRepository.save(user);
        SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
        return saleOrderResponse;
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
        SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
        return saleOrderResponse;
    }
    @Override
    public List<SaleOrderResponse> getSaleOrdersBySpec(LocalDateTime start, LocalDateTime end, String name, Integer idCategory, String status, Integer idUser)
    {
        Specification<SalesOrder> specHasTime = Specification.where(SaleOrderSpecification.hasTimeBetween(start, end));
        Specification<SalesOrder> specHasName = Specification.where(SaleOrderSpecification.hasManufracturer(name));
        Specification<SalesOrder> specHasIdCategory = Specification.where(SaleOrderSpecification.hasIdCategory(idCategory));
        Specification<SalesOrder> specHasSatus = Specification.where(SaleOrderSpecification.hasStatus(status));
        Specification<SalesOrder> specHasIdUser = Specification.where(SaleOrderSpecification.hasIdUser(idUser));

        Specification<SalesOrder> spec = specHasTime
                                        .and(specHasName)
                                        .and(specHasIdCategory)
                                        .and(specHasSatus)
                                        .and(specHasIdUser);
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageableResponse getSaleOrders (Pageable pageable, FilterOrderRequest request)
    {
        Specification<SalesOrder> specHasStatusList = Specification.where(SaleOrderSpecification.hasStatusList(request.getStatus()));
        Specification<SalesOrder> specHasPaymentMethodList = Specification.where(SaleOrderSpecification.hasPaymentMethodList(request.getPaymentMethod()));
        Specification<SalesOrder> spec = specHasStatusList
                .and(specHasStatusList)
                .and(specHasPaymentMethodList);
        if(request.isAcs())
        {
            if(request.getTypeFilter().equals("totalPrice"))
            {
                Sort sort = Sort.by(Sort.Order.asc("card.totalPrice"));
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }else
            {
                Sort sort = Sort.by(Sort.Order.asc(request.getTypeFilter()));
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
        }else {
            if(request.getTypeFilter().equals("totalPrice"))
            {
                Sort sort = Sort.by(Sort.Order.desc("card.totalPrice"));
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }else
            {
                Sort sort = Sort.by(Sort.Order.desc(request.getTypeFilter()));
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
        }

        Page<SalesOrder> pageOrigin = salesOrderRepository.findAll(spec, pageable);
        List<SalesOrder> salesOrderList = pageOrigin.getContent();
        List<SaleOrderResponse> saleOrderResponseList = new ArrayList<>();
        for(SalesOrder salesOrder : salesOrderList)
        {
            SaleOrderResponse saleOrderResponse = saleOrderMapper.toSaleOrderResponse(salesOrder);
            saleOrderResponse.setTotal(salesOrder.getCard().getTotalPrice());
            saleOrderResponse.setAddress(salesOrder.getShippingAddress().getAddress());
            saleOrderResponseList.add(saleOrderResponse);
        }

        return PageableResponse
                .builder()
                .results(saleOrderResponseList)
                .size(pageOrigin.getSize())
                .totalElements(pageOrigin.getTotalElements())
                .totalPages(pageOrigin.getTotalPages())
                .number(pageOrigin.getNumber())
                .build();
    }


}
