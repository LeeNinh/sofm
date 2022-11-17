package com.example.ninhdemo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// PK

    @ManyToOne
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bill bill;

    private Integer quantity;

    private Double buyPrice;

    @CreatedDate // tu gen
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
}