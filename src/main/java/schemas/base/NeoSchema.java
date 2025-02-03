//package schemas.base;
//
//import utils.Keyboards;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import schemas.Schema;
//import schemas.Schemas;
//
//public class NeoSchema implements Schema {
//    @Override
//    public SendMessage run() {
//        var sendMessage = new SendMessage();
//        sendMessage.setText("Тут можно прочитать инфо про Неоарт."
//                + "\nДля просмотра наличия материала отправьте сообщение с номером артикула.");
//        sendMessage.setReplyMarkup(Keyboards.neoKb());
//        return sendMessage;
//    }
//
//    @Override
//    public SendMessage reply(String message) {
//        var sendMessage = new SendMessage();
//        if (message.equals(Schemas.info)) {
//            sendMessage.setText("Ляля инфо про Нео.");
//            sendMessage.setReplyMarkup(Keyboards.neoKb());
//        } else {
//            sendMessage.setText("Тут можно прочитать инфо про Неоарт."
//                    + "\nДля просмотра наличия материала отправьте сообщение с номером артикула.");
//            sendMessage.setReplyMarkup(Keyboards.neoKb());
//        }
//        return sendMessage;
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
