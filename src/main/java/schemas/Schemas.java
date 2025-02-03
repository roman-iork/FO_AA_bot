package schemas;

import lombok.Getter;
import schemas.base.*;
import schemas.calculations.DoubleFrameCalcSchema;
import schemas.calculations.EmbroideryCalcSchema;
import schemas.calculations.InsertingCalcSchema;
import schemas.calculations.StrengtheningCalcSchema;
//import schemas.lessons.BackSideLessonSchema;
//import schemas.lessons.GlassLessonSchema;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Schemas {
    private Map<String, Schema> schemas = new HashMap<>();

    //base
    public static final String start = "/start";
    public static final String alfa = "Альфаарт";
    public static final String neo = "Неоарт";
    public static final String lessons = "Узнать";
    public static final String calc = "Рассчитать";

    //util buttons
    public static final String back = "Назад";
    public static final String info = "Инфо";
    public static final String cancel = "Отмена";
    public static final String done = "Готово";

    //lessons
    public static final String backSide = "Задник";
    public static final String glass = "Стекло";

    //calculations
    //STRENGTHENING
    public static final String strength = "Усиление подрамника";
    //INSERTING
    public static final String inserting = "Установка работы";
    public static final String foamFilling = "пена/заник";
    public static final String canvasFilling = "холст";
    public static final String glassFilling = "стекло";
    public static final String passFilling = "паспарту";
    //EMBROIDERY
    public static final String embroidery = "Вышивка";
    //DOUBLE FRAME
    public static final String doubleFrame = "Двойная рама";




    public Schemas() {
        //base
        schemas.put(start, new WelcomeSchema());
        schemas.put(alfa , new AlfaSchema());
//        schemas.put(neo, new NeoSchema());
//        schemas.put(lessons, new LessonsSchema());
        schemas.put(calc, new CalculationsSchema());
        //lessons
//        schemas.put(backSide, new BackSideLessonSchema());
//        schemas.put(glass, new GlassLessonSchema());
        //calc
        schemas.put(strength, new StrengtheningCalcSchema());
        schemas.put(inserting, new InsertingCalcSchema());
        schemas.put(embroidery, new EmbroideryCalcSchema());
        schemas.put(doubleFrame, new DoubleFrameCalcSchema());
    }
}
