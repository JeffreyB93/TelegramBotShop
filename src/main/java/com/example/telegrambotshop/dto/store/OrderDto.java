package com.example.telegrambotshop.dto.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDto {

    Long userId;
    List<Product> products = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}