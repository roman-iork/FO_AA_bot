package main;

import bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static final String PATH_FOR_PHOTO_IDS;
    public static final String PATH_FOR_STORAGE;
    public static final String PATH_FOR_PASSWORD;
    public static final String PATH_FOR_USERNAME;
    public static final String TOKEN_PATH;
    public static final String BOT_NAME_PATH;

    private static String currPath = "testPaths.txt";
//    private static String currPath = "prodPaths.txt";

    static {
        try {
            var input = new BufferedReader(new FileReader(currPath));
            PATH_FOR_PHOTO_IDS = input.readLine();
            PATH_FOR_STORAGE = input.readLine();
            PATH_FOR_PASSWORD = input.readLine();
            PATH_FOR_USERNAME = input.readLine();
            TOKEN_PATH = input.readLine();
            BOT_NAME_PATH = input.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = null;
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            input = new BufferedReader(new FileReader(TOKEN_PATH));
            final String BOT_TOKEN = input.readLine();
            Bot bot = new Bot(BOT_TOKEN);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        } finally {
            if (input != null) input.close();
        }
    }
}
