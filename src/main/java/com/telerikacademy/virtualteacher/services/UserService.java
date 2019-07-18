package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> save(User user);

    //Optional<User> update(Long id, User user);

    void deleteById(Long id);
}
