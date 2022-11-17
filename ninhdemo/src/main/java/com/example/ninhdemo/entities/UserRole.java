package com.example.ninhdemo.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

//@RequestBody
// {
//"user": {
//    "id" : 11
//},
//"role":"ADMIN"
//
//}
@Entity
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    private User user;

    private String role;// ADMIN,MEMBER
}
