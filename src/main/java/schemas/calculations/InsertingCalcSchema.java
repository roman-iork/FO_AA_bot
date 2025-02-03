package schemas.calculations;

import bot.Bot;
import model.VisitorChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Keyboards;
import schemas.Schema;
import schemas.Schemas;
import utils.Size;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertingCalcSchema implements Schema {
    static Map<String, Integer> fillingData = new HashMap<>();
    static {
        fillingData.put(Schemas.foamFilling, 200);
        fillingData.put(Schemas.canvasFilling, 800);
        fillingData.put(Schemas.glassFilling, 300);
        fillingData.put(Schemas.passFilling, 400);
    }

    @Override
    public SendMessage run(Message inMess) {
        var chat = Bot.chatMap.get(inMess.getChatId().toString());
        chat.clearCalcData();
        var sendMessage = new SendMessage();
        sendMessage.setText("Введите первую сторону в см  \uD83D\uDCCF");
        sendMessage.setReplyMarkup(Keyboards.calcKb());
        return sendMessage;
    }

    @Override
    public SendMessage reply(Message inMess) {
        var sendMessage = new SendMessage();
        var message = inMess.getText();
        var chatId = inMess.getChatId().toString();
        var chat = Bot.chatMap.get(chatId);
        var sideA = chat.getSideA();
        var sideB = chat.getSideB();
        var fillings = chat.getFillings();

        //if info button was pressed
        if (message.equals(Schemas.info)) {
            sendMessage.setText("""
                    - Введите по очереди размеры работы:
                    желательная максимальная длина 120 см ☑\uFE0F
                    желательная максимальная ширина 80 см ☑\uFE0F
                    - Выберите опции установки 📝
                    - Нажмите готово ✅
                    
                    """
                    + getSideOrder(sideA, sideB));
            sendMessage.setReplyMarkup(Keyboards.calcKb());
        }
        //checking if CANCEL was pressed
        else if (message.equals(Schemas.cancel)) {
            chat.clearCalcData();
            sendMessage.setText("Введите первую сторону в см  \uD83D\uDCCF");
            sendMessage.setReplyMarkup(Keyboards.calcKb());
        }
        //if they entered text instead of a number
        else if (!isIntParsable(message) && (sideA == null || sideB == null)) {
            sendMessage.setText("Введите числовое значение \uD83D\uDD22" + "\n" + getSideOrder(sideA,sideB));
            sendMessage.setReplyMarkup(Keyboards.calcKb());
        }
        //setting side A
        else if (sideA == null) {
            chat.setSideA(Integer.parseInt(message));
            sendMessage.setText("Введите вторую сторону в см  \uD83D\uDCD0");
            sendMessage.setReplyMarkup(Keyboards.calcKb());

        }
        //setting side B
        else if (sideB == null) {
            chat.setSideB(Integer.parseInt(message));
            sendMessage.setText("""
                    Выберете по очереди в любом порядке
                    состав установки и нажмите "Готово".
                    """);
            sendMessage.setReplyMarkup(Keyboards.calcFillingKb());
        }
        //if they choose fillings
        else if (fillingData.containsKey(message)) {
            chat.addFilling(message);
            sendMessage.setText(String.format("""
                    Был добавлен такой вид установки: %s.
                    Можете добавить еще или нажать "Готово".
                    """, message.toUpperCase()));
            sendMessage.setReplyMarkup(Keyboards.calcFillingKb());
        }
        //if they are done with choosing
        else if (message.equals(Schemas.done)) {
            if (fillings.isEmpty()) {
                sendMessage.setText("""
                    Вы ничего не выбрали!".
                    Выберете по очереди в любом порядке
                    состав установки и нажмите "Готово".
                    """);
                sendMessage.setReplyMarkup(Keyboards.calcFillingKb());
            } else {
                sendMessage.setText(calculate(chat));
                sendMessage.setReplyMarkup(Keyboards.calcKb());
                chat.clearCalcData();
            }
        }
        return sendMessage;
    }

    private String calculate(VisitorChat chat) {
        var length = Math.max(chat.getSideA(), chat.getSideB());
        var width = Math.min(chat.getSideA(), chat.getSideB());
        var square = (float) length * width / 10_000;
        float finalCalculation = 0;
        var additionalInfo = "";
        for (var filling : chat.getFillings()) {
            finalCalculation += fillingData.get(filling) * square;
        }

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

        return String.format("""
                Стоимость по установке клиентской работы,
                размерами %dсм на %dсм, составляет %d руб.
                Сюда входит работа мастера по установке: %s.
                """ + "\n" + additionalInfo,
                chat.getSideA(),
                chat.getSideB(),
                Math.round(finalCalculation),
                chat.getFillings().stream().map(fil -> {
                    var costPerSquare = fillingData.get(fil);
                    var actualCost = square * costPerSquare;
                    return fil + "(" + (int) actualCost + "руб)";
                }).collect(Collectors.joining(", "))) + "\n"
                + "Можно начать новый расчет, введя первую сторону \uD83D\uDE09";
    }

    private boolean isIntParsable(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String getSideOrder(Integer a, Integer b) {
        if (a == null && b == null) {
            return "Введите первую сторону в см  \uD83D\uDCCF";
        } else if (b == null) {
            return "Введите вторую сторону в см  \uD83D\uDCD0";
        }
        return "Видимо, все стороны введены \uD83E\uDD37\uD83C\uDFFC\u200D♂\uFE0F";
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
