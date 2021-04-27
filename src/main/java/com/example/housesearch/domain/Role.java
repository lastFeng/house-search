package com.example.housesearch.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 角色对象
 */
@Entity(name = "role")
@Table(name = "role")
@NoArgsConstructor
@Data
@Builder
@ToString
public class Role implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**用户*/
    @Column(name = "user_id")
    private Long userId;

    /**角色名称*/
    private String name;
}
