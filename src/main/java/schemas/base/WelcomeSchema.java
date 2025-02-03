package schemas.base;

import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Keyboards;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import schemas.Schema;

public class WelcomeSchema implements Schema {

    @Override
    public SendMessage run(Message inMess) {
        var sendMessage = new SendMessage();
        sendMessage.setText("Выберите опцию из меню.");
        sendMessage.setReplyMarkup(Keyboards.welcomeKb());
        return sendMessage;
    }

    @Override
    public SendMessage reply(Message inMess) {
        var sendMessage = new SendMessage();
//        if (message.equals(Schemas.bot)) {
//            sendMessage.setText("Тут инфо про бот \uD83D\uDE09");
//            sendMessage.setReplyMarkup(Keyboards.welcomeKb());
//        } else {
            sendMessage.setText("Выберите опцию из меню \uD83D\uDC47");
            sendMessage.setReplyMarkup(Keyboards.welcomeKb());
//        }
        return sendMessage;
    }

    @Override
    public String getVideoPath() {
        return null;
    }

    @Override
    public String getPhotoPath(String message) {
        return null;
    }
}
