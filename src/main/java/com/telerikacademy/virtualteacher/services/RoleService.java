package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;


public interface RoleService {

    User setRole(User currentUser, Long targetUserId, Role.Name role);

}
