package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    User findUserByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.userId = ?1")
    User findUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.userName = ?1 AND u.password = ?2")
    User findUserByUserNameAndPassword(String userName, String password);

    @Query("SELECT u.storeId FROM User u WHERE u.userId = :userId")
    Optional<Long> findStoreIdByUserId( Long userId);


    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM User u WHERE u.userName = :userName) THEN true ELSE false END")
    boolean existsByUserName(String userName);
    //true neu tai khoan da ton tai false neu chua ton tai


    @Modifying
    @Query("UPDATE User u set u.userStatus = '1' WHERE u.email = :email")
    void updateUserByEmail(String email);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM User u WHERE u.userId = :id AND u.userStatus = '0') THEN true ELSE false END")
    boolean existsByIdAndUserStatusIsZero(Long id);

}
