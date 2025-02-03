//package schemas.base;
//
//import utils.Keyboards;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import schemas.Schema;
//
//public class LessonsSchema implements Schema {
//
//    @Override
//    public SendMessage run() {
//        var sendMessage = new SendMessage();
//        sendMessage.setText("Выберите тему из меню \uD83D\uDC47");
//        sendMessage.setReplyMarkup(Keyboards.lessonsKb());
//        return sendMessage;
//    }
//
//    @Override
//    public SendMessage reply(String message) {
//        return run();
//    }
//
//    @Override
//    public String getVideoPath() {
//        return null;
//    }
//
//    @Override
//    public String getPhotoPath(String message) {
//        return null;
//    }
//
//}
