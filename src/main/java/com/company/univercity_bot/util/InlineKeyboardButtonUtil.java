package com.company.univercity_bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

public class InlineKeyboardButtonUtil {

    public static InlineKeyboardMarkup BIUInfo () {

        InlineKeyboardButton infoCreate = getButton("Ma'lumot qo'shish", "info_create" );
        InlineKeyboardButton infoUpdate = getButton("Ma'lumot tahrirlash", "info_update" );
        InlineKeyboardButton infoDelete = getButton("Ma'lumotni o'chirish", "info_delete" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_BIUInfo" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup BIU_Univercity () {

        InlineKeyboardButton infoCreate = getButton("Ma'lumot qo'shish", "biu_plus" );
        InlineKeyboardButton infoUpdate = getButton("Ma'lumot tahrirlash", "biu_update" );
        InlineKeyboardButton infoDelete = getButton("Ma'lumotni o'chirish", "biu_delete" );
        InlineKeyboardButton litsenziyaCreate = getButton("Litsenziya qo'shish", "litsenziya_create" );
        InlineKeyboardButton litsenziyaUpdate = getButton("Litsenziya o'chirish", "litsenziya_delete" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_BIU" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(litsenziyaCreate, litsenziyaUpdate);
        List<InlineKeyboardButton> row3 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup SHARTNOMACRUD () {

        InlineKeyboardButton infoCreate = getButton("Shartnoma qo'shish", "shartnoma_create" );
        InlineKeyboardButton infoUpdate = getButton("Shartnoma o'chirish", "shartnoma_delete" );
        InlineKeyboardButton infoDelete = getButton("Shartnomalarni ko'rish", "shartnoma_show" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_shartnomacrud" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup BIU_Univercity_update () {

        InlineKeyboardButton infoCreate = getButton("Ma'lumot Uz", "biu_infoUz_update" );
        InlineKeyboardButton infoUpdate = getButton("Ma'lumot Ru", "biu_infoRu_update" );
        InlineKeyboardButton infoDelete = getButton("Rasm", "biu_image_update" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "from_BIU_Univercity_update" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup ContactConnectionCRUD () {

        InlineKeyboardButton enter_info = getButton("Ma'lumot kiritish", "enter_info_contact" );
        InlineKeyboardButton edit_info = getButton("Ma'lumot tahrirlash", "edit_info_contact" );
        InlineKeyboardButton delete_info = getButton("Ma'lumot o'chirish", "delete_info_contact" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_ContactConnectionCrud" );

        List<InlineKeyboardButton> row1 = getRow(enter_info, edit_info);
        List<InlineKeyboardButton> row2 = getRow(delete_info, back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup ContactConnectionUpdate () {

        InlineKeyboardButton manzilUz = getButton("Manzil Uz", "manzil_uz_update_contact_connection" );
        InlineKeyboardButton manzilRu = getButton("Manzil Ru", "manzil_ru_update_contact_connection" );
        InlineKeyboardButton contact = getButton("Telefon raqam", "contact_update_contact_connection" );
        InlineKeyboardButton image = getButton("Rasm", "rasm_update_contact_connection" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "from_ContactConnectionUpdate" );

        List<InlineKeyboardButton> row1 = getRow(manzilUz, manzilRu);
        List<InlineKeyboardButton> row2 = getRow(contact, image);
        List<InlineKeyboardButton> row3 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup AbiturientlarCRUD () {

        InlineKeyboardButton enter_info = getButton("Ma'lumot kiritish", "enter_info_abiturient" );
        InlineKeyboardButton edit_info = getButton("Ma'lumot tahrirlash", "edit_info_abiturient" );
        InlineKeyboardButton delete_info = getButton("Ma'lumot o'chirish", "delete_info_abiturient" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_AbiturientlarCRUD" );

        List<InlineKeyboardButton> row1 = getRow(enter_info, edit_info);
        List<InlineKeyboardButton> row2 = getRow(delete_info, back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup AbiturientlarDegree () {

        InlineKeyboardButton bakalavr = getButton("Talim yo'nalishlari va narxlari", "bakalavr_create" );
        InlineKeyboardButton magistratura = getButton("Magistratura", "magistratura_create" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_AbiturientlarDegree" );

        List<InlineKeyboardButton> row1 = getRow(bakalavr,magistratura);
        List<InlineKeyboardButton> row2 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup AbiturientlarDegreeEdit () {

        InlineKeyboardButton bakalavr = getButton("Talim yo'nalishlari va narxlari", "bakalavr_edit" );
        InlineKeyboardButton magistratura = getButton("Magistratura", "magistratura_edit" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_AbiturientlarDegreeEdit" );

        List<InlineKeyboardButton> row1 = getRow(bakalavr,magistratura);
        List<InlineKeyboardButton> row2 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup Replay () {

        InlineKeyboardButton enter_info = getButton("Shartnoma yuborish", "replay" );

        List<InlineKeyboardButton> row1 = getRow(enter_info);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup AbiturientlarDegreeDelete () {

        InlineKeyboardButton bakalavr = getButton("Talim yo'nalishlari va narxlari", "bakalavr_delete" );
        InlineKeyboardButton magistratura = getButton("Magistratura", "magistratura_delete" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_AbiturientlarDegreeDelete" );

        List<InlineKeyboardButton> row1 = getRow(bakalavr,magistratura);
        List<InlineKeyboardButton> row2 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup EduTypeCreate () {

        InlineKeyboardButton kunduzgi = getButton("Kunduzgi", "kunduzgi_create" );
        InlineKeyboardButton kechgi = getButton("Kechgi", "kechgi_create" );
        InlineKeyboardButton sirtqi = getButton("Sirtqi", "sirtqi_create" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_edutypeCreate" );

        List<InlineKeyboardButton> row1 = getRow(kunduzgi,kechgi);
        List<InlineKeyboardButton> row2 = getRow(sirtqi);
        List<InlineKeyboardButton> row3 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup EduTypeDelete () {

        InlineKeyboardButton kunduzgi = getButton("Kunduzgi", "kunduzgi_delete" );
        InlineKeyboardButton kechgi = getButton("Kechgi", "kechgi_delete" );
        InlineKeyboardButton sirtqi = getButton("Sirtqi", "sirtqi_delete" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_edutypeDelete" );

        List<InlineKeyboardButton> row1 = getRow(kunduzgi,kechgi);
        List<InlineKeyboardButton> row2 = getRow(sirtqi);
        List<InlineKeyboardButton> row3 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup EduTypeEdit () {

        InlineKeyboardButton kunduzgi = getButton("Kunduzgi", "kunduzgi_edit" );
        InlineKeyboardButton kechgi = getButton("Kechgi", "kechgi_edit" );
        InlineKeyboardButton sirtqi = getButton("Sirtqi", "sirtqi_edit" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_edutypeEdit" );

        List<InlineKeyboardButton> row1 = getRow(kunduzgi,kechgi);
        List<InlineKeyboardButton> row2 = getRow(sirtqi);
        List<InlineKeyboardButton> row3 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup editDirectory () {

        InlineKeyboardButton infoCreate = getButton("Ma'lumot Uz", "infoUz_update" );
        InlineKeyboardButton infoUpdate = getButton("Ma'lumot Ru", "infoRu_update" );
        InlineKeyboardButton infoDelete = getButton("Rasm", "rasm_update" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "from_editDirectory" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup editDirectoryKechgi () {

        InlineKeyboardButton infoCreate = getButton("Info Uz", "infoUz_update_kechgi" );
        InlineKeyboardButton infoUpdate = getButton("Info Ru", "infoRu_update_kechgi" );
        InlineKeyboardButton infoDelete = getButton("Rasm", "rasm_update_kechgi" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "from_editDirectoryKechgi" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup editDirectorySirtqi () {

        InlineKeyboardButton infoCreate = getButton("Info Uz", "infoUz_update_sirtqi" );
        InlineKeyboardButton infoUpdate = getButton("Info Ru", "infoRu_update_sirtqi" );
        InlineKeyboardButton infoDelete = getButton("Rasm", "rasm_update_sirtqi" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "from_editDirectorySirtqi" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete);
        List<InlineKeyboardButton> row2 = getRow(back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup editDirectoryMagistratura () {

        InlineKeyboardButton infoCreate = getButton("Ma'lumot Uz", "infoUz_update_magistratura" );
        InlineKeyboardButton infoUpdate = getButton("Ma'lumot Ru", "infoRu_update_magistratura" );
        InlineKeyboardButton infoDelete = getButton("Rasm", "rasm_update_magistratura" );
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "from_editDirectoryMagistratura" );

        List<InlineKeyboardButton> row = getRow(infoCreate, infoUpdate);
        List<InlineKeyboardButton> row1 = getRow(infoDelete, back);
        List<List<InlineKeyboardButton>> rowList = getRowList(row, row1);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup TypingForRequest () {

        InlineKeyboardButton infoCreate = getButton("Xa", "typingforrequest_yes" );
        InlineKeyboardButton infoUpdate = getButton("Yo'q", "typingforrequest_not" );

        List<InlineKeyboardButton> row1 = getRow(infoCreate);
        List<InlineKeyboardButton> row2 = getRow(infoUpdate);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup MediaForRequest () {

        InlineKeyboardButton media_yes = getButton("Xa", "mediaforrequest_yes" );
        InlineKeyboardButton media_not = getButton("Yo'q", "mediaforrequest_not" );

        List<InlineKeyboardButton> row1 = getRow(media_yes);
        List<InlineKeyboardButton> row2 = getRow(media_not);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup ButtonForRequest () {

        InlineKeyboardButton media_yes = getButton("Xa", "buttonforrequest_yes" );
        InlineKeyboardButton media_not = getButton("Yo'q", "buttonforrequest_not" );

        List<InlineKeyboardButton> row1 = getRow(media_yes);
        List<InlineKeyboardButton> row2 = getRow(media_not);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }



    public static InlineKeyboardMarkup AloqaUz() {

        InlineKeyboardButton Instagram = new InlineKeyboardButton();
        InlineKeyboardButton Telegram = new InlineKeyboardButton();
        InlineKeyboardButton Manzil = new InlineKeyboardButton();

        Instagram.setText("Instagram");
        Telegram.setText("Telegram");
        Manzil.setText("Manzil \uD83D\uDCCD");


        Instagram.setUrl("https://instagram.com/ubs.uz?igshid=YmMyMTA2M2Y=");
        Telegram.setUrl("https://t.me/buhoro_innovatsiyalar_u_bott");
        Manzil.setUrl("https://www.google.com/maps/place/TDTU/@41.3525193,69.1366983,12z/data=!4m18!1m12!4m11!1m3!2m2!1d69.2082811!2d41.353679!1m6!1m2!1s0x38ae8c3da3c6e5c3:0x9882f2a6b7329d1d!2s2+Universitet+Ko'chasi,+Tashkent!2m2!1d69.2067382!2d41.3525405!3m4!1s0x38ae8c3da3c6e5c3:0x9882f2a6b7329d1d!8m2!3d41.3525405!4d69.2067382");
        List<InlineKeyboardButton> row1 = getRow(Instagram, Telegram);
        List<InlineKeyboardButton> row3 = getRow(Manzil);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row3);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup AloqaRu() {

        InlineKeyboardButton Instagram = new InlineKeyboardButton();
        InlineKeyboardButton Telegram = new InlineKeyboardButton();
        InlineKeyboardButton Manzil = new InlineKeyboardButton();


        Instagram.setText("Инстаграм");
        Telegram.setText("Телеграм");
        Manzil.setText("Адрес \uD83D\uDCCD");

        Instagram.setUrl("https://instagram.com/ubs.uz?igshid=YmMyMTA2M2Y=");
        Telegram.setUrl("https://t.me/buhoro_innovatsiyalar_u_bott");
        Manzil.setUrl("https://www.google.com/maps/place/TDTU/@41.3525193,69.1366983,12z/data=!4m18!1m12!4m11!1m3!2m2!1d69.2082811!2d41.353679!1m6!1m2!1s0x38ae8c3da3c6e5c3:0x9882f2a6b7329d1d!2s2+Universitet+Ko'chasi,+Tashkent!2m2!1d69.2067382!2d41.3525405!3m4!1s0x38ae8c3da3c6e5c3:0x9882f2a6b7329d1d!8m2!3d41.3525405!4d69.2067382");
        List<InlineKeyboardButton> row1 = getRow(Instagram, Telegram);
        List<InlineKeyboardButton> row3 = getRow(Manzil);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup CommitAndCancelContactConnection() {

        InlineKeyboardButton commit = getButton("Ha", "add_contact_commit");
        InlineKeyboardButton cancel = getButton("Yo'q", "add_contact_cancel");

        return new InlineKeyboardMarkup(getRowList(getRow(commit, cancel)));
    }

    public static InlineKeyboardMarkup CommitAndCancelDirectoryInfo() {

        InlineKeyboardButton commit = getButton("Ha", "add_directory_commit");
        InlineKeyboardButton cancel = getButton("Yo'q", "add_directory_cancel");

        return new InlineKeyboardMarkup(getRowList(getRow(commit, cancel)));
    }

    public static InlineKeyboardMarkup MessageCommitAndCancelBIU() {

        InlineKeyboardButton commit = getButton("Ha", "add_message_biu_commit");
        InlineKeyboardButton cancel = getButton("Yo'q", "add_message_biu_cancel");

        return new InlineKeyboardMarkup(getRowList(getRow(commit, cancel)));
    }


    private static InlineKeyboardButton getButton(String demo, String data) {
        org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton button = new org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton(demo);
        button.setCallbackData(data);
        return button;
    }

    private static List<InlineKeyboardButton> getRow(org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton... buttons) {
        return Arrays.asList(buttons);
    }


    private static List<List<InlineKeyboardButton>> getRowList(List<org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton>... rows) {
        return Arrays.asList(rows);
    }

}
