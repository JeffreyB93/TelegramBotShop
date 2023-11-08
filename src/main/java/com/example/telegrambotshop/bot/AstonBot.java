package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.StoreClient;
import com.example.telegrambotshop.client.UserClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class AstonBot extends TelegramLongPollingBot {

    private static final String START = "/start";
    private static final String STORES = "/stores";
    private static final String SHOP = "/shop";
    private static final String ORDER = "/order";
    private static final String ADDRESS = "/address";

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private UserClient userClient;
    private StoreClient storeClient;
    private UserCommands userCommands;
    private StoreCommands storeCommands;

    public AstonBot(UserClient userClient,
                    StoreClient storeClient,
                    UserCommands userCommands,
                    StoreCommands storeCommands) {
        this.userClient = userClient;
        this.storeClient = storeClient;
        this.userCommands = userCommands;
        this.storeCommands = storeCommands;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "приветствие"));
        listofCommands.add(new BotCommand("/stores", "список магазинов"));
        listofCommands.add(new BotCommand("/order", "список продуктов"));
        listofCommands.add(new BotCommand("/help", "помощь"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        String command = update.getMessage().getEntities().get(0).getText();
        System.out.println(message);
        switch (message) {
            case START -> start(chatId, userCommands.startCommand(update));
            case STORES -> stores(chatId, storeCommands.storesCommand(update));
            //case STORE -> store(chatId, storeCommands.getGoodsCommand(update));
            /*case ORDER + id ->;
            case basket + id ->;
            default -> helepCommand(chatId, userName, message);*/
        }
        if (message.contains(SHOP)) {
            store(chatId, storeCommands.getGoodsCommand(update));
        }
        if (message.contains(ORDER)) {
            product(chatId, storeCommands.orderGoodsCommand(update));
        }
        if (message.contains(ADDRESS)) {
            stop(chatId, userCommands.addressCommand(update));
        }
    }

    private void start(Long chatId, boolean userName) {
        String text = "";
        if (userName){
            text = """
                Добро пожаловать в бот!
                
                /delivery - если ты курьер
                /stores - список магазинов
                /address/(Ваш адрес)- завершение заказа 
                
                Дополнительные команды:
                /help - получение справки
                """;
        }
        sendMessage(chatId, text);
    }

    private void stores(Long chatId, List<String> goodsName) {
        StringBuilder text = new StringBuilder();
        text.append("""
                Введите название магазина.
                """);

        for(String goodName: goodsName) {
            text.append("/shop/").append(goodName).append("\n");
        }
        String formattedText = String.format(String.valueOf(text));
        sendMessage(chatId, formattedText);
    }

    private void store(Long chatId, List<String> productsName) {
        StringBuilder text = new StringBuilder();
        text.append(" Введите название продукта.\n");
        for(String productName: productsName) {
            text.append("/product/").append(productName).append("\n");
        }
        String formattedText = String.format(String.valueOf(text));
        sendMessage(chatId, formattedText);
    }

    private void product(Long chatId, String productName) {
        StringBuilder text = new StringBuilder();
        text.append("Продукт ").append(productName).append(" добавлен в корзину.");
        String formattedText = String.format(String.valueOf(text));
        sendMessage(chatId, formattedText);
    }

    private void stop(Long chatId, String address) {
        String text = "";
        text = """
            Введите пдрес через /address/(Ваш адрес)
            
            /delivery - если ты курьер
            /stores - список магазинов
            /stop - завершить заказ
            
            Дополнительные команды:
            /help - получение справки
            """;
        sendMessage(chatId, text);

    }


        /*if (message.equals("/start")) {
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
        }*/




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

    /*private void getStore(Long chatId) throws ServiceException {
        List<StoreDto> storeSDto = storeClient.getStoreService();
        ObjectMapper mapper = new ObjectMapper();

        List<String> storesName = storeSDto.stream().map(StoreDto::getName).collect(Collectors.toList());
        var text = """
                Введите название магазина.
                """ + storesName;
        var formattedText = String.format(text);
        sendMessage(chatId, formattedText);
    }*/

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
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
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
