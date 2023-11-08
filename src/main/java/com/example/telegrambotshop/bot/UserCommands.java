package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.UserClient;
import com.example.telegrambotshop.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class UserCommands {

    private UserClient userClient;

    public UserCommands(UserClient userClient) {
        this.userClient = userClient;
    }

    public boolean startCommand(Update update) throws ServiceException, JsonProcessingException {
        String userName = update.getMessage().getChat().getUserName();
        Optional<String> response = userClient.postUserService(userName);
        return true;
    }
}

    /*public boolean authenticationCommand(Update update) throws JsonProcessingException, ServiceException {
        String login = update.getMessage().getChat().getUserName();
        String password = update.getMessage().getText();
        RequestUserDto requestUserDto = new RequestUserDto(login, password);
        String jsonString = mapper.writeValueAsString(requestUserDto);
        Optional<String> response = userClient.postUserService(jsonString);
        if (response.isPresent()){
            return true;
        }
        return false;
    }*/

    /*public boolean registrationCommand(Update update) {

        sendMessage




        String login = update.getMessage().getChat().getUserName();
        String password = update.getMessage().getText();
        RequestUserDto requestUserDto = new RequestUserDto(login, password);
        String jsonString = mapper.writeValueAsString(requestUserDto);
        Optional<String> response = userClient.postUserService(jsonString);
        if (response.isPresent()){
            return true;
        }
        return false;
    }*/





        /*boolean registered = startCommand(update);
        if (registered) {
            authorizationCommand(update);
        } else {
            registrationCommand();
        }*/
