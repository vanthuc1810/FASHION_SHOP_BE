package com.example.FashionShop.Dto.response;

import lombok.*;

public class ProductDTO {
    private Long idProduct;
    private String name;
    private String categoryName;
    private String colorId;
    private String sizeId;
    private Double discount;
    private String manufacturer;

    public ProductDTO(
            Long idProduct,
            String name,
            String categoryName,
            String colorId,
            String sizeId,
            Double discount,
            String manufacturer) {
        this.idProduct = idProduct;
        this.name = name;
        this.categoryName = categoryName;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.discount = discount;
        this.manufacturer = manufacturer;
    }

    // Getter và Setter
    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
