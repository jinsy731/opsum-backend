package com.opusm.backend.customer;

import com.opusm.backend.common.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Customer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int points;
    private int assets;
}
