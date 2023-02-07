package com.company.univercity_bot.repository;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.BiuUnivercity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository

public interface BiuUnivercityRepository extends JpaRepository<BiuUnivercity, Integer> {

    List<BiuUnivercity>  findAllByMessageType (MessageType type);


    @Transactional
    @Modifying
    @Query("update BiuUnivercity  d set d.InfoUz = ?1 where d.messageType = ?2")
    int updateInfoUzByMessageType(String info_uz, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update BiuUnivercity  d set d.InfoRu = ?1 where d.messageType = ?2")
    int updateInfoRuByMessageType(String info_uz, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update BiuUnivercity  d set d.mediaId = ?1 where d.messageType = ?2")
    int updateImageByMessageType(String image, MessageType messageType);

    @Transactional
    @Modifying
    @Query("delete from BiuUnivercity d where d.messageType = ?1")
    int deleteLitsenziya( MessageType messageType);
}
