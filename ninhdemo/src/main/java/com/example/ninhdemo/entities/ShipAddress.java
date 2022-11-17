package com.example.ninhdemo.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ShipAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    private String nameReceiver;

    private String address;

    private String phone;
}