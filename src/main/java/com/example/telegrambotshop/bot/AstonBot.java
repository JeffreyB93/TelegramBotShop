package com.example.telegrambotshop.bot;

import com.example.telegrambotshop.client.StoreClient;
import com.example.telegrambotshop.client.UserClient;
import com.example.telegrambotshop.dto.store.Product;
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
    private static final String DELIVERYMAN = "/deliveryman";

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
        listofCommands.add(new BotCommand("/deliveryman", "курьер"));
        listofCommands.add(new BotCommand("/stores", "список магазинов"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        //String command = update.getMessage().getEntities().get(0).getText();
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
            address(chatId, userCommands.addressCommand(update));
        }
        if (message.contains(DELIVERYMAN)) {
            deliveryman(chatId, userCommands.deliverymanCommand(update));
        }
    }

    private void start(Long chatId, boolean userName) {
        String text = "";
        if (userName){
            text = """
                Добро пожаловать в бот!
                
                /deliveryman - если ты курьер
                /address/(Ваш адрес)
                
                /stores - список магазинов
                /order (Ваши продукты)
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

    private void store(Long chatId, List<Product> products) {
        StringBuilder text = new StringBuilder();
        text.append(" Введите название продукта.\n");
        text.append(" Название Цена  \n");

        for(Product product: products) {
            text.append(product.getName()).append("  ").append(product.getPrice()).append("\n");
            //text.append("/product/").append(productName).append("\n");
        }
        text.append("Пример \n");
        text.append("/order iop, hhh  \n");
        String formattedText = String.format(String.valueOf(text));
        sendMessage(chatId, formattedText);
    }

    private void product(Long chatId, String productName) {
        StringBuilder text = new StringBuilder();
        //text.append("Продукты ").append(productName).append(" добавлены в корзину.");
        text.append("Заказ выполнен.");
        String formattedText = String.format(String.valueOf(text));
        sendMessage(chatId, formattedText);
    }

    private void address(Long chatId, String addressCommand) {
        String text = """
            Адрес добавлен.
            """;
        sendMessage(chatId, text);
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

    private void deliveryman(Long chatId, String deliverymanCommand) {
        String text = """
            Привет курьер.
            """;
        sendMessage(chatId, text);
    }

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
