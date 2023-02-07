package com.company.univercity_bot.repository;

import com.company.univercity_bot.enums.MessageType;
import com.company.univercity_bot.model.ContactConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ContactConnectionRepository extends JpaRepository<ContactConnection, Integer> {
    ContactConnection findByMessageType(MessageType type);

    @Transactional
    @Modifying
    @Query("update ContactConnection  c set c.locationUz = ?1 where c.messageType = ?2")
    int updateLocationUzByMessageType(String location_uz, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update ContactConnection  c set c.locationRu = ?1 where c.messageType = ?2")
    int updateLocationRuByMessageType(String location_ru, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update ContactConnection  c set c.contact = ?1 where c.messageType = ?2")
    int updateContactByMessageType(String contact, MessageType messageType);

    @Transactional
    @Modifying
    @Query("update ContactConnection  c set c.image = ?1 where c.messageType = ?2")
    int updateImageByMessageType(String image, MessageType messageType);
}
