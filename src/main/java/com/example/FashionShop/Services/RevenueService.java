package com.example.FashionShop.Services;


import com.example.FashionShop.Dto.response.Revenue.Item;
import com.example.FashionShop.Dto.response.Revenue.RevenueResponse;
import com.example.FashionShop.Dto.response.Revenue.RevenueResponseItem;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Enum.Day;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Enum.SalesOrderStatus;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IRevenueService;
import com.example.FashionShop.Repository.CardRepository;
import com.example.FashionShop.Repository.SalesOrderRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults; 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RevenueService implements IRevenueService {
    SalesOrderRepository salesOrderRepository;
    SalesOrderService salesOrderService;
    CardRepository cardRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RevenueResponse report(LocalDateTime start,
                                  LocalDateTime end,
                                  String name,
                                  Integer idCategory,
                                  String status,
                                  Integer idUser,
                                  String time) {
        List<SaleOrderResponse> listSaleOrderResponse = salesOrderService.getSaleOrdersBySpec(start, end, name, idCategory, status, idUser);
        Long totalOrder = (long) listSaleOrderResponse.size();
        float totalPrice = 0;
        // Get total price
        for(SaleOrderResponse saleOrderResponse : listSaleOrderResponse)
        {
            Card card = cardRepository.findById(saleOrderResponse.getIdCard())
                    .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
            totalPrice += card.getTotalPrice();
        }
        List<RevenueResponseItem> revenueResponseItems = new ArrayList<>();
        if(time.equals(Day.DAY.name()))
        {
            for (SalesOrderStatus salesOrderStatus : SalesOrderStatus.values()) {
                String nameStatus = salesOrderStatus.name();
                Map<String, Double> revenueByDay = new HashMap<>();
                List<Item> items = new ArrayList<>();
                int numRecord = 0;
                for(SaleOrderResponse saleOrderResponse : listSaleOrderResponse)
                {
                    if(saleOrderResponse.getStatus().equals(nameStatus))
                    {
                        int date = 0;
                        int month = 0;
                        int year = 0;
                        date = saleOrderResponse.getTimeCreated().getDayOfMonth();
                        month = saleOrderResponse.getTimeCreated().getMonthValue();
                        year = saleOrderResponse.getTimeCreated().getYear();
                        if(saleOrderResponse.getTimeFinished() != null)
                        {
                             date = saleOrderResponse.getTimeFinished().getDayOfMonth();
                             month = saleOrderResponse.getTimeFinished().getMonthValue();
                             year = saleOrderResponse.getTimeFinished().getYear();
                        }

                        String key = String.format("%04d-%02d-%02d", year, month, date);
                        Card card = cardRepository.findById(saleOrderResponse.getIdCard())
                                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
                        float price = card.getTotalPrice();
                        revenueByDay.put(key, revenueByDay.getOrDefault(key, 0.0) + price);
                        numRecord ++;
                    }
                }
                for (Map.Entry<String, Double> entry : revenueByDay.entrySet()) {
                    Item item = new Item()
                            .builder()
                            .time(LocalDate.parse(entry.getKey()))
                            .total(entry.getValue())
                            .build();
                    items.add(item);
                }
                RevenueResponseItem revenueResponseItem = new RevenueResponseItem()
                        .builder()
                        .numRecord(numRecord)
                        .status(nameStatus)
                        .items(items)
                        .build();
                if(revenueResponseItem.getItems().size() != 0)
                {
                    revenueResponseItems.add(revenueResponseItem);
                }
            }
        }

        if(time.equals(Day.MONTH.name()))
        {
            for (SalesOrderStatus salesOrderStatus : SalesOrderStatus.values()) {
                String nameStatus = salesOrderStatus.name();
                Map<String, Double> revenueByDay = new HashMap<>();
                List<Item> items = new ArrayList<>();
                int numRecord = 0;
                for(SaleOrderResponse saleOrderResponse : listSaleOrderResponse)
                {
                    if(saleOrderResponse.getStatus().equals(nameStatus))
                    {
                        int date = 1;
                        int month = 0;
                        int year = 0;
                        month = saleOrderResponse.getTimeCreated().getMonthValue();
                        year = saleOrderResponse.getTimeCreated().getYear();
                        if(saleOrderResponse.getTimeFinished() != null)
                        {
                            month = saleOrderResponse.getTimeFinished().getMonthValue();
                            year = saleOrderResponse.getTimeFinished().getYear();
                        }

                        String key = String.format("%04d-%02d-%02d", year, month, date);
                        Card card = cardRepository.findById(saleOrderResponse.getIdCard())
                                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
                        float price = card.getTotalPrice();
                        revenueByDay.put(key, revenueByDay.getOrDefault(key, 0.0) + price);
                        numRecord ++;
                    }
                }
                for (Map.Entry<String, Double> entry : revenueByDay.entrySet()) {
                    Item item = new Item()
                            .builder()
                            .time(LocalDate.parse(entry.getKey()))
                            .total(entry.getValue())
                            .build();
                    items.add(item);
                }
                RevenueResponseItem revenueResponseItem = new RevenueResponseItem()
                        .builder()
                        .numRecord(numRecord)
                        .status(nameStatus)
                        .items(items)
                        .build();
                if(revenueResponseItem.getItems().size() != 0)
                {
                    revenueResponseItems.add(revenueResponseItem);
                }
            }
        }

        if(time.equals(Day.YEAR.name()))
        {
            for (SalesOrderStatus salesOrderStatus : SalesOrderStatus.values()) {
                String nameStatus = salesOrderStatus.name();
                Map<String, Double> revenueByDay = new HashMap<>();
                List<Item> items = new ArrayList<>();
                int numRecord = 0;
                for(SaleOrderResponse saleOrderResponse : listSaleOrderResponse)
                {
                    if(saleOrderResponse.getStatus().equals(nameStatus))
                    {
                        int date = 1;
                        int month = 1;
                        int year = 0;
                        date = saleOrderResponse.getTimeCreated().getDayOfMonth();
                        month = saleOrderResponse.getTimeCreated().getMonthValue();
                        year = saleOrderResponse.getTimeCreated().getYear();
                        if(saleOrderResponse.getTimeFinished() != null)
                        {
                            year = saleOrderResponse.getTimeFinished().getYear();
                        }

                        String key = String.format("%04d-%02d-%02d", year, month, date);
                        Card card = cardRepository.findById(saleOrderResponse.getIdCard())
                                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
                        float price = card.getTotalPrice();
                        revenueByDay.put(key, revenueByDay.getOrDefault(key, 0.0) + price);
                        numRecord ++;
                    }
                }
                for (Map.Entry<String, Double> entry : revenueByDay.entrySet()) {
                    Item item = new Item()
                            .builder()
                            .time(LocalDate.parse(entry.getKey()))
                            .total(entry.getValue())
                            .build();
                    items.add(item);
                }
                RevenueResponseItem revenueResponseItem = new RevenueResponseItem()
                        .builder()
                        .numRecord(numRecord)
                        .status(nameStatus)
                        .items(items)
                        .build();
                if(revenueResponseItem.getItems().size() != 0)
                {
                    revenueResponseItems.add(revenueResponseItem);
                }
            }
        }
        return RevenueResponse
                .builder()
                .orders(listSaleOrderResponse)
                .data(revenueResponseItems)
                .totalOrder(totalOrder)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void exportPDF (HttpServletResponse response,
                           LocalDateTime start,
                           LocalDateTime end,
                           String name,
                           Integer idCategory,
                           String status,
                           Integer idUser,
                           String time) throws java.io.IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sales Orders");
            Row headerRow = sheet.createRow(0);
            // Create Header
            List<String> header = new ArrayList<>();
            header.add("STT");
            header.add("ID_SALE_ORDER");
            header.add("ID_USER");
            header.add("STATUS");
            header.add("PAYMENT_METHOD");
            header.add("AMOUNT");
            header.add("UNIT");
            header.add("TIME_FINISH");
            // In Header
            for(int i = 0; i < header.size(); i ++)
            {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header.get(i));
            }
            // In Du Lieu
            RevenueResponse revenueResponse = report(start, end, name, idCategory, status, idUser, time);
            long totalOrder = revenueResponse.getTotalOrder();
            float totalPrice = revenueResponse.getTotalPrice();
            List<SaleOrderResponse> listSaleOrderResponse = revenueResponse.getOrders();

            int index = 1;
            for (SaleOrderResponse saleOrderResponse : listSaleOrderResponse) {
                Row row = sheet.createRow(index);

                // Ghi từng cột tương ứng với header
                for (int i = 0; i < header.size(); i++) {
                    Cell cell = row.createCell(i);

                    switch (header.get(i)) {
                        case "STT":
                            cell.setCellValue(index); // Ghi số thứ tự
                            break;
                        case "ID_SALE_ORDER":
                            cell.setCellValue(saleOrderResponse.getIdSalesOrder());
                            break;
                        case "ID_USER":
                            cell.setCellValue(saleOrderResponse.getIdUser());
                            break;
                        case "STATUS":
                            cell.setCellValue(saleOrderResponse.getStatus());
                            break;
                        case "PAYMENT_METHOD":
                            cell.setCellValue(saleOrderResponse.getPaymentMethod());
                            break;
                        case "AMOUNT":
                            Card card = cardRepository.findById(saleOrderResponse.getIdCard())
                                    .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
                            cell.setCellValue(card.getTotalPrice());
                            break;
                        case "UNIT":
                            cell.setCellValue("VND");
                            break;
                        case "TIME_FINISH":
                            cell.setCellValue(saleOrderResponse.getTimeFinished().toString());
                            break;
                        default:
                            cell.setCellValue(""); // Giá trị mặc định nếu không khớp header
                    }
                }
                index++;
            }

            // In total Amount
            Row row = sheet.createRow(index);
            Cell title = row.createCell(0);
            title.setCellValue("TOTAL_AMOUNT");
            Cell value = row.createCell(1);
            value.setCellValue(totalPrice);

            // Set response headers for file download
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"sales_orders.xlsx\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            response.getOutputStream().flush();
        }
    }
}
