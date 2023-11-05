package com.example.telegrambotshop.client;


import com.example.telegrambotshop.dto.store.StoreDto;
import com.example.telegrambotshop.dto.user.RequestUserDto;
import com.example.telegrambotshop.dto.user.ResponseUserDto;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StoreClient {

    private final OkHttpClient client;

    @Value("${service.store.url}")
    private String serviceStoreUrl;


    public StoreClient(OkHttpClient client) {
        this.client = client;
    }

    private ObjectMapper mapper = new ObjectMapper();

    public List<StoreDto> getStoreService() throws ServiceException{
        Request request = new Request.Builder()
                .url(serviceStoreUrl)
                .get()
                .build();
        try (var response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            String qwe =  body.string();
            ObjectMapper objectMapper = new ObjectMapper();

            List<StoreDto> storesDto = objectMapper.readValue(body.string(), objectMapper.getTypeFactory().constructCollectionType(List.class, StoreDto.class));
            /*Class<StoreDto> ListStoreDto  = StoreDto.class;
            List<StoreDto> storeDto = Collections.singletonList(mapper.readValue(body.string(), ListStoreDto));*/
            return storesDto ;
        } catch (IOException e) {
            throw new ServiceException("Нет подключение к серверу " + serviceStoreUrl + " !", e);
        }
    }










}
