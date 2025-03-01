package com.example.demo.model;

import java.util.Collections;
import java.util.Map;

public class Message {
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String INVALID_USER = "INVALID_USER";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String CREATE_USER_SUCCESS = "CREATE_USER_SUCCESS";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";

    public static final String CHANGE_PASSWORD_SUCCESS = "CHANGE_PASSWORD_SUCCESS";
    public static final String UPDATE_USER_SUCCESS = "UPDATE_USER_SUCCESS";
    //product
    public static final String CREATE_PRODUCT_SUCCESS = "CREATE_PRODUCT_SUCCESS";
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    public static final String UPDATE_PRODUCT_SUCCESS = "UPDATE_PRODUCT_SUCCESS";
    public static final String PRODUCT_NAME_EXISTED = "PRODUCT_NAME_EXISTED";
    //category
    public static final String CREATE_CATEGORY_SUCCESS = "CREATE_CATEGORY_SUCCESS";
    public static final String CATEGORY_NOT_FOUND = "CATEGORY_NOT_FOUND";
    public static final String CATEGORY_NAME_EXISTED = "CATEGORY_NAME_EXISTED";
    public static final String UPDATE_CATEGORY_SUCCESS = "UPDATE_CATEGORY_SUCCESS";
    public static final String INVALID_CATEGORY_NAME = "INVALID_CATEGORY_NAME";
    public static final String UPDATE_STATUS_SUCCESS = "UPDATE_STATUS_SUCCESS";
    //bill
    public static final String CREATE_BILL_SUCCESS = "CREATE_BILL_SUCCESS";
    public static final String DETAIL_BILL_NOT_FOUND = "DETAIL_BILL_NOT_FOUND";

    public static Map<String, String> map(String message) {
        return Collections.singletonMap("message", message);
    }
}
