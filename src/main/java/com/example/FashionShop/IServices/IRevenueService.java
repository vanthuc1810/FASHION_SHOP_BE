package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.response.RevenueResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.time.LocalDateTime;

public interface IRevenueService {

    @PreAuthorize("hasRole('ADMIN')")
    RevenueResponse report(LocalDateTime start, LocalDateTime end, String name, Integer idCategory, String status);

    @PreAuthorize("hasRole('ADMIN')")
    void exportPDF (HttpServletResponse response,
                    LocalDateTime start,
                    LocalDateTime end,
                    String name,
                    Integer idCategory,
                    String status) throws IOException;
}
