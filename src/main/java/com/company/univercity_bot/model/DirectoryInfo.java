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
@Table(name = "directory_info")
public class DirectoryInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column
    private String InfoUz;

    @Column
    private String InfoRu;

    @Column
    private String image;
}
