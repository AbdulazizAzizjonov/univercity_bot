package com.company.univercity_bot.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String gender;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String passportNumber;

    @Column
    private String takePassportDay;

    @Column
    private String birthday;

    @Column
    private String educationDegree;

    @Column
    private String educationType;

    @Column
    private String edu_location;

    @Column
    private String manzil;

    @Column
    private String schoolType;

    @Column
    private String fathPhone;

    @Column
    private String passportImage;

    @Column
    private String pImage;

    @Column
    private String image;

    @Column()
    private String UserNumber;

    @ManyToOne()
    @JoinColumn(name = "education_directory_id", referencedColumnName = "id")
    private EducationDirectory educationDirectory;

}
