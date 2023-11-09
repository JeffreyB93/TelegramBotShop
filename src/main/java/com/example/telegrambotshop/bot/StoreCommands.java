package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.StoreClient;
import com.example.telegrambotshop.dto.store.OrderDto;
import com.example.telegrambotshop.dto.store.Product;
import com.example.telegrambotshop.dto.store.ProductDto;
import com.example.telegrambotshop.dto.store.StoreDto;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
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

    public List<Product> getGoodsCommand(Update update) throws ServiceException, JsonProcessingException {
        String message = update.getMessage().getText();
        String nameSops = message.substring(6, message.length());
        StoreDto storeDto = new StoreDto(null, nameSops);
        String jsonStoreDto = objectMapper.writeValueAsString(storeDto);
        String jsonGoodsDto = storeClient.postStoreServiceGoods(jsonStoreDto);
        //List<ProductDto> storesDto = objectMapper.readValue(jsonGoodsDto, new TypeReference<List<ProductDto>>() {});
        List<Product> products = objectMapper.readValue(jsonGoodsDto, new TypeReference<List<Product>>() {});
        //List<String> productName = storesDto.stream().map(ProductDto::getName).collect(Collectors.toList());
        return /*productName*/products;
    }

    public String orderGoodsCommand(Update update) throws ServiceException, JsonProcessingException {
        String message = update.getMessage().getText();
        Long userId = update.getMessage().getChatId();
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        String nameProducts = message.substring(7, message.length());
        List<String> listFromString = Arrays.asList(nameProducts.split(",\\s*"));
        //List<ProductDto> productDtos = new ArrayList<>();
        HashMap<String, Integer> productStringMap = new HashMap<>();
        List<Product> products = new ArrayList<>();

        /*for(int i = 0; i < listFromString.size(); i++) {
            ProductDto productDto = new ProductDto();
            productDto.setName(listFromString.get(i));
            productDtos.add(productDto);
        }*/
        for (int i = 0; i < listFromString.size(); i++) {
            if (!productStringMap.containsKey(listFromString.get(i))) {
                productStringMap.put(listFromString.get(i), 1);
            } else {
                Integer j = productStringMap.get(listFromString.get(i));
                productStringMap.put(listFromString.get(i), j + 1);
            }
        }
        for (Map.Entry<String, Integer> entry:productStringMap.entrySet()) {
            Product product = new Product();
            product.setName(entry.getKey());
            product.setQuantity(entry.getValue());
            products.add(product);
        }

        /*List<Product> products = new ArrayList<>();
        for (int i = 0; i < productDtos.size(); i++) {
            Product product = new Product();
            productDtos.get(i);
        }*/

        orderDto.setProducts(products);
        //orderDto.setProductsDto(productDtos);
        String jsonorderDto = objectMapper.writeValueAsString(orderDto);
        String string = storeClient.postStoreService(jsonorderDto);// какая от Ярослава приходит json
        return string;
    }
}
