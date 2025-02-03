package utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import schemas.Schemas;

import java.util.List;

public class Keyboards {

    public static ReplyKeyboardMarkup welcomeKb() {
        var alfa = new KeyboardButton(Schemas.alfa);
        var neo = new KeyboardButton(Schemas.neo);
        var lessons = new KeyboardButton(Schemas.lessons);
        var calc = new KeyboardButton(Schemas.calc);
        var rkb = ReplyKeyboardMarkup.builder()
//                .keyboardRow(new KeyboardRow(List.of(alfa, neo)))
//                .keyboardRow(new KeyboardRow(List.of(lessons, calc)))
                .keyboardRow(new KeyboardRow(List.of(alfa, calc)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    public static ReplyKeyboardMarkup alfaKb() {
        var info = new KeyboardButton(Schemas.info);
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(info, back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    public static ReplyKeyboardMarkup neoKb() {
        var info = new KeyboardButton(Schemas.info);
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(info, back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    //LESSONS
    public static ReplyKeyboardMarkup lessonsKb() {
        var backSide = new KeyboardButton(Schemas.backSide);
        var glass = new KeyboardButton(Schemas.glass);
        var passepartout = new KeyboardButton("паспарту");
        var frame = new KeyboardButton("рама");
        var canvasFrame = new KeyboardButton("подрамник");
        var embroidery = new KeyboardButton("вышивка");
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(frame, backSide)))
                .keyboardRow(new KeyboardRow(List.of(glass, passepartout)))
                .keyboardRow(new KeyboardRow(List.of(canvasFrame, embroidery)))
                .keyboardRow(new KeyboardRow(List.of(back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    public static ReplyKeyboardMarkup lessonKb() {
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    //CALCULATIONS
    public static ReplyKeyboardMarkup calculationsKb() {
        var strengthening = new KeyboardButton(Schemas.strength);
        var embroidery = new KeyboardButton(Schemas.embroidery);
        var doubleFrame = new KeyboardButton(Schemas.doubleFrame);
        var assembly = new KeyboardButton(Schemas.inserting);
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(strengthening, embroidery)))
                .keyboardRow(new KeyboardRow(List.of(doubleFrame, assembly)))
                .keyboardRow(new KeyboardRow(List.of(back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    public static ReplyKeyboardMarkup calcKb() {
        var info = new KeyboardButton(Schemas.info);
        var cancel = new KeyboardButton(Schemas.cancel);
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(info, cancel, back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }

    public static ReplyKeyboardMarkup calcFillingKb() {
        var foamFilling = new KeyboardButton(Schemas.foamFilling);
        var canvasFilling = new KeyboardButton(Schemas.canvasFilling);
        var glassFilling = new KeyboardButton(Schemas.glassFilling);
        var passFilling = new KeyboardButton(Schemas.passFilling);

        var cancel = new KeyboardButton(Schemas.cancel);
        var done = new KeyboardButton(Schemas.done);
        var back = new KeyboardButton(Schemas.back);
        var rkb = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(foamFilling, canvasFilling)))
                .keyboardRow(new KeyboardRow(List.of(glassFilling, passFilling)))
                .keyboardRow(new KeyboardRow(List.of(done, cancel, back)))
                .build();
        rkb.setResizeKeyboard(true);
        rkb.setOneTimeKeyboard(true);
        return rkb;
    }
}
