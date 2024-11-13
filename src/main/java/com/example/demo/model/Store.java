package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "store")
@Data
public class Store {
    @Id
    @SequenceGenerator(
            name = "store_sequence",
            sequenceName = "store_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "store_sequence"
    )
    @Column(name = "store_id")
    private long storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "store_status")
    private Integer storeStatus;

    @Column(name = "store_created_date")
    private LocalDate storeCreatedDate;

    @Column(name = "store_updated_date")
    private LocalDate storeUpdatedDate;

    public Store(){
    }


    public Store(String name) {
        this.storeName = name;

    }
}