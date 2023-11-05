package com.example.telegrambotshop.client;


import com.example.telegrambotshop.dto.user.RequestUserDto;
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
import java.util.Optional;

@Component
public class UserClient {

    private final OkHttpClient client;

    @Value("${service.user.url}")
    private String serviceUserUrl;


    public UserClient(OkHttpClient client) {
        this.client = client;
    }

    private ObjectMapper mapper = new ObjectMapper();

    public Optional<String> getUserService(RequestUserDto requestUserDto) throws ServiceException, JsonProcessingException {
        String jsonString = mapper.writeValueAsString(requestUserDto);
        Request request = new Request.Builder()
                .url(serviceUserUrl)
                .post(RequestBody.create(jsonString.getBytes()))
                .build();

        try (var response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? Optional.empty() : Optional.of(body.string());
        } catch (IOException e) {
            throw new ServiceException("Нет подключение к серверу!", e);
        }
    }










}
