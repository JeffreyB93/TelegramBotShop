package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.StoreClient;
import com.example.telegrambotshop.dto.store.ProductDto;
import com.example.telegrambotshop.dto.store.StoreDto;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreCommands {

    private StoreClient storeClient;
    ObjectMapper objectMapper = new ObjectMapper();

    public StoreCommands(StoreClient storeClient) {
        this.storeClient = storeClient;
    }

    public List<String> storesCommand(Update update) throws ServiceException, JsonProcessingException {
        String jsonStoresDto = storeClient.getStoreService();
        List<StoreDto> storesDto = objectMapper.readValue(jsonStoresDto, new TypeReference<List<StoreDto>>() {});
        List<String> storesName = storesDto.stream().map(StoreDto::getName).collect(Collectors.toList());
        return storesName;
    }


    public List<String> getGoodsCommand(Update update) throws ServiceException, JsonProcessingException {
        String message = update.getMessage().getText();
        String jsonGoodsDto = storeClient.getStoreService(message);
        List<ProductDto> storesDto = objectMapper.readValue(jsonGoodsDto, new TypeReference<List<ProductDto>>() {});
        List<String> productName = storesDto.stream().map(ProductDto::getName).collect(Collectors.toList());
        return productName;
    }

    public String postGoodCommand(Update update) throws ServiceException {
        String message = update.getMessage().getText();
        String nameProduct = message.substring(9, message.length());
        String string = storeClient.postStoreService(nameProduct);
        System.out.println(nameProduct);
        return string;
    }

    public String endGoodCommand(Update update) {
        String message = update.getMessage().getText();

        return message;
    }

}
