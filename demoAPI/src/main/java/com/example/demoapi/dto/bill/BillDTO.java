package com.example.demoapi.dto.bill;

import com.example.demoapi.entities.User;
import lombok.Data;

@Data
public class BillDTO {

    private int id;

    private String buyDate;

    User user;
}
