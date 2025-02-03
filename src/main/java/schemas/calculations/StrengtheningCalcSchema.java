package schemas.calculations;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Keyboards;
import schemas.Schema;
import schemas.Schemas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StrengtheningCalcSchema implements Schema {
//    private Map<String, Integer> sidesData = new HashMap<>();
    private Map<String, Map<String, Object>> chatData = new HashMap<>();

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

        var side = inMess.getText();
        var chatId = inMess.getChatId().toString();

        var currChatData = chatData.get(chatId);
        var currSidesData = (Map<String, Integer>) currChatData.get("sidesData");

        if (isIntParsable(side) && Integer.parseInt(side) > 180) {
            sendMessage.setText("Слишком длинный, необходимо согласовать с мастером ☝\uFE0F");
            return sendMessage;
        }

        if (side.equals(Schemas.info)) {
            sendMessage.setText("""
                    Введите размеры подрамника.
                    Максимальная длина 180 см ☑\uFE0F
                    Максимальная ширина 90 см ☑\uFE0F
                    Если один из размеров больше,
                    изготовление надо согласовать с мастером \uD83E\uDD1D
                    
                    """
                    + getSideOrder(currSidesData));
            return sendMessage;
        }

        if (side.equals(Schemas.cancel)) {
            clearData(inMess);

            currChatData = chatData.get(chatId);
            currSidesData = (Map<String, Integer>) currChatData.get("sidesData");

            sendMessage.setText(getSideOrder(currSidesData));
            return sendMessage;
        }

        if (!currSidesData.containsValue(null)) {
            sendMessage.setText("Для нового расчета нажмите \"Отмена\" в меню \uD83D\uDC47");
            return sendMessage;
        }


        if (!isIntParsable(side)) {
            sendMessage.setText("Введите числовое значение \uD83D\uDD22" + "\n" + getSideOrder(currSidesData));
            return sendMessage;
        }

        if (currSidesData.get("sideA") == null) {
            currSidesData.put("sideA", Integer.parseInt(side));
            currChatData.put("sidesData", currSidesData);
            chatData.put(chatId, currChatData);
        } else {
            currSidesData.computeIfAbsent("sideB", k -> Integer.parseInt(side));
            currChatData.put("sidesData", currSidesData);
            chatData.put(chatId, currChatData);
        }

        if (currSidesData.containsValue(null)){
            sendMessage.setText(getSideOrder(currSidesData));
            return sendMessage;
        }
        var length = Math.max(currSidesData.get("sideA"), currSidesData.get("sideB"));
        var width = Math.min(currSidesData.get("sideA"), currSidesData.get("sideB"));

        if (length < 90) {
            sendMessage.setText("При длине менее 90 см усиление не требуется.");
            clearData(inMess);
            return sendMessage;
        }
        if (width > 90) {
            sendMessage.setText("Слишком широкий, необходимо согласовать с мастером ☝\uFE0F");
            clearData(inMess);
            return sendMessage;
        }
        var minInterval = 45;
        var plankQuantity = (length / minInterval) - 1;
        var pricePerCentimeter = (120.0 / 100) * 3;
        var planksPrice = (width - 8) * plankQuantity * pricePerCentimeter;
        sendMessage.setText("Подрамник " + length + " на " + width + " см."
                + "\n" + plankQuantity
                + " средн" + getEndingFirst(plankQuantity)
                + "план" + getEndingSecond(plankQuantity)
                + "\nЭто плюс " + Math.round(planksPrice) + " руб."
                + "\n----------" + "----------".repeat(plankQuantity)
                + "\n|          |" + "          |".repeat(plankQuantity)
                + "\n|          |" + "          |".repeat(plankQuantity)
                + "\n----------" + "----------".repeat(plankQuantity)
                + "\n\uD83D\uDE4B"
        );
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

    private String getEndingFirst(int quantity) {
        if (quantity == 1) {
            return "яя ";
        } else {
            return "их ";
        }
    }

    private String getEndingSecond(int quantity) {
        if (quantity == 1) {
            return "ка.";
        } else if (List.of(2, 3, 4).contains(quantity)) {
            return "ки.";
        } else {
            return "ок.";
        }
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
