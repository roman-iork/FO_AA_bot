package bot;

import main.Main;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import schemas.Schema;
import schemas.Schemas;
import model.VisitorChat;
import utils.Serialization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Bot extends TelegramLongPollingBot {

    public static final Map<String, Schema> schemas = new Schemas().getSchemas();
    public static Map<String, VisitorChat> chatMap = new HashMap<>();
    public static LinkedHashMap<String, String> photoMap;
    static {
        photoMap = retrievePhotoIds();
    }

    public Bot(String BOT_TOKEN) {
        super(BOT_TOKEN);
    }

    @Override
    public String getBotUsername() {
        try {
            return new BufferedReader(new FileReader(Main.BOT_NAME_PATH)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        //here I fetch video ID for potential video insertion
        if (update.hasMessage() && update.getMessage().hasVideo()) {
            System.out.println(update.getMessage().getVideo().getFileId());
        }//here I fetch photo ID for potential photo insertion
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            System.out.println(update.getMessage().getPhoto().getFirst().getFileId());
        }

        //further I start evaluation of messages form bot, so...
        try{
            if (update.hasMessage() && update.getMessage().hasText()) {
                //first create necessary variables
                //from incoming message
                Message inMess = update.getMessage();
                String chatId = inMess.getChatId().toString();
                String messageText = inMess.getText();
                //for preparing response
                var chat = chatMap.get(chatId);
                if (chat == null) {
                    chat = new VisitorChat(chatId);
                    chatMap.put(chatId, chat);
                }
                var response = chat.process(inMess);
                //and here I execute, send and so on ...
                addVisitor(inMess);
                if (chat.photoPath(messageText) != null) {
                    sendPhoto(inMess, chat.photoPath(messageText));
                }
                if (chat.videoPath() != null) {
                    sendVideo(chatId, chat.videoPath());
                }
                execute(response);
            }
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendVideo(String id, String path) {
        InputFile video = new InputFile(path);
        SendVideo sv = SendVideo.builder()
                .chatId(id)
                .video(video)
                .build();
        try {
            execute(sv);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(Message inMess, String path) {
        var message = inMess.getText();
        var chatId = inMess.getChatId();
        InputFile photo = new InputFile(path);
        SendPhoto sf = SendPhoto.builder()
                .chatId(chatId)
                .photo(photo)
                .build();
        try {
            var photoMessage = execute(sf);
            if (!photoMap.containsKey(message)) {
                var photoId = photoMessage.getPhoto().getFirst().getFileId();
                renewPhotoIds(message, photoId);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addVisitor(Message message) {
        String storageContent = Serialization.readFile(Main.PATH_FOR_STORAGE);
        var nameMap = new LinkedHashMap<String, String>();
        nameMap = storageContent.isEmpty() ? nameMap : Serialization.unserialize(storageContent);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String name = message.getFrom().getUserName();
        nameMap.put(name, time);
        storageContent = Serialization.serialize(nameMap);
        Serialization.renew(Main.PATH_FOR_STORAGE, storageContent);
    }

    private static LinkedHashMap<String, String> retrievePhotoIds() {
        String photoIds = Serialization.readFile(Main.PATH_FOR_PHOTO_IDS);
        var photoIdsMap = new LinkedHashMap<String, String>();
        photoIdsMap = photoIds.isEmpty() ? photoIdsMap : Serialization.unserialize(photoIds);
        return photoIdsMap;
    }

    private static void renewPhotoIds(String message, String photoId) {
        photoMap.put(message, photoId);
        String photoIds = Serialization.serialize(photoMap);
        Serialization.renew(Main.PATH_FOR_PHOTO_IDS, photoIds);
    }
}
