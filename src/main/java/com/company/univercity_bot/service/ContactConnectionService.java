package com.company.univercity_bot.service;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.ContactConnection;
import com.company.univercity_bot.repository.ContactConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.Optional;

@Service
public class ContactConnectionService {

    @Autowired
    private ContactConnectionRepository contactConnectionRepository;

    public ContactConnection saveContactConnection (ContactConnection contactConnection) {
        ContactConnection contactConnection1 = new ContactConnection();
        contactConnection1.setMessageType(contactConnection.getMessageType());
        contactConnection1.setLocationUz(contactConnection.getLocationUz());
        contactConnection1.setLocationRu(contactConnection.getLocationRu());
        contactConnection1.setContact(contactConnection.getContact());
        contactConnection1.setImage(contactConnection.getImage());

        ContactConnection save = contactConnectionRepository.save(contactConnection1);

        return save;
    }

    public ContactConnection getByMessageType(MessageType messageType) {
        return contactConnectionRepository.findByMessageType(messageType);
    }

    public int updateLocationUZ (String infoUz, MessageType messageType){
        return contactConnectionRepository.updateLocationUzByMessageType(infoUz, messageType);
    }

    public int updateLocationRU (String infoRu, MessageType messageType){
        return contactConnectionRepository.updateLocationRuByMessageType(infoRu, messageType);
    }

    public int updateContact (String contact, MessageType messageType){
        return contactConnectionRepository.updateContactByMessageType(contact, messageType);
    }

    public int updateImage (String image, MessageType messageType){
        return contactConnectionRepository.updateImageByMessageType(image, messageType);
    }

    public void deleteInfo (Integer id) {
        contactConnectionRepository.deleteById(id);
    }
}
