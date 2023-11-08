package com.example.telegrambotshop.client;


import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StoreClient {

    private final OkHttpClient client;

    @Value("${service.store.url}")
    private String serviceStoreUrl;


    public StoreClient(OkHttpClient client) {
        this.client = client;
    }

    public String getStoreService() throws ServiceException{
        Request request = new Request.Builder()
                .url(serviceStoreUrl)
                .get()
                .build();
        try (var response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body.string();
        } catch (IOException e) {
            throw new ServiceException("Нет подключение к серверу " + serviceStoreUrl + " !", e);
        }
    }

    public String getStoreService(String message) throws ServiceException {
        Request request = new Request.Builder()
                .url(serviceStoreUrl + message)
                .get()
                .build();
        try (var response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body.string();
        } catch (IOException e) {
            throw new ServiceException("Нет подключение к серверу " + serviceStoreUrl + " !", e);
        }
    }

    public String postStoreService(String nameProduct) throws ServiceException {
        Request request = new Request.Builder()
                .url(serviceStoreUrl + "/product")
                .post(RequestBody.create(nameProduct.getBytes()))
                .build();
        try (var response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body.string();
        } catch (IOException e) {
            throw new ServiceException("Нет подключение к серверу " + serviceStoreUrl + " !", e);
        }
    }
}