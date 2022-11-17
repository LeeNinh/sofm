package com.example.ninhdemo.dto;

import com.example.ninhdemo.entities.User;
import lombok.Data;

@Data
public class UserRoleDTO {

    private Integer id;

    private User user;

    private String role;// ADMIN,MEMBER}
}
