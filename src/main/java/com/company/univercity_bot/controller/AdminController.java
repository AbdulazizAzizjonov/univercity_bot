package com.company.univercity_bot.controller;

import com.company.univercity_bot.enums.AdminStatus;
import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.*;
import com.company.univercity_bot.repository.EducationDegreeRepository;
import com.company.univercity_bot.repository.EducationDirectoryRepository;
import com.company.univercity_bot.repository.ShartnomaInfoRepository;
import com.company.univercity_bot.service.*;
import com.company.univercity_bot.telegramBot.UnivercityBot;
import com.company.univercity_bot.util.InlineKeyboardButtonUtil;
import com.company.univercity_bot.util.KeyboardButtonUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.LinkedList;
import java.util.List;


import static com.company.univercity_bot.container.CompanantContainer.*;

@Component
@RequiredArgsConstructor

public class AdminController {

    @Autowired
    @Lazy
    private UnivercityBot univercityBot;

    @Autowired
    private final ContactConnectionService contactConnectionService;

    @Autowired
    private final DirectoryInfoService directoryInfoService;

    @Autowired
    private final BiuUnivercityService biuUnivercityService;

    @Autowired
    private final EducationDegreeService educationDegreeService;

    @Autowired
    private final EducationDirectoryService educationDirectoryService;

    @Autowired
    private final EducationDegreeRepository educationDegreeRepository;

    @Autowired
    private final EducationDirectoryRepository educationDirectoryRepository;
    @Autowired
    private final ShartnomaInfoRepository shartnomaInfoRepository;
    @Autowired
    private final ShartnomaInfoService shartnomaInfoService;

    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasPhoto()) {
            handlePhoto(user, message);
        } else if (message.hasDocument()) {
            hasDocument(user, message);
        }
    }

    private void hasDocument(User user, Message message) {

        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        if (shartnomaInfoStepMap.containsKey(chatId)) {

            ShartnomaInfo shartnomaInfo = shartnomaInfoMap.get(chatId);

            Document document = message.getDocument();

            if (shartnomaInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_SHARTNOMA_UZ)) {
                shartnomaInfo.setShartnomaUz(String.valueOf(document.getFileId()));

                shartnomaInfoMap.put(chatId, shartnomaInfo);

                shartnomaInfoStepMap.put(chatId, AdminStatus.ENTERED_SHARTNOMA_RU);

                sendMessage.setText("Shartnomani ruscha variantini jo'nating");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (shartnomaInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_SHARTNOMA_RU)) {
                shartnomaInfo.setShartnomaRu(String.valueOf(document));

                shartnomaInfoMap.put(chatId, shartnomaInfo);
                shartnomaInfoService.save(shartnomaInfo);

                sendMessage.setText("Shartnoma muvaffaqiyatli saqlandi!");
                System.out.println(1);
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.SHARTNOMACRUD());
                univercityBot.sendMsg(sendMessage);

                shartnomaInfoStepMap.remove(chatId);
                shartnomaInfoMap.remove(chatId);
            }
        }
    }

    private void handlePhoto(User user, Message message) {

        String text = message.getText();

        SendMessage sendMessage = new SendMessage();

        List<PhotoSize> photoSizeList = message.getPhoto();
        String chatId = String.valueOf(message.getChatId());

        if (infoImageStepMap.containsKey(chatId)) {
            InfoImage infoImage = infoImageMap.get(chatId);

            if (infoImageStepMap.get(chatId).equals(AdminStatus.TYPING_MESSAGE_RU)) {
                infoImage.setMediaId(photoSizeList.get(0).getFileId());
                infoImage.setMessageType(MessageType.PHOTO);

                infoImageStepMap.put(chatId, AdminStatus.ENTERED_FOTO);

                sendMessage.setText("Button qo'shasizmi?");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ButtonForRequest());
                univercityBot.sendMsg(sendMessage);

            } else if (infoImageStepMap.get(chatId).equals(AdminStatus.SAVE_INFO)) {

            }
        } else if (contactConnectionStepMap.containsKey(chatId)) {

            ContactConnection contactConnection = contactConnectionMap.get(chatId);

            if (contactConnectionStepMap.get(chatId).equals(AdminStatus.ENTERED_IMAGE)) {
                contactConnection.setImage(photoSizeList.get(0).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(contactConnection.getImage()));
                sendPhoto.setCaption(String.format("Manzil o'zbekcha: %s\n" +
                                "Manzil ruscha: %s \n" +
                                "Call centre: %s \n\n" +
                                "Quyidagi ma'lumot bazaga saqlansinmi?",
                        contactConnection.getLocationUz(), contactConnection.getLocationRu(), contactConnection.getContact()));

                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.CommitAndCancelContactConnection());
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);

            } else if (contactConnectionStepMap.get(chatId).equals(AdminStatus.UPDATE_IMAGE)) {

                ContactConnection contactConnection1 = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
                contactConnection1.setImage(photoSizeList.get(0).getFileId());

                contactConnectionService.updateImage(contactConnection1.getImage(), contactConnection1.getMessageType());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(contactConnection1.getImage()));
                String builder = "Ma'lumot o'zbekcha: \n\n" +
                        "Manzil: " + contactConnection1.getLocationUz() + "\n Aloqa: " + contactConnection1.getContact();
                sendPhoto.setCaption(builder);
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);
                sendMessage.setText("Rasm muvaffaqiyatli o'zgartirildi!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            }

        } else if (directoryInfoStepMap.containsKey(chatId)) {

            DirectoryInfo directoryInfo = directoryInfoMap.get(chatId);

            if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_RU)) {
                directoryInfo.setImage(photoSizeList.get(0).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo.getImage()));
                String builder = "Ma'lumot o'zbekcha:  " +
                        directoryInfo.getInfoUz() + "\n\nQuyidagi ma'lumot bazaga saqlansinmi?";

                sendPhoto.setCaption(builder);

                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.CommitAndCancelDirectoryInfo());
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_RU_KECHGI)) {
                directoryInfo.setImage(photoSizeList.get(0).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo.getImage()));
                String builder = "Ma'lumot o'zbekcha:  " +
                        directoryInfo.getInfoUz() + "\n\nQuyidagi ma'lumot bazaga saqlansinmi?";

                sendPhoto.setCaption(builder);

                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.CommitAndCancelDirectoryInfo());
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_RU_SIRTQI)) {
                directoryInfo.setImage(photoSizeList.get(0).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo.getImage()));
                String builder = "Ma'lumot o'zbekcha:  " +
                        directoryInfo.getInfoUz() + "\n\nQuyidagi ma'lumot bazaga saqlansinmi?";
                sendPhoto.setCaption(builder);
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.CommitAndCancelDirectoryInfo());
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_RU_MAGISTRATURA)) {
                directoryInfo.setImage(photoSizeList.get(0).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo.getImage()));
                String builder = "Ma'lumot o'zbekcha:  " +
                        directoryInfo.getInfoUz() + "\n\nQuyidagi ma'lumot bazaga saqlansinmi?";
                sendPhoto.setCaption(builder);
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.CommitAndCancelDirectoryInfo());
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_IMAGE_KUNDUZGI)) {
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
                directoryInfo1.setImage(photoSizeList.get(0).getFileId());

                directoryInfoService.updateImage(directoryInfo1.getImage(), directoryInfo1.getMessageType());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo1.getImage()));
                String builder = "Ma'lumot o'zbekcha: \n\n" +
                        directoryInfo1.getInfoUz();
                sendPhoto.setCaption(builder);
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);
                sendMessage.setText("Rasm muvaffaqiyatli o'zgartirildi!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_IMAGE_KECHGI)) {
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.KECHGI);
                directoryInfo1.setImage(photoSizeList.get(0).getFileId());

                directoryInfoService.updateImage(directoryInfo1.getImage(), directoryInfo1.getMessageType());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo1.getImage()));
                String builder = "Ma'lumot o'zbekcha: \n\n" +
                        directoryInfo1.getInfoUz();
                sendPhoto.setCaption(builder);
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);
                sendMessage.setText("Rasm muvaffaqiyatli o'zgartirildi!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_IMAGE_SIRTQI)) {
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.SIRTQI);
                directoryInfo1.setImage(photoSizeList.get(0).getFileId());

                directoryInfoService.updateImage(directoryInfo1.getImage(), directoryInfo1.getMessageType());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo1.getImage()));
                String builder = "Ma'lumot o'zbekcha: \n\n" +
                        directoryInfo1.getInfoUz();
                sendPhoto.setCaption(builder);
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);
                sendMessage.setText("Rasm muvaffaqiyatli o'zgartirildi!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_IMAGE_MAGISTRATURA)) {
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.MAGISTRATURA);
                directoryInfo1.setImage(photoSizeList.get(0).getFileId());

                directoryInfoService.updateImage(directoryInfo1.getImage(), directoryInfo1.getMessageType());
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(directoryInfo1.getImage()));
                String builder = "Ma'lumot o'zbekcha: \n\n" +
                        directoryInfo1.getInfoUz();
                sendPhoto.setCaption(builder);
                sendPhoto.setChatId(chatId);
                univercityBot.sendMsg(sendPhoto);
                sendMessage.setText("Rasm muvaffaqiyatli o'zgartirildi!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            }
        } else if (biuUnivercityStepMap.containsKey(chatId)) {

            BiuUnivercity biuUnivercity = biuUnivercityMap.get(chatId);

            if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.ENTERED_IMAGE_BIU)) {
                biuUnivercity.setMediaId(photoSizeList.get(0).getFileId());


                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(biuUnivercity.getMediaId()));
                univercityBot.sendMsg(sendPhoto);
                sendMessage.setText("Ma'lumot o'zbekcha: %s\n\n" + biuUnivercity.getInfoUz() + "\n\nQuyidagi ma'lumot bazaga saqlansinmi?");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.MessageCommitAndCancelBIU());
                univercityBot.sendMsg(sendMessage);

            } else if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.UPDATE_IMAGE_BIU)) {
                List<BiuUnivercity> biuUnivercity1 = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);
                for (BiuUnivercity b : biuUnivercity1) {
                    b.setMediaId(photoSizeList.get(0).getFileId());
                    biuUnivercityService.updateImage(b.getMediaId(), b.getMessageType());
                    SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(b.getMediaId()));
                    String builder = "Ma'lumot o'zbekcha: \n\n" +
                            b.getInfoUz();
                    sendPhoto.setCaption(builder);
                    sendPhoto.setChatId(chatId);
                    univercityBot.sendMsg(sendPhoto);
                }

                sendMessage.setText("Rasm muvaffaqiyatli o'zgartirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity_update());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.ENTERED_LITSENZIYA)) {
                biuUnivercity.setMediaId(photoSizeList.get(0).getFileId());

                biuUnivercityService.saveMesage(biuUnivercity);
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Litsenziya saqlandi!"
                );
                sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                univercityBot.sendMsg(sendMessage1);

            }
        }
    }

    private void handleText(User user, Message message) {

        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();

        if (text.equals("/start")) {

            sendMessage.setText("Xush kelibsiz admin, qanday vazifa bajarmohchisiz?");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("BIU Oliygohi haqida 2")) {

            sendMessage.setText("Quyidagi amallardan birini tanlang \uD83D\uDC47");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("BIU Oliygohi haqida")) {

            sendMessage.setText("Quyidagi amallardan birini tanlang \uD83D\uDC47");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIUInfo());
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("\uD83D\uDCDE Biz bilan bog'lanish")) {

            sendMessage.setText("Quyidagi amallardan birini tanlang \uD83D\uDC47");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ContactConnectionCRUD());

            contactConnectionStepMap.put(chatId, AdminStatus.CLICKED_CONTACT_CONNECTION);
            contactConnectionMap.put(chatId,
                    new ContactConnection(null, MessageType.BIZ_BILAN_BOGLANISH, null, null, null, null));
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Abiturientlarga \uD83C\uDF93")) {

            sendMessage.setText("Quyidagi amallardan birini tanlang \uD83D\uDC47");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarCRUD());
            univercityBot.sendMsg(sendMessage);

        } else if (infoImageStepMap.containsKey(chatId)) {

            InfoImage infoImage = infoImageMap.get(chatId);

            if (infoImageStepMap.get(chatId).equals(AdminStatus.TYPING_REQUEST)) {
                infoImage.setInfoUZ(text);
                infoImageStepMap.put(chatId, AdminStatus.TYPING_MESSAGE_UZ);

                sendMessage.setText("Ma'lumotni ruscha variantini kiriting!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (infoImageStepMap.get(chatId).equals(AdminStatus.TYPING_MESSAGE_UZ)) {
                infoImage.setInfoRU(text);
                infoImageStepMap.put(chatId, AdminStatus.TYPING_MESSAGE_RU);

                sendMessage.setText("Media joylysizmi?");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.MediaForRequest());
                univercityBot.sendMsg(sendMessage);

            } else if (infoImageStepMap.get(chatId).equals(AdminStatus.ENTERED_FOTO)) {
                infoImage.setButtonName(text);
                infoImageStepMap.put(chatId, AdminStatus.ENTERED_BUTTON);

                sendMessage.setText("Button url ni kiriting!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (infoImageStepMap.get(chatId).equals(AdminStatus.ENTERED_BUTTON)) {
                infoImage.setButtonUrl(text);
                infoImageStepMap.put(chatId, AdminStatus.SAVE_INFO);
                univercityBot.sendMsg(sendMessage);
            }

        } else if (contactConnectionStepMap.containsKey(chatId)) {

            String newManzilUz = null;
            String newManzilRu = null;
            String newContact = null;

            ContactConnection contactConnection = contactConnectionMap.get(chatId);

            if (contactConnectionStepMap.get(chatId).equals(AdminStatus.ENTERED_LOCATION_UZ)) {
                contactConnection.setLocationUz(text);
                contactConnectionStepMap.put(chatId, AdminStatus.ENTERED_LOCATION_RU);

                sendMessage.setText("Manzilni ruscha variantini kiriting");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (contactConnectionStepMap.get(chatId).equals(AdminStatus.ENTERED_LOCATION_RU)) {
                contactConnection.setLocationRu(text);
                contactConnectionStepMap.put(chatId, AdminStatus.ENTERED_CONTACT);

                sendMessage.setText("Call centre raqamini kiriting");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (contactConnectionStepMap.get(chatId).equals(AdminStatus.ENTERED_CONTACT)) {
                contactConnection.setContact(text);
                contactConnectionStepMap.put(chatId, AdminStatus.ENTERED_IMAGE);

                sendMessage.setText("Rasmni jo'nating");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (contactConnectionStepMap.get(chatId).equals(AdminStatus.UPDATE_MANZIL_UZ)) {
                newManzilUz = text;
                ContactConnection contactConnection1 = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
                contactConnectionService.updateLocationUZ(newManzilUz, contactConnection1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (contactConnectionStepMap.get(chatId).equals(AdminStatus.UPDATE_MANZIL_RU)) {
                newManzilRu = text;
                ContactConnection contactConnection1 = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
                contactConnectionService.updateLocationUZ(newManzilRu, contactConnection1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (contactConnectionStepMap.get(chatId).equals(AdminStatus.UPDATE_CONTACT)) {
                newContact = text;
                ContactConnection contactConnection1 = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
                contactConnectionService.updateLocationUZ(newContact, contactConnection1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            }

        } else if (directoryInfoStepMap.containsKey(chatId)) {

            String newInfouz = null;
            String newInforu = null;

            DirectoryInfo directoryInfo = directoryInfoMap.get(chatId);

            if (directoryInfoStepMap.get(chatId).equals(AdminStatus.CLICKED_DIRECTORY_INFO)) {
                directoryInfo.setInfoUz(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_UZ);

                sendMessage.setText("Ma'lumotni ruscha variantini kiritng!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_UZ)) {
                directoryInfo.setInfoRu(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_RU);

                sendMessage.setText("Rasmni jonating!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.CLICKED_DIRECTORY_INFO_KECHGI)) {
                directoryInfo.setInfoUz(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_UZ_KECHGI);

                sendMessage.setText("Ma'lumotni ruscha variantini kiritng!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_UZ_KECHGI)) {
                directoryInfo.setInfoRu(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_RU_KECHGI);

                sendMessage.setText("Rasmni jonating!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.CLICKED_DIRECTORY_INFO_SIRTQI)) {
                directoryInfo.setInfoUz(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_UZ_SIRTQI);

                sendMessage.setText("Ma'lumotni ruscha variantini kiritng!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_UZ_SIRTQI)) {
                directoryInfo.setInfoRu(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_RU_SIRTQI);

                sendMessage.setText("Rasmni jonating!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.CLICKED_DIRECTORY_INFO_MAGISTRATURA)) {
                directoryInfo.setInfoUz(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_UZ_MAGISTRATURA);

                sendMessage.setText("Ma'lumotni ruscha variantini kiritng!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_DIRECTORY_INFO_UZ_MAGISTRATURA)) {
                directoryInfo.setInfoRu(text);
                directoryInfoStepMap.put(chatId, AdminStatus.ENTERED_DIRECTORY_INFO_RU_MAGISTRATURA);

                sendMessage.setText("Rasmni jonating!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_UZ_KUNDUZGI)) {
                newInfouz = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
                directoryInfoService.updateInfoUZ(newInfouz, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_RU_KUNDUZGI)) {
                newInforu = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
                directoryInfoService.updateInfoRU(newInforu, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info ru)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_UZ_KECHGI)) {
                newInfouz = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.KECHGI);
                directoryInfoService.updateInfoUZ(newInfouz, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_RU_KECHGI)) {
                newInforu = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.KECHGI);
                directoryInfoService.updateInfoRU(newInforu, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info ru)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_UZ_SIRTQI)) {
                newInfouz = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.SIRTQI);
                directoryInfoService.updateInfoUZ(newInfouz, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_RU_SIRTQI)) {
                newInforu = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.SIRTQI);
                directoryInfoService.updateInfoRU(newInforu, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info ru)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_UZ_MAGISTRATURA)) {
                newInfouz = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.MAGISTRATURA);
                directoryInfoService.updateInfoUZ(newInfouz, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (directoryInfoStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_RU_MAGISTRATURA)) {
                newInforu = text;
                DirectoryInfo directoryInfo1 = directoryInfoService.getByMessageType(MessageType.MAGISTRATURA);
                directoryInfoService.updateInfoRU(newInforu, directoryInfo1.getMessageType());

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info ru)");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            }
        } else if (biuUnivercityStepMap.containsKey(chatId)) {

            String infoUz = null;
            String infoRu = null;

            BiuUnivercity biuUnivercity = biuUnivercityMap.get(chatId);

            if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.ENTERED_INFO_UZ_BIU)) {
                biuUnivercity.setInfoUz(text);
                biuUnivercityStepMap.put(chatId, AdminStatus.ENTERED_INFO_RU_BIU);

                sendMessage.setText("Ma'lumotni ruscha variantini kiriting!");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.ENTERED_INFO_RU_BIU)) {
                biuUnivercity.setInfoRu(text);
                biuUnivercityStepMap.put(chatId, AdminStatus.ENTERED_IMAGE_BIU);

                sendMessage.setText("Rasmni jo'nating");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_UZ_BIU)) {
                infoUz = text;
                List<BiuUnivercity> biuUnivercity1 = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);
                for (BiuUnivercity b : biuUnivercity1) {
                    biuUnivercityService.updateInfoUZ(infoUz, b.getMessageType());
                }

                sendMessage.setText("O'zgartirish amalga oshirildi! (Info uz)");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity_update());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (biuUnivercityStepMap.get(chatId).equals(AdminStatus.UPDATE_INFO_RU_BIU)) {
                infoRu = text;
                List<BiuUnivercity> biuUnivercity1 = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);
                for (BiuUnivercity b : biuUnivercity1) {
                    biuUnivercityService.updateInfoRU(infoRu, b.getMessageType());
                }
                sendMessage.setText("O'zgartirish amalga oshirildi! (Info ru)");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity_update());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            }

        } else if (educationDegreeStepMap.containsKey(chatId)) {

            String degreeeUz = null;
            String degreeRu = null;

            EducationDegree educationDegree = educationDegreeMap.get(chatId);

            if (educationDegreeStepMap.get(chatId).equals(AdminStatus.ENTERED_DARAJA_UZ)) {
                educationDegree.setNameUz(text);
                educationDegreeStepMap.put(chatId, AdminStatus.ENTERED_DARAJA_RU);
                sendMessage.setText("Daraja turini ruscha variantini kiriting:");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);


            } else if (educationDegreeStepMap.get(chatId).equals(AdminStatus.ENTERED_DARAJA_RU)) {


                educationDegree.setNameRu(text);
                educationDegreeService.saveDegreee(educationDegree);
                sendMessage.setText("Daraja muvaffaqiyatli saqlandi!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                educationDegreeStepMap.remove(chatId);
                educationDegreeMap.remove(chatId);
            }

        } else if (educationDirectoryStepMap.containsKey(chatId)) {

            String NameUz = null;
            String NameRu = null;

            EducationDirectory educationDirectory = educationDirectoryMap.get(chatId);

            if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.ENTERED_BAKALAVR_NAME_UZ)) {
                educationDirectory.setNameUZ(text);
                educationDirectoryMap.put(chatId, educationDirectory);
                educationDirectoryStepMap.put(chatId, AdminStatus.ENTERED_BAKALAVR_NAME_RU);
                sendMessage.setText("Bakalavr yo'nalishini ruscha variantini kiriting:");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.ENTERED_BAKALAVR_NAME_RU)) {
                educationDirectory.setNameRU(text);
                educationDirectoryMap.put(chatId, educationDirectory);
                educationDirectoryStepMap.put(chatId, AdminStatus.ENTERED_BAKALAVR_ID);
                sendMessage.setText("Yo'nalishning ID raqamini kiriting:");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.ENTERED_BAKALAVR_ID)) {
                educationDirectory.setDirectoryId(text);
                educationDirectoryMap.put(chatId, educationDirectory);
                educationDirectoryService.saveDirectory(educationDirectory);
                sendMessage.setText("Bakalavr" + " " + educationDirectory.getNameUZ() + " " + "yo'nalishi muvaffaqiyatli saqlandi!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationBakalavrDirectoryMarkup(getDirectoryList()));
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);


                educationDirectoryStepMap.remove(chatId);
                educationDirectoryMap.remove(chatId);

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.ENTERED_MAGISTRATURA_NAME_UZ)) {
                educationDirectory.setNameUZ(text);
                educationDirectoryMap.put(chatId, educationDirectory);
                educationDirectoryStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA_NAME_RU);
                sendMessage.setText("Magistratura yo'nalishini ruscha variantini kiriting:");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.ENTERED_MAGISTRATURA_NAME_RU)) {
                educationDirectory.setNameRU(text);
                educationDirectoryMap.put(chatId, educationDirectory);
                educationDirectoryStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA_ID);
                sendMessage.setText("Yo'nalishning ID raqamini kiriting:");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.ENTERED_MAGISTRATURA_ID)) {
                educationDirectory.setDirectoryId(text);
                educationDirectoryMap.put(chatId, educationDirectory);
                educationDirectoryService.saveDirectory(educationDirectory);
                sendMessage.setText("Magistratura" + " " + educationDirectory.getNameUZ() + " " + "yo'nalishi muvaffaqiyatli saqlandi!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getMagistraturaDirectoryList()));
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                educationDirectoryStepMap.remove(chatId);
                educationDirectoryMap.remove(chatId);

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.DELETED_BAKALAVR_NAME_UZ)) {

                EducationDirectory educationDirectory1 = educationDirectoryRepository.findByNameUZ(text);

                if (educationDirectory1 != null) {
                    educationDirectoryRepository.visibleFalse(educationDirectory1.getId());
                    sendMessage.setText(text + " " + "muvoffaqiyatli o'chirildi!");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.educationBakalavrDirectoryMarkup(getDirectoryList()));

                    univercityBot.sendMsg(sendMessage);

                    educationDirectoryStepMap.remove(chatId);
                    educationDirectoryMap.remove(chatId);

                } else {
                    sendMessage.setText("Notog'ri xabar, Tekshirib qaytada kiriting!");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getDirectoryList()));
                    univercityBot.sendMsg(sendMessage);
                }

            } else if (educationDirectoryStepMap.get(chatId).equals(AdminStatus.DELETED_MAGISTRATURA_NAME_UZ)) {

                EducationDirectory educationDirectory1 = educationDirectoryRepository.findByNameUZ(text);

                if (educationDirectory1 != null) {
                    educationDirectoryRepository.visibleFalse(educationDirectory1.getId());
                    sendMessage.setText(text + " " + "muvoffaqiyatli o'chirildi!");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getMagistraturaDirectoryList()));
                    univercityBot.sendMsg(sendMessage);

                    educationDirectoryStepMap.remove(chatId);
                    educationDirectoryMap.remove(chatId);

                } else if (text.equals("⬅ Ortga qaytish")) {
                    System.out.println(1);
                    AdminStepMap.put(chatId, AdminStatus.DELETED_MAGISTRATURA_YONALISH_ICHIDA);
                    sendMessage.setText("Quyidagilardan birini tanlang!");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getMagistraturaDirectoryList()));
                    univercityBot.sendMsg(sendMessage);

                } else {
                    sendMessage.setText("Notog'ri xabar, Tekshirib qaytada kiriting!");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getMagistraturaDirectoryList()));
                    univercityBot.sendMsg(sendMessage);
                }
            }

        } else if (shartnomaInfoStepMap.containsKey(chatId)) {

            ShartnomaInfo shartnomaInfo = shartnomaInfoMap.get(chatId);

            Document document = message.getDocument();

            if (shartnomaInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_SHARTNOMA_DARAJA)) {
                sendMessage.setText("Yo'nalishni tanlang!");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationBakalavrShartnomaDirectoryMarkup(getShartnomaDirectoryList(text)));
                shartnomaInfoStepMap.put(chatId, AdminStatus.ENTERED_SHARTNOMA_YONALISH);
                univercityBot.sendMsg(sendMessage);

            } else if (shartnomaInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_SHARTNOMA_YONALISH)) {
                EducationDirectory educationDirectory = educationDirectoryRepository.findByNameUZ(text);
                shartnomaInfo.setEducationDirectory(educationDirectory);
                shartnomaInfoStepMap.put(chatId, AdminStatus.ENTERED_SHARTNOMA_UZ);

                sendMessage.setText("Shartnomani o'zbekcha variantini jo'nating");
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            }
//            else if (shartnomaInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_SHARTNOMA_UZ)) {
//                shartnomaInfo.setShartnomaUz(String.valueOf(document));
//                System.out.println(document);
//                shartnomaInfoStepMap.put(chatId, AdminStatus.ENTERED_SHARTNOMA_RU);
//
//                sendMessage.setText("Shartnomani o'zbekcha variantini jo'nating");
//                sendMessage.setChatId(chatId);
//                univercityBot.sendMsg(sendMessage);
//
//            } else if (shartnomaInfoStepMap.get(chatId).equals(AdminStatus.ENTERED_SHARTNOMA_RU)) {
//                shartnomaInfo.setShartnomaRu(String.valueOf(document));
//                shartnomaInfoMap.put(chatId, shartnomaInfo);
//                shartnomaInfoService.save(shartnomaInfo);
//                sendMessage.setText("Shartnoma muvaffaqiyatli saqlandi!");
//                sendMessage.setChatId(chatId);
//                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.SHARTNOMACRUD());
//                univercityBot.sendMsg(sendMessage);
//
//                shartnomaInfoStepMap.remove(chatId);
//                shartnomaInfoMap.remove(chatId);
//            }

        } else if (text.equals("Yo'nalish CRUD")) {
            AdminStepMap.put(chatId, AdminStatus.ENTERED_YONALISH_CRUD);
            sendMessage.setText("Quyidagilardan birini tanlang!");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardButtonUtil.educationDegreeMarkup(getEducationList()));
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Daraja Qo'shish")) {
            sendMessage.setText("Daraja turini o'zbekcha variantini kiriting:");
            sendMessage.setChatId(chatId);

            educationDegreeStepMap.put(chatId, AdminStatus.ENTERED_DARAJA_UZ);
            educationDegreeMap.put(chatId,
                    new EducationDegree(null, null, null, true));
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Bakalavrga Yo'nalish Qo'shish")) {

            AdminStepMap.put(chatId, AdminStatus.ENTERED_BAKALAVR_YONALISH);
            sendMessage.setText("Bakalavr yo'nalishini o'zbekcha variantini kiriting:");
            sendMessage.setChatId(chatId);

            EducationDegree education = educationDegreeService.getNameUz("Bakalavr");


            educationDirectoryStepMap.put(chatId, AdminStatus.ENTERED_BAKALAVR_NAME_UZ);
            EducationDirectory edu = new EducationDirectory(null, null, null, null, true, education);
            educationDirectoryMap.put(chatId, edu
            );
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Bakalavr Yo'nalishini O'chirish")) {

            AdminStepMap.put(chatId, AdminStatus.DELETED_BAKALAVR_YONALISH);
            sendMessage.setText("O'chirmohchi bo'lgan yo'nalshingizni tanlang!");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardButtonUtil.educationBakalavrDirectoryDeleteMarkup(getDirectoryList()));

            EducationDegree educationDegree = educationDegreeService.getNameUz("Bakalavr");


            educationDirectoryStepMap.put(chatId, AdminStatus.DELETED_BAKALAVR_NAME_UZ);
            educationDirectoryMap.put(chatId,
                    new EducationDirectory(null, null, null, null, true, educationDegree));


            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Magistraturaga Yo'nalish Qo'shish")) {

            AdminStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA_YONALISH);
            sendMessage.setText("Magistratura yo'nalishini o'zbekcha variantini kiriting:");
            sendMessage.setChatId(chatId);

            EducationDegree educationDegree = educationDegreeService.getNameUz("Magistratura");


            educationDirectoryStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA_NAME_UZ);
            educationDirectoryMap.put(chatId,
                    new EducationDirectory(null, null, null, null, true, educationDegree));
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Magistratura Yo'nalishini O'chirish")) {

            AdminStepMap.put(chatId, AdminStatus.DELETED_MAGISTRATURA_YONALISH);
            AdminStepMap.get(chatId).equals(AdminStatus.DELETED_MAGISTRATURA_YONALISH);

            sendMessage.setText("O'chirmohchi bo'lgan yo'nalshingizni tanlang!");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryDeleteMarkup(getMagistraturaDirectoryList()));

            EducationDegree educationDegree = educationDegreeService.getNameUz("Magistratura");


            educationDirectoryStepMap.put(chatId, AdminStatus.DELETED_MAGISTRATURA_NAME_UZ);
            educationDirectoryMap.put(chatId,
                    new EducationDirectory(null, null, null, null, true, educationDegree));

            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("Bakalavr")) {
            AdminStepMap.get(chatId).equals(AdminStatus.ENTERED_BAKALAVR);
            sendMessage.setText("Bakalavr");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.educationBakalavrDirectoryMarkup(getDirectoryList()));
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            AdminStepMap.put(chatId, AdminStatus.ENTERED_BAKALAVR);

        } else if (text.equals("Magistratura")) {
            AdminStepMap.get(chatId).equals(AdminStatus.ENTERED_MAGISTRATURA);
            sendMessage.setText("Magistratura");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getMagistraturaDirectoryList()));
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            AdminStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA);

        } else if (text.equals("SHARTNOMA CRUD")) {
            AdminStepMap.put(chatId, AdminStatus.ENTERED_SHARTNOMA_CRUD);
            sendMessage.setText("Quyidagilardan birini tanlang!");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.SHARTNOMACRUD());
            univercityBot.sendMsg(sendMessage);

        } else if (text.equals("⬅ Ortga qaytish")) {

            if (AdminStepMap.get(chatId).equals(AdminStatus.ENTERED_YONALISH_CRUD)) {

                sendMessage.setText("Quyidagilardan birini tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

            } else if (AdminStepMap.get(chatId).equals(AdminStatus.ENTERED_BAKALAVR)) {

                sendMessage.setText("Quyidagilardan birini tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationDegreeMarkup(getEducationList()));
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                AdminStepMap.put(chatId, AdminStatus.ENTERED_YONALISH_CRUD);

            } else if (AdminStepMap.get(chatId).equals(AdminStatus.ENTERED_MAGISTRATURA)) {

                sendMessage.setText("Quyidagilardan birini tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationDegreeMarkup(getEducationList()));
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                AdminStepMap.put(chatId, AdminStatus.ENTERED_YONALISH_CRUD);

            } else if (AdminStepMap.get(chatId).equals(AdminStatus.DELETED_MAGISTRATURA_YONALISH)) {

                sendMessage.setText("Quyidagilardan birini tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getDirectoryList()));
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                AdminStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA);

            } else if (AdminStepMap.get(chatId).equals(AdminStatus.DELETED_MAGISTRATURA_YONALISH_ICHIDA)) {

                sendMessage.setText("Quyidagilardan birini tanlang!");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.educationMagistraturaDirectoryMarkup(getDirectoryList()));
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);

                AdminStepMap.put(chatId, AdminStatus.ENTERED_MAGISTRATURA);

            }
        }
    }

    public List<EducationDegree> getEducationList() {
        Iterable<EducationDegree> iterable = educationDegreeRepository.findAllByVisibleIsTrue();
        List<EducationDegree> educationDegreeList = new LinkedList<>();
        iterable.forEach(educationDegree -> educationDegreeList.add(toEducatinDegree(educationDegree)));
        return educationDegreeList;
    }

    public List<EducationDirectory> getDirectoryList() {
        EducationDegree educationDegree = educationDegreeRepository.findByNameUz("Bakalavr");
        Integer educationId = educationDegree.getId();
        Iterable<EducationDirectory> iterable = educationDirectoryRepository.findByEducationDegreeIdAndVisibleTrue(educationId);
        List<EducationDirectory> educationDirectoryList = new LinkedList<>();
        iterable.forEach(educationDirectory -> educationDirectoryList.add(toEducationDirectory(educationDirectory)));
        return educationDirectoryList;
    }

    public List<EducationDirectory> getShartnomaDirectoryList(String text) {
        EducationDegree educationDegree = educationDegreeRepository.findByNameUz(text);
        Integer educationId = educationDegree.getId();
        Iterable<EducationDirectory> iterable = educationDirectoryRepository.findByEducationDegreeIdAndVisibleTrue(educationId);
        List<EducationDirectory> educationDirectoryList = new LinkedList<>();
        iterable.forEach(educationDirectory -> educationDirectoryList.add(toEducationDirectory(educationDirectory)));
        return educationDirectoryList;
    }

    public List<EducationDirectory> getMagistraturaDirectoryList() {
        EducationDegree educationDegree = educationDegreeRepository.findByNameUz("Magistratura");
        Integer educationId = educationDegree.getId();
        Iterable<EducationDirectory> iterable = educationDirectoryRepository.findByEducationDegreeIdAndVisibleTrue(educationId);
        List<EducationDirectory> educationDirectoryList = new LinkedList<>();
        iterable.forEach(educationDirectory -> educationDirectoryList.add(toEducationDirectory(educationDirectory)));
        return educationDirectoryList;
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

    private EducationDirectory toEducationDirectory(EducationDirectory educationDirectory) {
        if (educationDirectory == null) {
            return null;
        } else {
            EducationDirectory educationDirectory1 = new EducationDirectory();
            educationDirectory1.setNameUZ(educationDirectory.getNameUZ());
            educationDirectory1.setNameRU(educationDirectory.getNameRU());
            educationDirectory1.setDirectoryId(educationDirectory.getDirectoryId());
            educationDirectory1.setVisible(educationDirectory.getVisible());
            return educationDirectory1;
        }
    }

    public void handleCallBack(User user, Message message, String data) {
        String chatId = String.valueOf(message.getChatId());

        if (data.equals("info_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Ma'lumotni kiritasizmi?"
            );
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.TypingForRequest());

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("typingforrequest_yes")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Ma'lumotni kiriting:"
            );

            infoImageStepMap.put(chatId, AdminStatus.TYPING_REQUEST);
            infoImageMap.put(chatId,
                    new InfoImage(null, null, null, null, null, true, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("mediaforrequest_yes")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Meidani jo'nating:");

            infoImageStepMap.put(chatId, AdminStatus.TYPING_MESSAGE_RU);

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("buttonforrequest_yes")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Button nomini jo'nating");

            infoImageStepMap.put(chatId, AdminStatus.ENTERED_FOTO);

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("enter_info_contact")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            contactConnectionStepMap.get(chatId).equals(AdminStatus.CLICKED_CONTACT_CONNECTION);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Manzilni o'zbekcha variantini kiriting:"
            );

            univercityBot.sendMsg(sendMessage);

            contactConnectionStepMap.put(chatId, AdminStatus.ENTERED_LOCATION_UZ);

        } else if (data.equals("edit_info_contact")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            ContactConnection contactConnection = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
            if (contactConnection != null) {
                SendMessage sendMessage = new SendMessage(
                        chatId, "O'zgartirmohchi bo'lgan qismingizni tanlang!"
                );

                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ContactConnectionUpdate());
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Tahrirlash uchun ma'lumotlar mavjud emas!"
                );
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ContactConnectionCRUD());
                univercityBot.sendMsg(sendMessage);
            }

        } else if (data.equals("delete_info_contact")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            ContactConnection contactConnection = contactConnectionService.getByMessageType(MessageType.BIZ_BILAN_BOGLANISH);
            if (contactConnection != null) {
                contactConnectionService.deleteInfo(contactConnection.getId());
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ContactConnectionCRUD());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ContactConnectionUpdate());
                univercityBot.sendMsg(sendMessage);
            }


        } else if (data.equals("manzil_uz_update_contact_connection")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Manzil uz ni yangisini kiriting!"
            );
            contactConnectionStepMap.put(chatId, AdminStatus.UPDATE_MANZIL_UZ);
            contactConnectionMap.put(chatId,
                    new ContactConnection(null, MessageType.BIZ_BILAN_BOGLANISH, null, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("manzil_ru_update_contact_connection")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Manzil ru ni yangisini kiriting!"
            );
            contactConnectionStepMap.put(chatId, AdminStatus.UPDATE_MANZIL_RU);
            contactConnectionMap.put(chatId,
                    new ContactConnection(null, MessageType.BIZ_BILAN_BOGLANISH, null, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("contact_update_contact_connection")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Telefon raqamni yangisini kiriting!"
            );

            contactConnectionStepMap.put(chatId, AdminStatus.UPDATE_CONTACT);
            contactConnectionMap.put(chatId,
                    new ContactConnection(null, MessageType.BIZ_BILAN_BOGLANISH, null, null, null, null));

            univercityBot.sendMsg(sendMessage);


        } else if (data.equals("rasm_update_contact_connection")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Rasmni yangisini jo'nating!"
            );
            contactConnectionStepMap.put(chatId, AdminStatus.UPDATE_IMAGE);
            contactConnectionMap.put(chatId,
                    new ContactConnection(null, MessageType.BIZ_BILAN_BOGLANISH, null, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("add_contact_commit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            ContactConnection contactConnection = contactConnectionMap.get(chatId);
            contactConnectionService.saveContactConnection(contactConnection);

            contactConnectionMap.remove(chatId);
            contactConnectionStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Ma'lumot bazaga saqlandi!"
            );

            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("add_contact_cancel")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            ContactConnection contactConnection = contactConnectionMap.get(chatId);

            contactConnectionMap.remove(chatId);
            contactConnectionStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, contactConnection.getLocationUz() + " " + "❌ bekor qilindi!\n\nQuyidagi amallardan birini tanlang:"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIUInfo());
            univercityBot.sendMsg(sendMessage);


        } else if (data.equals("add_message_biu_commit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            BiuUnivercity biuUnivercity = biuUnivercityMap.get(chatId);
            biuUnivercityService.saveMesage(biuUnivercity);

            biuUnivercityMap.remove(chatId);
            biuUnivercityStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Ma'lumot bazaga saqlandi!"
            );

            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("add_message_biu_cancel")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            BiuUnivercity biuUnivercity = biuUnivercityMap.get(chatId);

            biuUnivercityMap.remove(chatId);
            biuUnivercityStepMap.remove(chatId);


            SendMessage sendMessage = new SendMessage(
                    chatId, biuUnivercity.getInfoUz() + " " + "❌ bekor qilindi!\n\nQuyidagi amallardan birini tanlang:"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
            univercityBot.sendMsg(sendMessage);


        } else if (data.equals("shartnoma_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            AdminStepMap.put(chatId, AdminStatus.SHARTNOMA_QOSHISH);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Darajani tanlang!");
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(KeyboardButtonUtil.educationDegreeMarkup(getEducationList()));
            univercityBot.sendMsg(sendMessage);

            shartnomaInfoStepMap.put(chatId, AdminStatus.ENTERED_SHARTNOMA_DARAJA);
            shartnomaInfoMap.put(chatId,
                    new ShartnomaInfo(null, null, null, true, null));


        } else if (data.equals("back_from_ContactConnectionCrud")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );

            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("enter_info_abiturient")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Quyidagi yo'nalishlardan birini tanlang!"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegree());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("edit_info_abiturient")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Tahrirlash uchun quyidagi bo'limlardan birini tanlang!"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("bakalavr_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Quyidagilardan birini tanlang!"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeCreate());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("bakalavr_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Quyidagi vaqtlardan birini tanlang!"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("kunduzgi_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("O'zgartirmohchi bo'lgan qismingizni tanlang!");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.editDirectory());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoUz_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info uz ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_UZ_KUNDUZGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KUNDUZGI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoRu_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info Ru ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_RU_KUNDUZGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KUNDUZGI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("rasm_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Yangi rasmni jo'nating!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_IMAGE_KUNDUZGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KUNDUZGI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("kechgi_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("O'zgartirmohchi bo'lgan qismingizni tanlang!");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.editDirectoryKechgi());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoUz_update_kechgi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info uz ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_UZ_KECHGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KECHGI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoRu_update_kechgi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info Ru ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_RU_KECHGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KECHGI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("rasm_update_kechgi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Yangi rasmni jo'nating!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_IMAGE_KECHGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KECHGI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("sirtqi_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("O'zgartirmohchi bo'lgan qismingizni tanlang!");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.editDirectorySirtqi());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoUz_update_sirtqi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info uz ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_UZ_SIRTQI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.SIRTQI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoRu_update_sirtqi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info Ru ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_RU_SIRTQI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.SIRTQI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("rasm_update_sirtqi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Yangi rasmni jo'nating!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_IMAGE_SIRTQI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.SIRTQI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("add_directory_commit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            DirectoryInfo directoryInfo = directoryInfoMap.get(chatId);
            directoryInfoService.saveDirectory(directoryInfo);

            directoryInfoMap.remove(chatId);
            directoryInfoStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Ma'lumot bazaga saqlandi!"
            );

            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("add_directory_cancel")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            DirectoryInfo directoryInfo = directoryInfoMap.get(chatId);

            directoryInfoMap.remove(chatId);
            directoryInfoStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, directoryInfo.getInfoUz() + " " + "❌ bekor qilindi!\n\nQuyidagi amallardan birini tanlang:"
            );

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("kunduzgi_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ma'lumotni o'zbekcha variantini kiriting!");
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            directoryInfoStepMap.remove(chatId);
            directoryInfoMap.remove(chatId);

            directoryInfoStepMap.put(chatId, AdminStatus.CLICKED_DIRECTORY_INFO);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KUNDUZGI, null, null, null));

        } else if (data.equals("kechgi_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ma'lumotni o'zbekcha variantini kiriting!");
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            directoryInfoStepMap.remove(chatId);
            directoryInfoMap.remove(chatId);

            directoryInfoStepMap.put(chatId, AdminStatus.CLICKED_DIRECTORY_INFO_KECHGI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.KECHGI, null, null, null));

        } else if (data.equals("sirtqi_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ma'lumotni o'zbekcha variantini kiriting!");
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            directoryInfoStepMap.remove(chatId);
            directoryInfoMap.remove(chatId);

            directoryInfoStepMap.put(chatId, AdminStatus.CLICKED_DIRECTORY_INFO_SIRTQI);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.SIRTQI, null, null, null));

        } else if (data.equals("magistratura_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ma'lumotni o'zbekcha variantini kiriting!");
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

            directoryInfoStepMap.remove(chatId);
            directoryInfoMap.remove(chatId);

            directoryInfoStepMap.put(chatId, AdminStatus.CLICKED_DIRECTORY_INFO_MAGISTRATURA);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.MAGISTRATURA, null, null, null));

        } else if (data.equals("magistratura_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Tahrirlamohchi bo'lgan qismingizni tanlang!");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.editDirectoryMagistratura());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoUz_update_magistratura")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info uz ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_UZ_MAGISTRATURA);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.MAGISTRATURA, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("infoRu_update_magistratura")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Info Ru ni yangisini kiriting!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_INFO_RU_MAGISTRATURA);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.MAGISTRATURA, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("rasm_update_magistratura")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Yangi rasmni jo'nating!"
            );
            directoryInfoStepMap.put(chatId, AdminStatus.UPDATE_IMAGE_MAGISTRATURA);
            directoryInfoMap.put(chatId,
                    new DirectoryInfo(null, MessageType.MAGISTRATURA, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("delete_info_abiturient")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Quyidagilardan birini tanlang!"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeDelete());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("litsenziya_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);
            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.LITSENZIYA);
            if (biuUnivercity != null) {
                biuUnivercityService.deleteLitsenziya(MessageType.LITSENZIYA);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                univercityBot.sendMsg(sendMessage);
            }

        } else if (data.equals("biu_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);
            if (!biuUnivercity.isEmpty()) {
                for (BiuUnivercity b : biuUnivercity) {
                    biuUnivercityService.deleteInfo(b.getId());
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                univercityBot.sendMsg(sendMessage);
            }

        } else if (data.equals("magistratura_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.MAGISTRATURA);
            if (directoryInfo != null) {
                directoryInfoService.deleteInfo(directoryInfo.getId());
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeDelete());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeDelete());
                univercityBot.sendMsg(sendMessage);
            }

        } else if (data.equals("bakalavr_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Qaysi birini o'chirishni istaysiz?"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeDelete());
            sendMessage.setChatId(chatId);
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("kunduzgi_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.KUNDUZGI);
            if (directoryInfo != null) {
                directoryInfoService.deleteInfo(directoryInfo.getId());
                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeDelete());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage1.setChatId(chatId);
                sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeDelete());
                univercityBot.sendMsg(sendMessage1);
            }
        } else if (data.equals("kechgi_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();

            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.KECHGI);
            if (directoryInfo != null) {
                directoryInfoService.deleteInfo(directoryInfo.getId());

                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeDelete());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage1.setChatId(chatId);
                sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeDelete());
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (data.equals("sirtqi_delete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            DirectoryInfo directoryInfo = directoryInfoService.getByMessageType(MessageType.SIRTQI);
            if (directoryInfo != null) {
                directoryInfoService.deleteInfo(directoryInfo.getId());

                sendMessage.setText("Ma'lumot muvaffaqiyatli o'chirildi!");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeDelete());
                sendMessage.setChatId(chatId);
                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage1 = new SendMessage(
                        chatId, "Ma'lumotlar topilmadi!"
                );
                sendMessage1.setChatId(chatId);
                sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeDelete());
                univercityBot.sendMsg(sendMessage1);
            }

        } else if (data.equals("litsenziya_create")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Litsenziya rasmini jo'nating!"
            );

            biuUnivercityStepMap.put(chatId, AdminStatus.ENTERED_LITSENZIYA);
            biuUnivercityMap.put(chatId,
                    new BiuUnivercity(null, MessageType.LITSENZIYA, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("biu_plus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Ma'lumotni o'zbekcha variantini kiriting!"
            );

            biuUnivercityStepMap.put(chatId, AdminStatus.ENTERED_INFO_UZ_BIU);
            biuUnivercityMap.put(chatId,
                    new BiuUnivercity(null, MessageType.BIU_OLIYGOHI, null, null, null));

            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("biu_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Tahrirlamohchi bo'lgan qismingizni tanlang"
            );

            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity_update());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("biu_infoUz_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);

            if (biuUnivercity != null) {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Ma'lumotni yangi o'zbekcha variantini kiriting!"
                );
                biuUnivercityStepMap.put(chatId, AdminStatus.UPDATE_INFO_UZ_BIU);
                biuUnivercityMap.put(chatId,
                        new BiuUnivercity(null, MessageType.BIU_OLIYGOHI, null, null, null));

                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Tahrirlash uchun ma'lumotlar mavjud emas!"
                );
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                univercityBot.sendMsg(sendMessage);
            }

        } else if (data.equals("biu_infoRu_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);

            if (biuUnivercity != null) {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Ma'lumotni yangi ruscha variantini kiriting!"
                );
                biuUnivercityStepMap.put(chatId, AdminStatus.UPDATE_INFO_RU_BIU);
                biuUnivercityMap.put(chatId,
                        new BiuUnivercity(null, MessageType.BIU_OLIYGOHI, null, null, null));

                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Tahrirlash uchun ma'lumotlar mavjud emas!"
                );
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                univercityBot.sendMsg(sendMessage);
            }

        } else if (data.equals("biu_image_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            List<BiuUnivercity> biuUnivercity = biuUnivercityService.getByMessageType(MessageType.BIU_OLIYGOHI);

            if (biuUnivercity != null) {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Yangi rasmni jo'nating!"
                );
                biuUnivercityStepMap.put(chatId, AdminStatus.UPDATE_IMAGE_BIU);
                biuUnivercityMap.put(chatId,
                        new BiuUnivercity(null, MessageType.BIU_OLIYGOHI, null, null, null));

                univercityBot.sendMsg(sendMessage);
            } else {
                SendMessage sendMessage = new SendMessage(
                        chatId, "Tahrirlash uchun ma'lumotlar mavjud emas!"
                );
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
                univercityBot.sendMsg(sendMessage);
            }
        } else if (data.equals("back_from_AbiturientlarCRUD")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_AbiturientlarDegree")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarCRUD());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_edutypeCreate")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegree());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_AbiturientlarDegreeEdit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarCRUD());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_edutypeEdit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("from_editDirectoryMagistratura")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_AbiturientlarDegreeDelete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarCRUD());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_edutypeDelete")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.AbiturientlarDegreeDelete());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("from_ContactConnectionUpdate")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.ContactConnectionCRUD());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("from_editDirectory")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("from_editDirectoryKechgi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("from_editDirectorySirtqi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.EduTypeEdit());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("back_from_BIU")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.startAdmin());
            univercityBot.sendMsg(sendMessage);

        } else if (data.equals("from_BIU_Univercity_update")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            univercityBot.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi✅"
            );
            univercityBot.sendMsg(sendMessage);

            sendMessage.setText("Quyidagilardan yo'nalishlardan birini tanlang\uD83D\uDC47");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.BIU_Univercity());
            univercityBot.sendMsg(sendMessage);
        }
    }
}
