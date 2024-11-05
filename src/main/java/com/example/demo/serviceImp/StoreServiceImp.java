package com.example.demo.serviceImp;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Store;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreServiceImp implements StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Store saveStore(String nameStore) {
        Store store = new Store(nameStore);
        return storeRepository.save(store);
    }

    @Override
    public boolean checkStoreName(String userName) {
        return storeRepository.existsByStoreUserName(userName);
    }

    @Override
    public Store findStoreByUserName(String userName) {
        if(checkStoreName(userName)){
            System.out.println("save moi");
            saveStore(userName);
        }
        System.out.println("tiep tuc");
        Store store = storeRepository.findStoreByUserName(userName);
        if( store == null){
            throw new UserNotFoundException("Store not found with username: " + userName);
        }
        return store;
    }


}
