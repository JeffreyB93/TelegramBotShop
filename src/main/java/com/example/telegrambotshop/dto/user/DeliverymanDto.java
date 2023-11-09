package com.example.telegrambotshop.dto.user;

import java.util.Objects;

public class DeliverymanDto {

    Long userId;
    String deliveryman;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeliveryman() {
        return deliveryman;
    }

    public void setDeliveryman(String deliveryman) {
        this.deliveryman = deliveryman;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliverymanDto that = (DeliverymanDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(deliveryman, that.deliveryman);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, deliveryman);
    }
}