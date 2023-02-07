package com.company.univercity_bot.service;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.BiuUnivercity;
import com.company.univercity_bot.model.ContactConnection;
import com.company.univercity_bot.repository.BiuUnivercityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiuUnivercityService {

    @Autowired
    private BiuUnivercityRepository biuUnivercityRepository;

    public BiuUnivercity saveMesage (BiuUnivercity biuUnivercity) {
        BiuUnivercity biuUnivercity1 = new BiuUnivercity();
        biuUnivercity1.setInfoUz(biuUnivercity.getInfoUz());
        biuUnivercity1.setInfoRu(biuUnivercity.getInfoRu());
        biuUnivercity1.setMessageType(biuUnivercity.getMessageType());
        biuUnivercity1.setMediaId(biuUnivercity.getMediaId());

        BiuUnivercity save = biuUnivercityRepository.save(biuUnivercity1);

        return save;
    }


    public List<BiuUnivercity> getByMessageType(MessageType messageType) {
        return biuUnivercityRepository.findAllByMessageType(messageType);
    }


    public int updateInfoUZ (String infoUz, MessageType messageType){
        return biuUnivercityRepository.updateInfoUzByMessageType(infoUz, messageType);
    }

    public int updateInfoRU (String infoRu, MessageType messageType){
        return biuUnivercityRepository.updateInfoRuByMessageType(infoRu, messageType);
    }

    public int updateImage (String image, MessageType messageType){
        return biuUnivercityRepository.updateImageByMessageType(image, messageType);
    }

    public void deleteInfo (Integer id) {
        biuUnivercityRepository.deleteById(id);
    }
    public void deleteLitsenziya (MessageType messageType) {
        biuUnivercityRepository.deleteLitsenziya(messageType);
    }

}
