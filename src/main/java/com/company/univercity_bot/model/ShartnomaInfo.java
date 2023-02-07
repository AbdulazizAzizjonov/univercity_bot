package com.company.univercity_bot.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "shartnoma_info")
public class ShartnomaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String shartnomaUz;

    @Column
    private String shartnomaRu;

    @Column
    private Boolean visible;

    @ManyToOne()
    @JoinColumn(name = "education_directory_id", referencedColumnName = "id")
    private EducationDirectory educationDirectory;

}
