package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.StoreClient;
import com.example.telegrambotshop.dto.store.OrderDto;
import com.example.telegrambotshop.dto.store.ProductDto;
import com.example.telegrambotshop.dto.store.StoreDto;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreCommands {

    private StoreClient storeClient;
    private ObjectMapper objectMapper = new ObjectMapper();

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
        String nameSops = message.substring(6, message.length());
        StoreDto storeDto = new StoreDto(null, nameSops);
        String jsonStoreDto = objectMapper.writeValueAsString(storeDto);
        String jsonGoodsDto = storeClient.postStoreServiceGoods(jsonStoreDto);
        List<ProductDto> storesDto = objectMapper.readValue(jsonGoodsDto, new TypeReference<List<ProductDto>>() {});
        List<String> productName = storesDto.stream().map(ProductDto::getName).collect(Collectors.toList());
        return productName;
    }

    public String orderGoodsCommand(Update update) throws ServiceException, JsonProcessingException {
        String message = update.getMessage().getText();
        Long userId = update.getMessage().getChatId();
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        String nameProducts = message.substring(7, message.length());
        List<String> listFromString = Arrays.asList(nameProducts.split(",\\s*"));
        List<ProductDto> productDtos = new ArrayList<>();
        for(int i = 0; i < listFromString.size(); i++) {
            ProductDto productDto = new ProductDto();
            productDto.setName(listFromString.get(i));
            productDtos.add(productDto);
        }
        orderDto.setProductsDto(productDtos);
        String jsonorderDto = objectMapper.writeValueAsString(orderDto);
        String string = storeClient.postStoreService(jsonorderDto);
        return string;
    }
}
