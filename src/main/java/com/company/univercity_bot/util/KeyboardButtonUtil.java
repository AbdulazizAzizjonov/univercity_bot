package com.company.univercity_bot.util;

import com.company.univercity_bot.model.EducationDegree;
import com.company.univercity_bot.model.EducationDirectory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class KeyboardButtonUtil {

    //ADMIN
    public static ReplyKeyboard startAdmin() {
        KeyboardButton universityInfo = new KeyboardButton("BIU Oliygohi haqida 2");
        KeyboardButton students = new KeyboardButton("Abiturientlarga \uD83C\uDF93");
        KeyboardButton registration = new KeyboardButton("Ro'yxatdan o'tganlar");
        KeyboardButton callCentre = new KeyboardButton("\uD83D\uDCDE Biz bilan bog'lanish");
        KeyboardButton information = new KeyboardButton("Fikr-mulohazalar");
        KeyboardButton EDUCATIBCRUD = new KeyboardButton("Yo'nalish CRUD");
        KeyboardButton SHARTNOMA = new KeyboardButton("SHARTNOMA CRUD");


        KeyboardRow row1 = getRow(universityInfo, students);
        KeyboardRow row2 = getRow(registration);
        KeyboardRow row3 = getRow(callCentre, information);
        KeyboardRow row4 = getRow(EDUCATIBCRUD, SHARTNOMA);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3, row4);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard EduType() {
        KeyboardButton kunduzgi = new KeyboardButton("Kunduzgi");
        KeyboardButton kechgi = new KeyboardButton("Kechgi");
        KeyboardButton sirtqi = new KeyboardButton("Sirtqi");
        KeyboardButton plus = new KeyboardButton("❌ Bekor qilish");


        KeyboardRow row1 = getRow(kunduzgi, kechgi);
        KeyboardRow row2 = getRow(sirtqi);
        KeyboardRow row3 = getRow(plus);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3);
        return getMarkup(rowList);

    }

    public static ReplyKeyboard HUDUDUZ() {
        KeyboardButton kunduzgi = new KeyboardButton("Toshkent shahri");
        KeyboardButton kechgi = new KeyboardButton("Namangan shahri");
        KeyboardButton sirtqi = new KeyboardButton("Sirdaryo viloyati");
        KeyboardButton ortga = new KeyboardButton("Buxoro viloyati");
        KeyboardButton plus = new KeyboardButton("❌ Bekor qilish");


        KeyboardRow row1 = getRow(kunduzgi, kechgi);
        KeyboardRow row2 = getRow(sirtqi, ortga);
        KeyboardRow row3 = getRow(plus);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard GENDERUZ() {
        KeyboardButton kunduzgi = new KeyboardButton("\uD83E\uDDD4 Erkak");
        KeyboardButton kechgi = new KeyboardButton("\uD83D\uDC69\u200D\uD83E\uDDB0 Ayol");
        KeyboardButton cancel = new KeyboardButton("❌ Bekor qilish");


        KeyboardRow row1 = getRow(kunduzgi, kechgi);
        KeyboardRow row2 = getRow(cancel);

        List<KeyboardRow> rowList = getRowList(row1, row2);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard ContactUz() {

        KeyboardButton contact = new KeyboardButton("⬆️Kontaktni jo'natish\uD83D\uDCF2");
        contact.setRequestContact(true);
        KeyboardButton cancel = new KeyboardButton("❌ Bekor qilish");


        KeyboardRow row1 = getRow(contact, cancel);

        List<KeyboardRow> rowList = getRowList(row1);
        return getMarkup(rowList);

    }

    public static ReplyKeyboard CancelAndBackUz() {


        KeyboardButton cancel = new KeyboardButton("❌ Bekor qilish");

        KeyboardRow row1 = getRow(cancel);

        List<KeyboardRow> rowList = getRowList(row1);
        return getMarkup(rowList);

    }

    public static ReplyKeyboard CancelAndCommitUz() {


        KeyboardButton send = new KeyboardButton("⬆️Yuborish");
        KeyboardButton cancel = new KeyboardButton("❌ Bekor qilish");
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");

        KeyboardRow row1 = getRow(send);
        KeyboardRow row2 = getRow(cancel, back);

        List<KeyboardRow> rowList = getRowList(row1, row2);
        return getMarkup(rowList);

    }


    public static ReplyKeyboardMarkup educationDegreeMarkup(List<EducationDegree> educationDegreeList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton plus = new KeyboardButton("Daraja Qo'shish");
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");
        KeyboardRow row = new KeyboardRow();
        row.add(plus);
        row.add(back);

        KeyboardRow row1 = new KeyboardRow();
        for (EducationDegree educationDegree : educationDegreeList) {
            KeyboardButton button = new KeyboardButton(educationDegree.getNameUz());
            row1.add(button);

        }
        keyboardRowList.add(row1);
        keyboardRowList.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup educationDegreeForUserMarkup(List<EducationDegree> educationDegreeList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton plus = new KeyboardButton("❌ Bekor qilish");


        KeyboardRow row = new KeyboardRow();
        row.add(plus);


        KeyboardRow row1 = new KeyboardRow();
        for (EducationDegree educationDegree : educationDegreeList) {
            KeyboardButton button = new KeyboardButton(educationDegree.getNameUz());
            row1.add(button);

        }
        keyboardRowList.add(row1);
        keyboardRowList.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup educationBakalavrDirectoryMarkup(List<EducationDirectory> educationDirectoryList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton plus = new KeyboardButton("Bakalavrga Yo'nalish Qo'shish");
        KeyboardButton minus = new KeyboardButton("Bakalavr Yo'nalishini O'chirish");
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row.add(plus);
        row.add(minus);
        row3.add(back);

        KeyboardRow row1 = new KeyboardRow();
        int count = 0;
        for (EducationDirectory educationDirectory : educationDirectoryList) {
            KeyboardButton button = new KeyboardButton(educationDirectory.getNameUZ());
            if (count == 2) {
                keyboardRowList.add(row1);
                row1 = new KeyboardRow();
                count = 0;
            }
            row1.add(button);
            count++;
        }
        if (educationDirectoryList.size() % 2== 1){
            EducationDirectory directory = educationDirectoryList.get(educationDirectoryList.size() -1);
            KeyboardButton button = new KeyboardButton(directory.getNameUZ());
            KeyboardRow row2 = new KeyboardRow();
            row2.add(button);
            keyboardRowList.add(row2);
        }

        keyboardRowList.add(row);
        keyboardRowList.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup educationBakalavrShartnomaDirectoryMarkup(List<EducationDirectory> educationDirectoryList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row3.add(back);

        KeyboardRow row1 = new KeyboardRow();
        int count = 0;
        for (EducationDirectory educationDirectory : educationDirectoryList) {
            KeyboardButton button = new KeyboardButton(educationDirectory.getNameUZ());
            if (count == 2) {
                keyboardRowList.add(row1);
                row1 = new KeyboardRow();
                count = 0;
            }
            row1.add(button);
            count++;
        }
        if (educationDirectoryList.size() % 2== 1){
            EducationDirectory directory = educationDirectoryList.get(educationDirectoryList.size() -1);
            KeyboardButton button = new KeyboardButton(directory.getNameUZ());
            KeyboardRow row2 = new KeyboardRow();
            row2.add(button);
            keyboardRowList.add(row2);
        }

        keyboardRowList.add(row);
        keyboardRowList.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup educationBakalavrDirectoryDeleteMarkup(List<EducationDirectory> educationDirectoryList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");
        KeyboardRow row = new KeyboardRow();

        row.add(back);

        KeyboardRow row1 = new KeyboardRow();
        int count = 0;
        for (EducationDirectory educationDirectory : educationDirectoryList) {
            KeyboardButton button = new KeyboardButton(educationDirectory.getNameUZ());
            if (count == 2) {
                keyboardRowList.add(row1);
                row1 = new KeyboardRow();
                row1.add(button);
                count = 0;
            }
            row1.add(button);
            count++;
        }
        if (educationDirectoryList.size() % 2== 1){
            EducationDirectory directory = educationDirectoryList.get(educationDirectoryList.size() -1);
            KeyboardButton button = new KeyboardButton(directory.getNameUZ());
            KeyboardRow row2 = new KeyboardRow();
            row2.add(button);
            keyboardRowList.add(row2);
        }

        keyboardRowList.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup educationBakalavrDirectoryForUserMarkup(List<EducationDirectory> educationDirectoryList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton plus = new KeyboardButton("❌ Bekor qilish");

        KeyboardRow row = new KeyboardRow();
        row.add(plus);


        KeyboardRow row1 = new KeyboardRow();
        int count = 0;
        for (EducationDirectory educationDirectory : educationDirectoryList) {
            KeyboardButton button = new KeyboardButton(educationDirectory.getNameUZ());
            if (count == 2) {
                count = 0;
                keyboardRowList.add(row1);
                row1 = new KeyboardRow();
            }
            row1.add(button);
            count++;
        }

        if (educationDirectoryList.size() % 2== 1){
            EducationDirectory directory = educationDirectoryList.get(educationDirectoryList.size() -1);
            KeyboardButton button = new KeyboardButton(directory.getNameUZ());
            KeyboardRow row2 = new KeyboardRow();
            row2.add(button);
            keyboardRowList.add(row2);
        }

        keyboardRowList.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup educationMagistraturaDirectoryMarkup(List<EducationDirectory> educationDirectoryList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton plus = new KeyboardButton("Magistraturaga Yo'nalish Qo'shish");
        KeyboardButton minus = new KeyboardButton("Magistratura Yo'nalishini O'chirish");
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row.add(plus);
        row.add(minus);
        row3.add(back);


        KeyboardRow row1 = new KeyboardRow();
        int count = 0;
        for (EducationDirectory educationDirectory : educationDirectoryList) {
            KeyboardButton button = new KeyboardButton(educationDirectory.getNameUZ());
            if (count == 2) {
                keyboardRowList.add(row1);
                row1 = new KeyboardRow();
                count = 0;
            }
            row1.add(button);
            count++;
        }
        if (educationDirectoryList.size() % 2== 1){
            EducationDirectory directory = educationDirectoryList.get(educationDirectoryList.size() -1);
            KeyboardButton button = new KeyboardButton(directory.getNameUZ());
            KeyboardRow row2 = new KeyboardRow();
            row2.add(button);
            keyboardRowList.add(row2);
        }

        keyboardRowList.add(row);
        keyboardRowList.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }



    public static ReplyKeyboardMarkup educationMagistraturaDirectoryDeleteMarkup(List<EducationDirectory> educationDirectoryList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardButton back = new KeyboardButton("⬅ Ortga qaytish");
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row3.add(back);


        KeyboardRow row1 = new KeyboardRow();
        int count = 0;
        for (EducationDirectory educationDirectory : educationDirectoryList) {
            KeyboardButton button = new KeyboardButton(educationDirectory.getNameUZ());
            if (count == 2) {
                keyboardRowList.add(row1);
                row1 = new KeyboardRow();
                count = 0;
            }
            row1.add(button);
            count++;
        }
        if (educationDirectoryList.size() % 2== 1){
            EducationDirectory directory = educationDirectoryList.get(educationDirectoryList.size() -1);
            KeyboardButton button = new KeyboardButton(directory.getNameUZ());
            KeyboardRow row2 = new KeyboardRow();
            row2.add(button);
            keyboardRowList.add(row2);
        }

        keyboardRowList.add(row);
        keyboardRowList.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


    //USER

    public static ReplyKeyboard startUserUz() {
        KeyboardButton universityInfo = new KeyboardButton("BIU Oliygohi haqida");
        KeyboardButton students = new KeyboardButton("Abiturientlarga \uD83C\uDF93");
        KeyboardButton registration = new KeyboardButton("Ro'yxatdan o'tish\uD83D\uDCDD\uD83C\uDF93");
        KeyboardButton shartnoma = new KeyboardButton("Shartnoma olish");
        KeyboardButton callCentre = new KeyboardButton("\uD83D\uDCDE Biz bilan bog'lanish");
        KeyboardButton information = new KeyboardButton("Fikr-mulohaza");
        KeyboardButton changeLanguage = new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA/\uD83C\uDDFA\uD83C\uDDFF Til");

        KeyboardRow row1 = getRow(universityInfo, students);
        KeyboardRow row2 = getRow(registration);
        KeyboardRow row3 = getRow(callCentre, information);
        KeyboardRow row4 = getRow(shartnoma);
        KeyboardRow row5 = getRow(changeLanguage);


        List<KeyboardRow> rowList = getRowList(row1, row2, row3, row4, row5);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard startUserRu() {
        KeyboardButton universityInfo = new KeyboardButton("BIU Oliygohi haqida(ruscha)");
        KeyboardButton students = new KeyboardButton("Abiturientlarga \uD83C\uDF93 (ruscha)");
        KeyboardButton registration = new KeyboardButton("Ro'yxatdan o'tish\uD83D\uDCDD\uD83C\uDF93 (ruscha)");
        KeyboardButton callCentre = new KeyboardButton("\uD83D\uDCDE Biz bilan bog'lanish (ruscha)");
        KeyboardButton information = new KeyboardButton("Fikr-mulohaza (ruscha)");
        KeyboardButton changeLanguage = new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA/\uD83C\uDDFA\uD83C\uDDFF Язык (ruscha)");

        KeyboardRow row1 = getRow(universityInfo, students);
        KeyboardRow row2 = getRow(registration);
        KeyboardRow row3 = getRow(callCentre, information);
        KeyboardRow row4 = getRow(changeLanguage);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3, row4);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard changeLanguageRu() {
        KeyboardButton uz = new KeyboardButton("Танланди: \uD83C\uDDFA\uD83C\uDDFF O'zbekcha");
        KeyboardButton ru = new KeyboardButton("Выбрано: \uD83C\uDDF7\uD83C\uDDFA Русский");
        KeyboardButton back = new KeyboardButton("⬅️ Назад");

        KeyboardRow row1 = getRow(uz, ru);
        KeyboardRow row2 = getRow(back);

        List<KeyboardRow> rowList = getRowList(row1, row2);
        return getMarkup(rowList);

    }

    public static ReplyKeyboard changeLanguageUz() {
        KeyboardButton uz = new KeyboardButton("Танланди: \uD83C\uDDFA\uD83C\uDDFF O'zbekcha");
        KeyboardButton ru = new KeyboardButton("Выбрано: \uD83C\uDDF7\uD83C\uDDFA Русский");
        KeyboardButton back = new KeyboardButton("⬅️ Orqaga");

        KeyboardRow row1 = getRow(uz, ru);
        KeyboardRow row2 = getRow(back);

        List<KeyboardRow> rowList = getRowList(row1, row2);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard EduDegreeUz() {
        KeyboardButton uz = new KeyboardButton("Ta'lim shaklari va shartnoma narxlari");
        KeyboardButton ru = new KeyboardButton("Magistratura");
        KeyboardButton back = new KeyboardButton("⏪ Ortga qaytish");
        KeyboardRow row1 = getRow(uz);
        KeyboardRow row2 = getRow(ru);
        KeyboardRow row3 = getRow(back);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard EduDegreeRu() {
        KeyboardButton uz = new KeyboardButton("Формы обучения и контрактные цены");
        KeyboardButton ru = new KeyboardButton("Mагистра");
        KeyboardButton back = new KeyboardButton("⬅️ Назад");

        KeyboardRow row1 = getRow(uz);
        KeyboardRow row2 = getRow(ru);
        KeyboardRow row3 = getRow(back);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3);
        return getMarkup(rowList);

    }


    public static ReplyKeyboard EduTypeRu() {
        KeyboardButton kunduzgi = new KeyboardButton("Kunduzgi ru");
        KeyboardButton kechgi = new KeyboardButton("Kechgi ru");
        KeyboardButton sirtqi = new KeyboardButton("Sirtqi ru");
        KeyboardButton ortga = new KeyboardButton("⏪ Ortga qaytish");


        KeyboardRow row1 = getRow(kunduzgi, kechgi);
        KeyboardRow row2 = getRow(sirtqi);
        KeyboardRow row3 = getRow(ortga);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3);
        return getMarkup(rowList);

    }

    public static ReplyKeyboard EduTypeUz() {
        KeyboardButton kunduzgi = new KeyboardButton("Kunduzgi");
        KeyboardButton kechgi = new KeyboardButton("Kechgi");
        KeyboardButton sirtqi = new KeyboardButton("Sirtqi");
        KeyboardButton ortga = new KeyboardButton("⏪ Ortga qaytish");


        KeyboardRow row1 = getRow(kunduzgi, kechgi);
        KeyboardRow row2 = getRow(sirtqi);
        KeyboardRow row3 = getRow(ortga);

        List<KeyboardRow> rowList = getRowList(row1, row2, row3);
        return getMarkup(rowList);

    }


    private static KeyboardRow getRow(KeyboardButton... buttons) {
        return new KeyboardRow(Arrays.asList(buttons));
    }

    private static List<KeyboardRow> getRowList(KeyboardRow... rows) {
        return Arrays.asList(rows);
    }

    private static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> keyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
