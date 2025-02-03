//package schemas.lessons;
//
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import utils.Keyboards;
//import schemas.Schema;
//
//public class GlassLessonSchema implements Schema {
//
//    @Override
//    public SendMessage run() {
//        var sendMessage = new SendMessage();
//        sendMessage.setText("Тут инфо про стекло \uD83D\uDC47");
//        sendMessage.setReplyMarkup(Keyboards.lessonKb());
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
//}
