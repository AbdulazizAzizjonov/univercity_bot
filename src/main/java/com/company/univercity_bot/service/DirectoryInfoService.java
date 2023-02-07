package com.company.univercity_bot.service;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.ContactConnection;
import com.company.univercity_bot.model.DirectoryInfo;
import com.company.univercity_bot.repository.DirectoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectoryInfoService {

    @Autowired
    private DirectoryInfoRepository directoryInfoRepository;

    public DirectoryInfo saveDirectory (DirectoryInfo directoryInfo) {
        DirectoryInfo directoryInfo1 = new DirectoryInfo();
        directoryInfo1.setMessageType(directoryInfo.getMessageType());
        directoryInfo1.setInfoUz(directoryInfo.getInfoUz());
        directoryInfo1.setInfoRu(directoryInfo.getInfoRu());
        directoryInfo1.setImage(directoryInfo.getImage());

        DirectoryInfo save = directoryInfoRepository.save(directoryInfo1);

        return save;
    }

    public DirectoryInfo getByMessageType(MessageType bizBilanBoglanish) {
        return directoryInfoRepository.findByMessageType(bizBilanBoglanish);
    }

    public int updateInfoUZ (String infoUz, MessageType messageType){
        return directoryInfoRepository.updateInfoUzByMessageType(infoUz, messageType);
    }

    public int updateInfoRU (String infoRu, MessageType messageType){
        return directoryInfoRepository.updateInfoRuByMessageType(infoRu, messageType);
    }

    public int updateImage (String image, MessageType messageType){
        return directoryInfoRepository.updateImageByMessageType(image, messageType);
    }

    public void deleteInfo (Integer id) {
        directoryInfoRepository.deleteById(id);
    }
}
