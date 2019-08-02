package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.RoleRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("RoleService")
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public User setRole(User currentUser, Long targetUserId, Role.Name role) {
        User target = getUser(targetUserId);

        Collection<Role> newRoles = new HashSet<>();

        switch (role) {
            case Admin:
                newRoles.add(getRole(Role.Name.Admin));
            case Teacher:
                newRoles.add(getRole(Role.Name.Teacher));
            case Student:
                newRoles.add(getRole(Role.Name.Student));
                break;
            default:
                throw new BadRequestException("No such role exists");
        }

        target.setRoles(newRoles);
        return userRepository.save(target);
    }

    private Role getRole(Role.Name role) {
        return roleRepository.findByName(role.name())
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
