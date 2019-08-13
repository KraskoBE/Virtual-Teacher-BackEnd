package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("update User u set u.enabled = false where u.id= :userId")
    void deleteById(@Param("userId") Long id);

    Optional<User> findByEmailIgnoreCase(String email);
}
