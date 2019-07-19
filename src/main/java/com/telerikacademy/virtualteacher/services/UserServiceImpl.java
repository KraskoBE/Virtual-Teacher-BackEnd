package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("UserService")
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper,
                           UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> save(UserRequestDTO user) {
        if (!isEmailAvailable(user.getEmail()))
            return Optional.empty();
        return Optional.of(userRepository.save(modelMapper.map(user, User.class)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private boolean isEmailAvailable(String email) {
        return findAll().stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }
}
