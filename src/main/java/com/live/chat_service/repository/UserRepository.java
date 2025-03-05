package com.live.chat_service.repository;

import com.live.chat_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.emailId=:email AND u.isActive=true ")
    Optional<User> findByEmailId(String email);

    @Query("SELECT u FROM User u WHERE u.id=:id AND u.isActive=true ")
    Optional<User> findByIdIsActive(Long id);

    @Query("SELECT u FROM User u WHERE u.emailId = :email AND u.isActive=true")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.deletedFlag = false")
    List<User> findAllIsActive();


    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.userName LIKE %:search% ")
    List<User> findByName(String search);


}
