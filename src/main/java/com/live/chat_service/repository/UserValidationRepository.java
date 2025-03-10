package com.live.chat_service.repository;

import com.live.chat_service.model.UserValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserValidationRepository extends JpaRepository<UserValidation,Long> {

    //@Query("SELECT u FROM UserValidation u WHERE u.email=:userEmail")
    Optional<UserValidation> findByEmail(String userEmail);

}
