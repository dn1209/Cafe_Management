package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.time.LocalDate;
@Entity
@Table(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "status")
    private int status;


}