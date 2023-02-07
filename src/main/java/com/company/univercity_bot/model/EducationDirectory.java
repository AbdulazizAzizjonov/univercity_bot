package com.company.univercity_bot.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "education_directory")
public class EducationDirectory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String nameUZ;

    @Column
    private String infoUZ;

    @Column
    private String nameRU;

    @Column
    private String infoRU;

    @Column(unique = true)
    private String directoryId;


    @Column
    private Boolean visible;

    @ManyToOne()
    @JoinColumn(name = "education_degree_id", referencedColumnName = "id")
    private EducationDegree educationDegree;
}
