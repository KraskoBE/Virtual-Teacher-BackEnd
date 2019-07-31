package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.TeacherRequest;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.RoleRepository;
import com.telerikacademy.virtualteacher.repositories.TeacherRequestRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("TeacherRequestService")
@AllArgsConstructor
public class TeacherRequestServiceImpl implements TeacherRequestService {

    private TeacherRequestRepository teacherRequestRepository;
    private UserService userService;

    @Override
    public TeacherRequest save(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        checkIfAlreadyExists(user);

        TeacherRequest newTeacherRequest = new TeacherRequest(user);

        return teacherRequestRepository.save(newTeacherRequest);
    }

    @Override
    public List<TeacherRequest> findAll() {
        return teacherRequestRepository.findAll();
    }

    @Override
    public Optional<TeacherRequest> findById(Long id) {
        return teacherRequestRepository.findById(id);
    }

    @Override
    public Optional<TeacherRequest> findByUserId(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return teacherRequestRepository.findByUser(user);
    }

    @Override
    public void deleteById(Long id) {
        teacherRequestRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByUserId(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        teacherRequestRepository.deleteByUser(user);
    }

    @Override
    public TeacherRequest acceptByUserId(Long userId) {
        TeacherRequest teacherRequest = findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Teacher request not found"));

        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userService.hasRole(user, Role.Name.Teacher))
            throw new BadRequestException("Already a teacher");

        userService.addRole(user, Role.Name.Teacher);

        teacherRequest.setAccepted(true);
        return teacherRequestRepository.save(teacherRequest);
    }

    //
    private void checkIfAlreadyExists(User user) {
        if (teacherRequestRepository.findByUser(user).isPresent())
            throw new AlreadyExistsException("You have already requested to become a teacher");
    }

}
