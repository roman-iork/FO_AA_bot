package model;

import bot.Bot;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import schemas.Schema;
import schemas.Schemas;
import schemas.base.WelcomeSchema;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VisitorChat {
    private String id;
    private Schema schema;
    private List<Schema> cash;

    private Integer sideA;
    private Integer sideB;
    private List<String> fillings;

    public VisitorChat(String id) {
        this.id = id;
        this.schema = new WelcomeSchema();
        this.cash = new ArrayList<>();
        cash.add(schema);
    }

    public SendMessage process(Message message) {
        SendMessage outMessage;
        var messageText = message.getText();
        var schemas = Bot.schemas;
        if (schemas.containsKey(messageText)) {
            renewSchema(schemas.get(messageText));
            outMessage = schema.run(message);
        } else if (messageText.equals(Schemas.back)) {
            stepCacheBack();
            outMessage = schema.run(message);
        } else {
            outMessage = schema.reply(message);
        }
        outMessage.setChatId(message.getChatId());
        return outMessage;
    }

    private void renewSchema(Schema schema) {
        this.schema = schema;
        cash.add(schema);
        if (cash.size() > 6) {
            cash.removeFirst();
        }
    }

    private void stepCacheBack() {
        this.cash.removeLast();
        if (cash.isEmpty()) {
            cash.add(new WelcomeSchema());
        }
        this.schema = cash.getLast();
    }

    public void clearCalcData() {
        this.sideA = null;
        this.sideB = null;
        this.fillings = new ArrayList<>();
    }

    public void addFilling(String filling) {
        this.fillings.add(filling);
    }

    public String photoPath(String message) {
        return schema.getPhotoPath(message);
    }

    public String videoPath() {
        return schema.getVideoPath();
    }
}
