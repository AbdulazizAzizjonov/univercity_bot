package com.company.univercity_bot.model;

import com.company.univercity_bot.enums.LanguacheEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class Users {

    @Id
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String userName;

    @Column
    private boolean isAdmin;

    @Column
    private boolean isEmployee;

    @Column
    private boolean isUser;

    @Column
    @Enumerated(EnumType.STRING)
    private LanguacheEnums languacheEnums=LanguacheEnums.UZ;

    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdDate;

}
