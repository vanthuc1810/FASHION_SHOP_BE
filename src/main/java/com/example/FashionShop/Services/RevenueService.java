package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.response.RevenueResponse;
import com.example.FashionShop.Dto.response.SaleOrderResponse;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IRevenueService;
import com.example.FashionShop.Repository.CardRepository;
import com.example.FashionShop.Repository.SalesOrderRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                                         String status) {
        List<SaleOrderResponse> listSaleOrderResponse = salesOrderService.getSaleOrdersBySpec(start, end, name, idCategory, status);
        Long totalOrder = (long) listSaleOrderResponse.size();
        float totalPrice = 0;
        // Get total price
        for(SaleOrderResponse saleOrderResponse : listSaleOrderResponse)
        {
            Card card = cardRepository.findById(saleOrderResponse.getIdCard())
                    .orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
            totalPrice += card.getTotalPrice();
        }
        return RevenueResponse
                .builder()
                .orders(listSaleOrderResponse)
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
                           String status) throws IOException {
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
            RevenueResponse revenueResponse = report(start, end, name, idCategory, status);
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
            }

            response.getOutputStream().flush();
        }
    }
}
