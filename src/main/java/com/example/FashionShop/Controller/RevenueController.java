package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.response.Revenue.RevenueResponse;
import com.example.FashionShop.IServices.IRevenueService;
import com.example.FashionShop.Services.RevenueService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/revenue")
public class RevenueController {
    IRevenueService revenueService;

    @GetMapping("/report")
    public RevenueResponse revenueByTime(
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer idCategory,
            @RequestParam(required = false) Integer idUser,
            @RequestParam(required = false, defaultValue = "DAY") String time,
            @RequestParam(required = false) String status)
            {
                return revenueService.report(start, end, name, idCategory, status, idUser, time);
            }


    @GetMapping("/export")
    public void report(HttpServletResponse response,
                       @RequestParam(required = false) LocalDateTime start,
                       @RequestParam(required = false) LocalDateTime end,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer idCategory,
                       @RequestParam(required = false) Integer idUser,
                       @RequestParam(required = false) String time,
                       @RequestParam(required = false) String status) throws IOException {
        revenueService.exportPDF(response, start, end, name, idCategory, status, idUser, time);
    }
}
