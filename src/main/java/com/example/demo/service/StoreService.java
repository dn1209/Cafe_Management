package com.example.demo.service;

import com.example.demo.model.Store;

public interface StoreService {
     Store saveStore(String nameStore);

     boolean checkStoreName(String userName);

     Store findStoreByUserName(String userName);

}
