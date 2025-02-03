package schemas;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Schema {
    SendMessage run(Message inMess);
    SendMessage reply(Message inMess);

    String getVideoPath();
    String getPhotoPath(String message);
}
