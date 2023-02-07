package com.company.univercity_bot.controller;

import com.company.univercity_bot.container.CompanantContainer;
import com.company.univercity_bot.enums.AdminStatus;
import com.company.univercity_bot.enums.LanguacheEnums;
import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.enums.UserStatus;
import com.company.univercity_bot.model.*;
import com.company.univercity_bot.repository.*;
import com.company.univercity_bot.service.*;
import com.company.univercity_bot.telegramBot.UnivercityBot;
import com.company.univercity_bot.util.InlineKeyboardButtonUtil;
import com.company.univercity_bot.util.KeyboardButtonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
public class UserController {

    @Autowired
    @Lazy
    private UnivercityBot univercityBot;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ContactConnectionService contactConnectionService;
    @Autowired
    private final BiuUnivercityService biuUnivercityService;
    @Autowired
    private final DirectoryInfoService directoryInfoService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EducationDirectoryRepository educationDirectoryRepository;
    @Autowired
    private final EducationDegreeRepository educationDegreeRepository;
    @Autowired
    private final EducationDirectoryService educationDirectoryService;
    @Autowired
    private final EducationDegreeService educationDegreeService;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final ShartnomaInfoService shartnomaInfoService;
    @Autowired
    private final ShartnomaInfoRepository shartnomaInfoRepository;
    @Autowired
    private final OrderShartnomaRepository orderShartnomaRepository;
    @Autowired
    private final OrderShartnomaService orderShartnomaService;

    @Autowired
    private StudentService studentService;

    public void handleMessage(User user, Message message) {
        String chatId = String.valueOf(message.getChatId());

        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasPhoto()) {
            handlePhoto(user, message);
        } else if (message.hasContact()) {
            handleContact(user, message, chatId);
        }
    }

    private void handlePhoto(User user, Message message) {


        List<PhotoSize> photoSizeList = message.getPhoto();
        String chatId = String.valueOf(message.getChatId());


        if (CompanantContainer.studentStepMap.containsKey(chatId)) {
            Student student = CompanantContainer.studentMap.get(chatId);
            if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.DIPLOM_ATTESTAT_RASMI)) {
                SendMessage sendMessage = new SendMessage();
                if (photoSizeList.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setPImage(photoSizeList.get(0).getFileId());
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.RASMINI_YUBORVOTDI);

                    sendMessage.setChatId(chatId);
                    sendMessage.setText("3x4 suratingizni jo'nating.");


                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    univercityBot.sendMsg(sendMessage);
                }
            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.RASMINI_YUBORVOTDI)) {
                SendMessage sendMessage = new SendMessage();
                if (photoSizeList.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setImage(photoSizeList.get(0).getFileId());

                    SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(student.getImage()));
                    sendPhoto.setChatId(chatId);
                    univercityBot.sendMsg(sendPhoto);

                    sendMessage.setText(String.format("Ta'lim darajasi:  %s\n\n" +
                                    "Ta'lim yo'nalishi:  %s\n\n" +
                                    "Ta'lim shakli:  %s\n\n" +
                                    "Universitet hududi:  %s\n\n" +
                                    "Jinsi:  %s\n\n" +
                                    "F" + "." + "I" + "." + "SH:  %s\n\n" +
                                    "Tug'ilgan sanasi:   %s \n\n" +
                                    "Passport seria va raqam:   %s\n\n" +
                                    "Passport berilgan sana:   %s\n\n" +
                                    "Telefon raqami:   %s\n\n" +
                                    "Ota/ona telefon raqami:   %s\n\n" +
                                    "Yashash manzili:   %s\n\n" +
                                    "Oxirgi ta'lim olgan muasassangiz:  %s\n\n",
                            student.getEducationDegree(), student.getEducationDirectory().getNameUZ(),
                            student.getEducationType(), student.getEdu_location(),
                            student.getGender(), student.getName(),
                            student.getBirthday(), student.getPassportNumber(),
                            student.getTakePassportDay(), student.getPhone(),
                            student.getFathPhone(), student.getManzil(), student.getSchoolType()));

                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    SendMessage sendMessage1 = new SendMessage(
                            chatId, "Ma'lumotlaringizni tasdiqlaysizmi"
                    );
                    sendMessage1.setReplyMarkup(KeyboardButtonUtil.CancelAndCommitUz());
                    sendMessage1.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage1);
                }
            }
        }
    }

    private void handleText(User user, Message message) {

        SendMessage sendMessage = new SendMessage();
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        Long userId = Long.valueOf(String.valueOf(message.getChatId()));
        Contact contact = message.getContact();

        Users optional = userService.getUsersById(userId);
        if (!CompanantContainer.userDeails.containsKey(userId) && optional != null) {
            CompanantContainer.userDeails.put(userId, optional);
        }

        if (text.equals("/start")) {
            if (optional == null) {

                Users users = new Users();
                users.setId(user.getId());
                users.setFirstName(user.getFirstName());
                users.setLastName(user.getLastName());
                users.setUserName(user.getUserName());
                users.setAdmin(false);
                users.setUser(true);
                users.setEmployee(false);
                userService.saveUser(users);

                sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                univercityBot.sendMsg(sendMessage);
                CompanantContainer.UserStepMap.put(chatId, UserStatus.START_BOSDI);

            } else {

                CompanantContainer.studentStepMap.remove(chatId);
                CompanantContainer.studentStepMap.remove(chatId);

                Users users = CompanantContainer.userDeails.get(userId);
                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                } else {
                    sendMessage.setText("Этот бот создан для знакомства с BUXORO INNOVATSIYALAR UNIVERSITETI и регистрации абитуриентов!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserRu());
                }
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                CompanantContainer.UserStepMap.put(chatId, UserStatus.ABITURIENTLARGA_UZ);
            }

        } else if (text.equals("⬆️Yuborish")) {

            Student student = CompanantContainer.studentMap.get(chatId);
            studentService.saveStudent(student);

            sendMessage.setText("So'rovingiz muvaffaqiyatli to'ldirildi!");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
            univercityBot.sendMsg(sendMessage);

        } else if (CompanantContainer.studentStepMap.containsKey(chatId)) {

            Student student = CompanantContainer.studentMap.get(chatId);

            if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TALIM_DARAJASINI_KIRITIDI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setEducationDegree(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.TALIM_YONALISHINI_KIRITIDI);

                    sendMessage.setText("Ta'lim yo'nalishini tanlang.");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.educationBakalavrDirectoryForUserMarkup(getDirectoryList()));
                    univercityBot.sendMsg(sendMessage);
                }
            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TALIM_YONALISHINI_KIRITIDI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    EducationDirectory educationDirectoryName = educationDirectoryRepository.findByNameUZAndVisibleTrue(text);

                    EducationDirectory educationDirectory = educationDirectoryRepository.findByDirectoryIdAndVisibleTrue(educationDirectoryName.getDirectoryId());

                    EducationDirectory educationDirectory1 = educationDirectoryRepository.findByNameUZ(educationDirectory.getNameUZ());

                    sendMessage.setText(educationDirectoryName.getInfoUZ());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);

                    student.setEducationDirectory(educationDirectory1);

                    CompanantContainer.studentStepMap.put(chatId, UserStatus.TALIM_TURINI_KIRITIDI);

                    sendMessage.setText("Ta'lim shaklini tanlang.");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.EduType());
                    univercityBot.sendMsg(sendMessage);
                }
            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TALIM_TURINI_KIRITIDI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setEducationType(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.HUDUDNI_TANLADI);

                    sendMessage.setText("Imtihon bo'ladigan hududni tanlang.");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.HUDUDUZ());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }
            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.HUDUDNI_TANLADI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setEdu_location(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.JINSI_TANLADI);

                    sendMessage.setText("Jinsingizni tanlang.");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.GENDERUZ());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.JINSI_TANLADI)) {

                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setGender(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.ISMNI_KIRITDI);

                    sendMessage.setText("\uD83D\uDC64Toʻliq F.I.Sh ingizni kiriting\n" +
                            "(boshlanishi: Ivanov Ivan Ivanovich)");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.ISMNI_KIRITDI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else if (text.length() <= 3) {
                    sendMessage.setText("Noto'g'ri ma'lumot kiritildi  ❗️\nIltimos tekshirib qaytadan kiritb ko`ring.");
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);

                } else {
                    student.setName(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.TUGILGAN_KUNINI_KIRITIDI);
                    sendMessage.setText("\uD83D\uDCC5 Tug'ilgan kuningizni kiriting (01.01.2000)");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TUGILGAN_KUNINI_KIRITIDI)) {

                try {
                    String[] split = text.split("\\.");

                    int day = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int year = Integer.parseInt(split[2]);

                    LocalDate localDate = LocalDate.of(year, month, day);

                    if (text.equals("❌ Bekor qilish")) {
                        sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                        sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                        sendMessage.setChatId(chatId);
                        univercityBot.sendMsg(sendMessage);
                        CompanantContainer.studentStepMap.remove(chatId);

                    } else if (localDate.isBefore(LocalDate.now())) {
                        student.setBirthday(text);
                        CompanantContainer.studentStepMap.put(chatId, UserStatus.PASSPORT_RAQAMINI_KIRITIDI);

                        sendMessage.setText("Pasport seria va raqamingizni kiriting (AA XXXXXXX)");
                        sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                        sendMessage.setChatId(chatId);
                        univercityBot.sendMsg(sendMessage);

                    } else {
                        sendMessage.setText("Hozirgi sanadan oldingi sana kiritilishi kerak ❗");
                    }

                } catch (Exception e) {
                    sendMessage.setText("Sana kiritilishida xatolik ❗\nQaytadan urinib ko`ring.");
                }

            }
            if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.PASSPORT_RAQAMINI_KIRITIDI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else if (Pattern.matches("[A-Z]{2}[0-9]{7}", text)) {
                    student.setPassportNumber(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.PASSPORT_BERILGAN_SANANI_KIRITDI);

                    sendMessage.setText("Pasport berilgan sanani kiriting:\n" +
                            "(misol: 01.01.2000)");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                } else {
                    sendMessage.setText("Noto'g'ri ma'lumot kiritildi  ❗️\nIltimos tekshirib qaytadan kiritb ko`ring.");
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.PASSPORT_BERILGAN_SANANI_KIRITDI)) {

                try {
                    String[] split = text.split("\\.");

                    int day = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int year = Integer.parseInt(split[2]);

                    LocalDate localDate = LocalDate.of(year, month, day);


                    if (text.equals("❌ Bekor qilish")) {
                        sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                        sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                        sendMessage.setChatId(chatId);
                        univercityBot.sendMsg(sendMessage);
                        CompanantContainer.studentStepMap.remove(chatId);

                    } else if (localDate.isBefore(LocalDate.now())) {

                        student.setTakePassportDay(text);
                        CompanantContainer.studentStepMap.put(chatId, UserStatus.TELEFON_RAQAMINI_KIRITIDI);
                        sendMessage.setText("📞 Telefon raqamini kiriting: (+998XXXXXXXXX)");
                        sendMessage.setReplyMarkup(KeyboardButtonUtil.ContactUz());
                        sendMessage.setChatId(chatId);
                        univercityBot.sendMsg(sendMessage);

                    } else {
                        sendMessage.setText("Hozirgi sanadan oldingi sana kiritilishi kerak ❗");
                    }
                } catch (Exception e) {
                    sendMessage.setText("Sana kiritilishida xatolik ❗\nQaytadan urinib ko`ring.");
                }

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TELEFON_RAQAMINI_KIRITIDI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else if (Pattern.matches("[+]998[0-9][0-9]{8}", text)) {

                    student.setPhone(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.OTA_ONA_TELEFON);

                    sendMessage.setText("Qo'shimcha telefon raqamini kiriting (Oila a'zo yoki yaqin qarindosh)");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                } else {
                    sendMessage.setText("Noto'g'ri ma'lumot kiritildi  ❗️\nIltimos tekshirib qaytadan kiritb ko`ring.");
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }
            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.OTA_ONA_TELEFON)) {

                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else if (Pattern.matches("[+]998[0-9][0-9]{8}", text)) {

                    student.setFathPhone(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.YASHASH_MANZILI);
                    sendMessage.setText("Hozirda istiqomat qilayotgan yashash manzilingizni kirting.");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                } else {
                    sendMessage.setText("Noto'g'ri ma'lumot kiritildi  ❗️\nIltimos tekshirib qaytadan kiritb ko`ring.");
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                }

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.YASHASH_MANZILI)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                }
                student.setManzil(text);
                CompanantContainer.studentStepMap.put(chatId, UserStatus.TALIM_OLGAN);

                sendMessage.setText("Oxirgi ta’lim olgan muassasangizni kiriting");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TALIM_OLGAN)) {
                if (text.equals("❌ Bekor qilish")) {
                    sendMessage.setText("Ushbu Bot BUXORO INNOVATSIYALAR UNIVERSITETI  Oliygohi bilan  tanishtirish va abituriyentlarni ro'yxatga olish uchun yaratilgan!");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                    sendMessage.setChatId(chatId);
                    univercityBot.sendMsg(sendMessage);
                    CompanantContainer.studentStepMap.remove(chatId);

                } else {
                    student.setSchoolType(text);
                    CompanantContainer.studentStepMap.put(chatId, UserStatus.DIPLOM_ATTESTAT_RASMI);

                    sendMessage.setText("O’rta yoki o’rta maxsus ta’lim diplom yoki attestatingizni pdf/jpg shaklida yuklang.");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
                    univercityBot.sendMsg(sendMessage);
                }
            }

        } else if (text.equals("BIU Oliygohi haqida")) {
            Users users = userService.getUsersById(userId);
            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);
            List<BiuUnivercity> litsenziya = biuUnivercityService.getByMessageType(MessageType.LITSENZIYA);

            SendPhoto sendPhoto = new SendPhoto();
            SendMessage sendMessage1 = new SendMessage();
            sendMessage1.setChatId(chatId);
            sendPhoto.setChatId(message.getChatId());


            for (BiuUnivercity b : biuUnivercity) {

                sendPhoto.setPhoto(new InputFile(b.getMediaId()));
                univercityBot.sendMsg(sendPhoto);
                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    sendMessage1.setText("Ma'lumot : " + b.getInfoUz());
                } else {
                    sendMessage1.setText("Информация : " + b.getInfoRu());
                }

                sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.AloqaUz());
                univercityBot.sendMsg(sendMessage1);
            }

            for (BiuUnivercity l : litsenziya) {

                sendPhoto.setPhoto(new InputFile(l.getMediaId()));
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                univercityBot.sendMsg(sendPhoto);
            }

        } else if (text.equals("\uD83C\uDDF7\uD83C\uDDFA/\uD83C\uDDFA\uD83C\uDDFF Til")) {

            Users users = CompanantContainer.userDeails.get(userId);

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("Tilni tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.changeLanguageUz());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_UZ);
            } else {
                sendMessage.setChatId(chatId);
                sendMessage.setText("Выберите ваш язык!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.changeLanguageRu());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_RU);
            }
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("\uD83C\uDDF7\uD83C\uDDFA/\uD83C\uDDFA\uD83C\uDDFF Язык (ruscha)")) {

            Users users = CompanantContainer.userDeails.get(userId);

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                sendMessage.setText("Tilni tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.changeLanguageUz());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_UZ);
            } else {
                sendMessage.setText("Выберите ваш язык!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.changeLanguageRu());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_RU);
            }

            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Выбрано: \uD83C\uDDF7\uD83C\uDDFA Русский")) {

            Users users = CompanantContainer.userDeails.get(userId);
            users.setLanguacheEnums(LanguacheEnums.RU);
            userRepository.save(users);
            CompanantContainer.userDeails.put(userId, users);
            sendMessage.setText("Выбран русский язык!");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.changeLanguageRu());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Танланди: \uD83C\uDDFA\uD83C\uDDFF O'zbekcha")) {

            Users users = CompanantContainer.userDeails.get(userId);
            users.setLanguacheEnums(LanguacheEnums.UZ);
            userRepository.save(users);
            CompanantContainer.userDeails.put(userId, users);
            sendMessage.setText("O'zbek tili tanlandi!");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.changeLanguageUz());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("⬅️ Назад")) {
            CompanantContainer.UserStepMap.get(chatId).equals(UserStatus.CHANGE_LANGUAGE_RU);
            sendMessage.setChatId(chatId);
            sendMessage.setText("(ruscha)Ortga qaytish tugmasi bosildi.");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserRu());
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("⬅️ Orqaga")) {
            CompanantContainer.UserStepMap.get(chatId).equals(UserStatus.CHANGE_LANGUAGE_UZ);
            sendMessage.setChatId(chatId);
            sendMessage.setText("Ortga qaytish tugmasi bosildi.");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("\uD83D\uDCDE Biz bilan bog'lanish")) {
            Users users = userService.getUsersById(userId);
            ContactConnection contactConnection = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId());
            sendPhoto.setPhoto(new InputFile(contactConnection.getImage()));
            StringBuilder stringBuilder = new StringBuilder();

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                stringBuilder.append("\uD83D\uDCCD Manzil :  ").append(contactConnection.getLocationUz());
                stringBuilder.append("\n\uD83D\uDCF2Telefon raqam :  ").append(contactConnection.getContact());
            } else {
                stringBuilder.append("\uD83D\uDCCD Адрес :  ").append(contactConnection.getLocationRu());
                stringBuilder.append("\n\uD83D\uDCF2Номер телефона :  ").append(contactConnection.getContact());
            }
            sendPhoto.setCaption(stringBuilder.toString());
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.AloqaUz());

            univercityBot.sendMsg(sendPhoto);

        } else if (text.equals("\uD83D\uDCDE Biz bilan bog'lanish (ruscha)")) {

            Users users = userService.getUsersById(userId);
            ContactConnection contactConnection = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId());
            sendPhoto.setPhoto(new InputFile(contactConnection.getImage()));
            StringBuilder stringBuilder = new StringBuilder();

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                stringBuilder.append("\uD83D\uDCCD Manzil : ").append(contactConnection.getLocationUz());
                stringBuilder.append("\n\uD83D\uDCF2Telefon raqam : ").append(contactConnection.getContact());
            } else {
                stringBuilder.append("\uD83D\uDCCD Адрес : ").append(contactConnection.getLocationRu());
                stringBuilder.append("\n\uD83D\uDCF2Номер телефона : ").append(contactConnection.getContact());
            }
            sendPhoto.setCaption(stringBuilder.toString());
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.AloqaUz());

            univercityBot.sendMsg(sendPhoto);

        } else if (text.equals("Abiturientlarga \uD83C\uDF93")) {

            Users users = CompanantContainer.userDeails.get(userId);

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                sendMessage.setText("Ushbu bo'limizda Oliygohimizdagi yo'nalishlar va shartnoma narxlari haqida ma'lumot olishingz mumkin!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduDegreeUz());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_UZ);
            } else {
                sendMessage.setText("В этом разделе вы можете получить информацию о маршрутах и договорных ценах в нашем университете");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduDegreeRu());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_RU);
            }

            sendMessage.setChatId(chatId);
            CompanantContainer.UserStepMap.put(chatId, UserStatus.ABITURIENTLARGA_UZ);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Abiturientlarga \uD83C\uDF93 (ruscha)")) {

            Users users = CompanantContainer.userDeails.get(userId);

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                sendMessage.setText("Ushbu bo'limizda Oliygohimizdagi yo'nalishlar va shartnoma narxlari haqida ma'lumot olishingz mumkin!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduDegreeUz());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_UZ);
            } else {
                sendMessage.setText("В этом разделе вы можете получить информацию о маршрутах и договорных ценах в нашем университете");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduDegreeRu());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_RU);
            }

            sendMessage.setChatId(chatId);
            CompanantContainer.UserStepMap.put(chatId, UserStatus.ABITURIENTLARGA_RU);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Ta'lim shaklari va shartnoma narxlari")) {

            Users users = CompanantContainer.userDeails.get(userId);

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                sendMessage.setText("Ta'lim shaklari va shartnoma narxlari.");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_UZ);
            } else {
                sendMessage.setText("Формы обучения и контрактные цены");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduTypeRu());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_RU);
            }

            sendMessage.setChatId(chatId);
            CompanantContainer.UserStepMap.put(chatId, UserStatus.TALIM_SHAKLI_SHARTNOMA_UZ);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Формы обучения и контрактные цены")) {

            Users users = CompanantContainer.userDeails.get(userId);

            if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                sendMessage.setText("Ta'lim shaklari va shartnoma narxlari.");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_UZ);
            } else {
                sendMessage.setText("Формы обучения и контрактные цены");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduTypeRu());
                CompanantContainer.UserStepMap.put(chatId, UserStatus.CHANGE_LANGUAGE_RU);
            }

            sendMessage.setChatId(chatId);
            CompanantContainer.UserStepMap.put(chatId, UserStatus.TALIM_SHAKLI_SHARTNOMA_RU);
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Kunduzgi")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();

                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar hozircha mavjud emas!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Kunduzgi ru")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();

                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Данные пока недоступны!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Kechgi")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.KECHGI);
            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();

                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar hozircha mavjud emas!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Kechgi ru")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();

                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Данные пока недоступны!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Sirtqi")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.SIRTQI);
            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();

                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar hozircha mavjud emas!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Sirtqi ru")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.SIRTQI);
            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();

                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduTypeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Данные пока недоступны!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Magistratura")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.MAGISTRATURA);

            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();
                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduDegreeUz());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar hozircha mavjud emas!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Mагистра")) {
            Users users = userService.getUsersById(userId);
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.MAGISTRATURA);

            if (directoryInfo != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(message.getChatId());
                sendPhoto.setPhoto(new InputFile(directoryInfo.getImage()));
                StringBuilder stringBuilder = new StringBuilder();
                if (users.getLanguacheEnums().equals(LanguacheEnums.UZ)) {
                    stringBuilder.append(directoryInfo.getInfoUz());
                } else {
                    stringBuilder.append(directoryInfo.getInfoRu());
                }
                sendPhoto.setCaption(stringBuilder.toString());
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.EduDegreeRu());
                univercityBot.sendMsg(sendPhoto);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Данные пока недоступны!"
                );
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (text.equals("Ro'yxatdan o'tish\uD83D\uDCDD\uD83C\uDF93")) {

            sendMessage.setText("Mazkur bo'lim BIU oliygohida ro'yxatdan o'tish uchun mo'ljallangan!");
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            SendMessage sendMessage1 = new SendMessage();
            sendMessage1.setText("Ta'lim darajani tanlang");
            sendMessage1.setChatId(chatId);
            sendMessage1.setReplyMarkup(KeyboardButtonUtil.educationDegreeForUserMarkup(getEducationList()));

            univercityBot.sendMsg(sendMessage1);

            System.out.println(Long.valueOf(chatId));
            CompanantContainer.studentStepMap.put(chatId, UserStatus.TALIM_DARAJASINI_KIRITIDI);
            CompanantContainer.studentMap.put(
                    chatId, new Student(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, chatId, null));


        } else if (text.equals("BIU Oliygohi haqida(ruscha)")) {

            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);
            List<BiuUnivercity> litsenziya = biuUnivercityService.getByMessageType(MessageType.LITSENZIYA);

            SendPhoto sendPhoto = new SendPhoto();
            SendMessage sendMessage1 = new SendMessage();
            sendMessage1.setChatId(chatId);
            sendPhoto.setChatId(message.getChatId());


            for (BiuUnivercity b : biuUnivercity) {
                sendPhoto.setPhoto(new InputFile(b.getMediaId()));
                univercityBot.sendMsg(sendPhoto);

                sendMessage1.setText("Информация : " + b.getInfoRu());
                sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.AloqaRu());
                univercityBot.sendMsg(sendMessage1);
            }
            for (BiuUnivercity l : litsenziya) {
                sendPhoto.setPhoto(new InputFile(l.getMediaId()));
                sendPhoto.setReplyMarkup(KeyboardButtonUtil.startUserRu());
                univercityBot.sendMsg(sendPhoto);
            }

        } else if (text.equals("Shartnoma olish")) {
            SendMessage message1 = new SendMessage();
            message1.setText("So'rovingiz qabul qilindi.\nTez orada sizga shartnoma yuboriladi.");
            message1.setChatId(chatId);

            message1.setReplyMarkup(KeyboardButtonUtil.startUserUz());

            univercityBot.sendMsg(message1);

            Student student = studentService.getUserId(chatId);

            Integer directoryId = student.getEducationDirectory().getId();

            System.out.println(directoryId);

            EducationDirectory educationDirectory = educationDirectoryRepository.findByIdAndVisibleTrue(directoryId);

            ShartnomaInfo shartnomaInfo = shartnomaInfoRepository.findByEducationDirectoryId(directoryId);

            OrderShartnoma orderShartnoma = new OrderShartnoma();
            orderShartnoma.setStudent(student.getName());
            orderShartnoma.setShartnomaInfo(shartnomaInfo);
            OrderShartnoma save = orderShartnomaRepository.save(orderShartnoma);


            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);
            sendDocument.setDocument(new InputFile(shartnomaInfo.getShartnomaUz()));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Abiturient:  ").append(student.getName());
            stringBuilder.append("\nTanlangan yo'nalish:  ").append(educationDirectory.getNameUZ());
            sendDocument.setCaption(stringBuilder.toString());
            univercityBot.sendMsg(sendDocument);

            SendMessage sendMessage1 = new SendMessage();
            String msg = String.format("Shartnoma ✅\n\n" +
                    "F.I.Sh: " + save.getStudent() +
                    "\nYo'nalish ID: " + save.getShartnomaInfo());

            sendMessage1.setText(msg);
            sendMessage1.setChatId(chatId);
            sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.Replay());
            sendMessage1.setDisableNotification(true);


        } else if (text.equals("⏪ Ortga qaytish")) {

            if (CompanantContainer.UserStepMap.get(chatId).equals(UserStatus.ABITURIENTLARGA_RU)) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("⬅ Нажата кнопка «Назад».");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserRu());
                univercityBot.sendMsg(sendMessage);


            } else if (CompanantContainer.UserStepMap.get(chatId).equals(UserStatus.ABITURIENTLARGA_UZ)) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("⬅ Ortga qaytish tugmasi bosildi.");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                univercityBot.sendMsg(sendMessage);


            } else if (CompanantContainer.UserStepMap.get(chatId).equals(UserStatus.TALIM_SHAKLI_SHARTNOMA_RU)) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("⬅ Нажата кнопка «Назад».");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduDegreeRu());
                univercityBot.sendMsg(sendMessage);
                CompanantContainer.UserStepMap.remove(chatId);

                CompanantContainer.UserStepMap.put(chatId, UserStatus.ABITURIENTLARGA_RU);

            } else if (CompanantContainer.UserStepMap.get(chatId).equals(UserStatus.TALIM_SHAKLI_SHARTNOMA_UZ)) {
                sendMessage.setChatId(chatId);
                sendMessage.setText("⬅ Ortga qaytish tugmasi bosildi.");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.EduDegreeUz());
                univercityBot.sendMsg(sendMessage);
                CompanantContainer.UserStepMap.remove(chatId);

                CompanantContainer.UserStepMap.put(chatId, UserStatus.ABITURIENTLARGA_UZ);

            } else {
                sendMessage.setChatId(chatId);
                sendMessage.setText("⬅ Ortga qaytish tugmasi bosildi.");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.startUserUz());
                univercityBot.sendMsg(sendMessage);
            }
        }
    }


    public void handleLocation(User user, Message message) {

    }

    public void handleContact(User user, Message message, String chatId) {
        Student student = CompanantContainer.studentMap.get(chatId);
        if (CompanantContainer.studentStepMap.get(chatId).equals(UserStatus.TELEFON_RAQAMINI_KIRITIDI)) {
            Contact contact = message.getContact();
            student.setPhone(String.valueOf(contact.getPhoneNumber()));
            CompanantContainer.studentStepMap.put(chatId, UserStatus.OTA_ONA_TELEFON);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ota/ona telefon raqamini kiriting.");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.CancelAndBackUz());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);
        }

    }

    public void handleCallBack(User user, Message message, String data) {

    }

    public List<EducationDegree> getEducationList() {
        Iterable<EducationDegree> iterable = educationDegreeRepository.findAllByVisibleIsTrue();
        List<EducationDegree> educationDegreeList = new LinkedList<>();
        iterable.forEach(educationDegree -> educationDegreeList.add(toEducatinDegree(educationDegree)));
        return educationDegreeList;
    }

    private EducationDegree toEducatinDegree(EducationDegree educationDegree) {
        if (educationDegree == null) {
            return null;
        } else {
            EducationDegree educationDegree1 = new EducationDegree();
            educationDegree1.setNameUz(educationDegree.getNameUz());
            educationDegree1.setNameRu(educationDegree.getNameRu());
            educationDegree1.setVisible(educationDegree.getVisible());
            return educationDegree1;
        }
    }

    public List<EducationDirectory> getDirectoryList() {
        EducationDegree educationDegree = educationDegreeRepository.findByNameUz("Bakalavr");
        Integer educationId = educationDegree.getId();
        Iterable<EducationDirectory> iterable = educationDirectoryRepository.findByEducationDegreeIdAndVisibleTrue(educationId);
        List<EducationDirectory> educationDirectoryList = new LinkedList<>();
        iterable.forEach(educationDirectory -> educationDirectoryList.add(toEducationDirectory(educationDirectory)));
        return educationDirectoryList;
    }

    private EducationDirectory toEducationDirectory(EducationDirectory educationDirectory) {
        if (educationDirectory == null) {
            return null;
        } else {
            EducationDirectory educationDirectory1 = new EducationDirectory();
            educationDirectory1.setNameUZ(educationDirectory.getNameUZ());
            educationDirectory1.setNameRU(educationDirectory.getNameRU());
            educationDirectory1.setDirectoryId(educationDirectory.getDirectoryId());
            return educationDirectory1;
        }
    }

}
