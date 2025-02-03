package schemas.base;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.Message;
import merchants.AaSite;
import utils.Keyboards;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import schemas.Schema;
import schemas.Schemas;

public class AlfaSchema implements Schema {
    private AaSite aaSite = new AaSite();

    @Override
    public SendMessage run(Message inMess) {
        var sendMessage = new SendMessage();
        sendMessage.setText("Для просмотра наличия материала отправьте сообщение с номером артикула.");
        sendMessage.setReplyMarkup(Keyboards.alfaKb());
        return sendMessage;
    }

    @Override
    public SendMessage reply(Message inMess) {
        var message = inMess.getText();

        message = message.toUpperCase();
        var sendMessage = new SendMessage();
        if (message.equals(Schemas.info.toUpperCase())) {
            sendMessage.setText("Для багета наличие указывается в метрах, для паспарту в штуках.");
            sendMessage.setReplyMarkup(Keyboards.alfaKb());
        } else {
            sendMessage.setText(aaSite.getResponseText(message));
            sendMessage.setReplyMarkup(Keyboards.alfaKb());
        }
        return sendMessage;
    }

    @Override
    public String getVideoPath() {
        return null;
    }

    @Override
    public String getPhotoPath(String message) {
        if (Bot.photoMap.containsKey(message)) {
            return Bot.photoMap.get(message);
        }
        message = message.toUpperCase();
        var link = aaSite.getPhotoLink(message);
        if (link.isBlank()) {
            return null;
        }
        return link;
    }
}
