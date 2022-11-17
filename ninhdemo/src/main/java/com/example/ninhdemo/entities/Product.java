package com.example.ninhdemo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String image;// URL

    private String description;

    private Double price;

    @JsonIgnore
    @Transient // field is not persistent.
    private MultipartFile file;

    @CreatedDate // tu gen
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;

    @ManyToOne
    private Category category;
}
