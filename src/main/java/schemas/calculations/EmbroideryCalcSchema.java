package schemas.calculations;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Keyboards;
import schemas.Schema;
import schemas.Schemas;
import utils.Size;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmbroideryCalcSchema implements Schema {
//    private Map<String, Integer> sidesData = new HashMap<>();
    private Map<String, Map<String, Object>> chatData = new HashMap<>();
    private final float tariff = 3000;


    @Override
    public SendMessage run(Message inMess) {
        clearData(inMess);
        var sendMessage = new SendMessage();
        sendMessage.setText("Введите первую сторону в см  \uD83D\uDCCF");
        sendMessage.setReplyMarkup(Keyboards.calcKb());
        return sendMessage;
    }

    @Override
    public SendMessage reply(Message inMess) {
        return getSendMessage(inMess);
    }

    public SendMessage getSendMessage(Message inMess) {
        var sendMessage = new SendMessage();

        var message = inMess.getText();
        var chatId = inMess.getChatId().toString();

        var currChatData = chatData.get(chatId);
        var currSidesData = (Map<String, Integer>) currChatData.get("sidesData");

        //if info button was pressed
        if (message.equals(Schemas.info)) {
            sendMessage.setText("""
                    Введите по очереди размеры работы:
                    желательная максимальная длина 70 см ☑\uFE0F
                    желательная максимальная ширина 40 см ☑\uFE0F
                    и бот рассчитает стоимость  ✅
                    
                    """
                    + getSideOrder(currSidesData));
            return sendMessage;
        }
        //checking if CANCEL was pressed
        if (message.equals(Schemas.cancel)) {
            clearData(inMess);

            currChatData = chatData.get(chatId);
            currSidesData = (Map<String, Integer>) currChatData.get("sidesData");

            sendMessage.setText(getSideOrder(currSidesData));
            return sendMessage;
        }
        //if they entered text instead of a number
        if (!isIntParsable(message) && currSidesData.containsValue(null)) {
            sendMessage.setText("Введите числовое значение \uD83D\uDD22" + "\n" + getSideOrder(currSidesData));
            return sendMessage;
        }
        //there I assign values to sides
        if (currSidesData.get("sideA") == null) {
            currSidesData.put("sideA", Integer.parseInt(message));
            currChatData.put("sidesData", currSidesData);
            chatData.put(chatId, currChatData);
        } else {
            currSidesData.computeIfAbsent("sideB", k -> Integer.parseInt(message));
            currChatData.put("sidesData", currSidesData);
            chatData.put(chatId, currChatData);
        }
        //there check if still some side was not entered
        if (currSidesData.containsValue(null)){
            sendMessage.setText(getSideOrder(currSidesData));
            return sendMessage;
        }

        //calculations
        var length = Math.max(currSidesData.get("sideA"), currSidesData.get("sideB"));
        var width = Math.min(currSidesData.get("sideA"), currSidesData.get("sideB"));
        var square = (float) length * width / 10_000;


        float finalCalculation = square * tariff;
        var additionalInfo = "";

        if (square < Size.XS.getValue()) {
            var coefficient = 20;
            finalCalculation *= coefficient;
            additionalInfo = String
                    .format("(Итоговая стоимость умножена на %d, так как площадь работы меньше %f м.кв.)",
                            coefficient,
                            Size.XS.getValue());
        } else if (square < Size.S.getValue()) {
            var coefficient = 5;
            finalCalculation *= coefficient;
            additionalInfo = String
                    .format("(Итоговая стоимость умножена на %d, так как площадь работы меньше %f м.кв.)",
                            coefficient,
                            Size.S.getValue());
        } else if (square < Size.XM.getValue()) {
            var coefficient = 2;
            finalCalculation *= coefficient;
            additionalInfo = String
                    .format("(Итоговая стоимость умножена на %d, так как площадь работы меньше %f м.кв.)",
                            coefficient,
                            Size.XM.getValue());
        }

        sendMessage.setText(String.format("""
                Стоимость натяжки вышивки,
                размерами %dсм на %dсм, составляет %d руб.
                """ + "\n" + additionalInfo,
                currSidesData.get("sideA"),
                currSidesData.get("sideB"),
                Math.round(finalCalculation)));
        sendMessage.setReplyMarkup(Keyboards.calcKb());
        clearData(inMess);
        return sendMessage;
    }

    public void clearData(Message inMess) {
        var chatId = inMess.getChatId().toString();
        var currChatData = new HashMap<String, Object>();
        var currSidesData = new HashMap<String, Integer>();
        currSidesData.put("sideA", null);
        currSidesData.put("sideB", null);
        currChatData.put("sidesData", currSidesData);
        chatData.put(chatId, currChatData);
    }

    private boolean isIntParsable(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String getSideOrder(Map<String, Integer> sidesData) {
        var nullCount = sidesData.values().stream().filter(Objects::isNull).toList();
        if (nullCount.size() == 2) {
            return "Введите первую сторону в см  \uD83D\uDCCF";
        } else if (nullCount.size() == 1) {
            return "Введите вторую сторону в см  \uD83D\uDCD0";
        }
        return "Скорее всего, все стороны введены \uD83E\uDD37\uD83C\uDFFC\u200D♂\uFE0F";
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
