package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query(value = "update users u set u.enabled = 0 where u.user_id = :id", nativeQuery = true) //TODO
    void deleteById(@Param("id") Long id);
}
