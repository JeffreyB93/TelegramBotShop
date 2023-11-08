package com.example.telegrambotshop.dto.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDto {

    Long userId;
    List<ProductDto> productsDto = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ProductDto> getProductsDto() {
        return productsDto;
    }

    public void setProductsDto(List<ProductDto> productsDto) {
        this.productsDto = productsDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(userId, orderDto.userId) && Objects.equals(productsDto, orderDto.productsDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productsDto);
    }
}
