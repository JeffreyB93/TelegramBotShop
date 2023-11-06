package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.StoreClient;
import com.example.telegrambotshop.client.UserClient;
import com.example.telegrambotshop.dto.store.StoreDto;
import com.example.telegrambotshop.dto.user.RequestUserDto;
import com.example.telegrambotshop.dto.user.ResponseUserDto;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class AstonBot extends TelegramLongPollingBot {

    //private static final String START = "/start";
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private UserClient userClient;
    private StoreClient storeClient;
    private Authorization authorization;
    private ObjectMapper mapper = new ObjectMapper();

    private boolean userBoolean = false;
    private boolean passwordBoolean = false;


    public AstonBot(UserClient userClient, StoreClient storeClient,
                    Authorization authorization) {
        this.userClient = userClient;
        this.storeClient = storeClient;
        this.authorization = authorization;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getChat().getUserName();
        String message = update.getMessage().getText();

        if (message.equals("/start")) {
            userBoolean = authorization.startCommand(update);
        }
        if (userBoolean && !passwordBoolean) {
            passwordBoolean = authorization.authenticationCommand(update);
        } else if (userBoolean && passwordBoolean) {
            //store

        } else {
            userBoolean = authorization.registrationCommand(update);
            if (userBoolean) {
                passwordBoolean = true;
            }
        }




        //String message = update.getMessage().getText();
        //Long chatId = update.getMessage().getChatId();
        //String userName = update.getMessage().getChat().getUserName();



        /*if (!authorization) {
            switch (message) {
                case START -> {
                    startCommand(chatId, userName);
                }
                default -> authorizationCommand(chatId, userName, message);
            }
        } else {

        }*/

        /*switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case PHONE -> phoneCommand(chatId);*/
            /*case EUR -> eurCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }*/
    }

    /*private void authorizationCommand(Long chatId, String userName, String message) {

        RequestUserDto requestUserDto = new RequestUserDto(userName, message);
        Optional<String> jsonString = null;
        try {
            jsonString = userClient.getUserService(requestUserDto);
            ResponseUserDto responseUserDto = mapper.readValue(jsonString.get(), ResponseUserDto.class);
            if (!responseUserDto.getFirstName().equals("null")) {
                authorization = true;
                helloUser(chatId, responseUserDto.getFirstName(), responseUserDto.getLastName());
                if (true) {//todo вставить роль клиента
                    getStore(chatId);
                } else {//todo вставить Курьер

                }
            } else {
                authorizationNullCommand(chatId);
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(jsonString.get());
    }*/

    private void getStore(Long chatId) throws ServiceException {
        List<StoreDto> storeSDto = storeClient.getStoreService();
        ObjectMapper mapper = new ObjectMapper();

        List<String> storesName = storeSDto.stream().map(StoreDto::getName).collect(Collectors.toList());
        var text = """
                Введите название магазина.
                """ + storesName;
        var formattedText = String.format(text);
        sendMessage(chatId, formattedText);
    }

    private void helloUser(Long chatId, String firstName, String lastName) {
        var text = """
                %s %s Вы вошли!
                Теперь вы можете выбрать товар.
                """;
        var formattedText = String.format(text, firstName, lastName);
        sendMessage(chatId, formattedText);
    }

    private void passwordCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                Введите пожалуйста свой пароль.
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void authorizationNullCommand(Long chatId) {
        var text = """
                Вы не вошли. Не верный логин или пароль.
                """;
        var formattedText = String.format(text);
        sendMessage(chatId, formattedText);
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            //LOG.error("Ошибка отправки сообщения", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
