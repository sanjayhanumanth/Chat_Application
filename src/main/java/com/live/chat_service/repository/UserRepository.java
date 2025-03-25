package com.live.chat_service.repository;

import com.live.chat_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

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



    @Query("SELECT u FROM User u WHERE u.isActive = true AND (u.userName LIKE %:search%  or u.displayName LIKE %:search%)")
    List<User> findByName(String search);


    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.id <> :userId ORDER BY u.id DESC")
    List<User> findDefaultUsers(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.id IN :userLists")
    List<User> findAllIsActiveTrue(List<Long> userLists, Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.displayName ASC")
    List<User> findByDisplayName();


    List<User> findByIsActiveTrueOrderByDisplayNameAsc();

    List<User> findByIsActiveTrue();


    List<User> findByUserNameIgnoreCaseContainingOrDisplayNameIgnoreCaseContaining(String search, String search1);
}
