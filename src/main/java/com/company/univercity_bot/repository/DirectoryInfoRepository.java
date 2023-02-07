package com.company.univercity_bot.repository;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.ContactConnection;
import com.company.univercity_bot.model.DirectoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DirectoryInfoRepository extends JpaRepository<DirectoryInfo, Integer> {

    DirectoryInfo findByMessageType(MessageType type);

    @Transactional
    @Modifying
    @Query("update DirectoryInfo  d set d.InfoUz = ?1 where d.messageType = ?2")
    int updateInfoUzByMessageType(String info_uz, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update DirectoryInfo  d set d.InfoRu = ?1 where d.messageType = ?2")
    int updateInfoRuByMessageType(String info_uz, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update DirectoryInfo  d set d.image = ?1 where d.messageType = ?2")
    int updateImageByMessageType(String image, MessageType messageType);
}
