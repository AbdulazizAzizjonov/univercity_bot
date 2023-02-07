package com.company.univercity_bot.model;

import com.company.univercity_bot.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "info_image")
public class InfoImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String infoUZ;

    @Column
    private String infoRU;

    @Column
    private String mediaId;

    @Column
    private MessageType messageType;

    @Column
    private boolean isButton;

    @Column
    private String buttonName;

    @Column
    private String buttonUrl;


}

