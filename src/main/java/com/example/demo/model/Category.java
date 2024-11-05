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
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "status")
    private int status;


}