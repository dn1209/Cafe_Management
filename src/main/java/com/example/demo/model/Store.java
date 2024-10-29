package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Category> categories;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Bill> bills;

    public Store(){
    }


    public Store(String name) {
        this.storeName = name;

    }
}