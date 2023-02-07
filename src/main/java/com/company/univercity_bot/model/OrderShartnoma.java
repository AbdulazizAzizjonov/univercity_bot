package com.company.univercity_bot.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "order_shartnoma")
public class OrderShartnoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String student;

    @ManyToOne()
    @JoinColumn(name = "shartnoma_id", referencedColumnName = "id")
    private ShartnomaInfo shartnomaInfo;
}
