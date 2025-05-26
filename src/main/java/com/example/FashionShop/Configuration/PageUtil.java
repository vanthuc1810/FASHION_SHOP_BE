package com.example.FashionShop.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
public class PageUtil {

    public static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<T> subList;

        if (list.size() < startItem) {
            subList = List.of(); // Trả về danh sách rỗng nếu vượt quá kích thước
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            subList = list.subList(startItem, toIndex);
        }

        return new PageImpl<>(subList, pageable, list.size());
    }
}
