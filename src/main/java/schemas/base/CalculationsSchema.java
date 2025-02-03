package schemas.base;

import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Keyboards;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import schemas.Schema;

public class CalculationsSchema implements Schema {


    @Override
    public SendMessage run(Message inMess) {
        var sendMessage = new SendMessage();
        sendMessage.setText("Выберите вид расчета из меню \uD83D\uDC47");
        sendMessage.setReplyMarkup(Keyboards.calculationsKb());
        return sendMessage;
    }

    @Override
    public SendMessage reply(Message inMess) {
        return run(inMess);
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
