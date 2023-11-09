package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.UserClient;
import com.example.telegrambotshop.dto.user.AddressDto;
import com.example.telegrambotshop.dto.user.DeliverymanDto;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class UserCommands {

    private UserClient userClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    public UserCommands(UserClient userClient) {
        this.userClient = userClient;
    }

    public boolean startCommand(Update update) throws ServiceException, JsonProcessingException {
        Long chatId = update.getMessage().getChatId();
        Optional<String> response = userClient.postUserService(String.valueOf(chatId));
        return true;
    }

    public String addressCommand(Update update) throws ServiceException, JsonProcessingException {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String address = message.substring(9, message.length());
        AddressDto addressDto = new AddressDto();
        addressDto.setAddress(address);
        addressDto.setUserId(chatId);
        String jsonAddressDto = objectMapper.writeValueAsString(addressDto);
        String string = userClient.postUserService(jsonAddressDto).get();
        System.out.println(string);
        return string;
    }

    public String deliverymanCommand(Update update) throws JsonProcessingException, ServiceException {
        //String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        DeliverymanDto deliverymanDto = new DeliverymanDto();
        deliverymanDto.setUserId(chatId);
        deliverymanDto.setDeliveryman("delivery");
        String jsonDeliverymanDto = objectMapper.writeValueAsString(deliverymanDto);
        String string = userClient.postUserService(jsonDeliverymanDto).get();
        System.out.println(string);
        return string;
    }
}