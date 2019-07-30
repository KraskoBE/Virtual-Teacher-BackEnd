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
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public TeacherRequest save(Long userId) {
        User user  = getUser(userId);

        checkIfAlreadyExists(user);

        TeacherRequest newTeacherRequest = new TeacherRequest(user);

        return teacherRequestRepository.save(newTeacherRequest);
    }

    @Override
    public List<TeacherRequest> findAll() {
        return teacherRequestRepository.findAll();
    }

    @Override
    public Optional<TeacherRequest> findById(Long id){
        return teacherRequestRepository.findById(id);
    }

    @Override
    public Optional<TeacherRequest> findByUserId(Long userId){
        User user = getUser(userId);
        return teacherRequestRepository.findByUser(user);
    }

    @Override
    public void deleteById(Long id) {
        teacherRequestRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByUserId(Long userId) {
        User user = getUser(userId);
        teacherRequestRepository.deleteByUser(user);
    }

    @Override
    public Optional<TeacherRequest> acceptById(Long id) {
        TeacherRequest toAccept = getTeacherRequest(id);
        User user = toAccept.getUser();
        Role teacher = getRole(Role.TEACHER);

        if ( user.getRoles().contains(teacher) ) {
            throw new BadRequestException("Already a teacher");
        }

        user.getRoles().add(teacher);
        userRepository.save(user);

        toAccept.setAccepted(true);
        return Optional.of(teacherRequestRepository.save(toAccept));
    }

    @Override
    public Optional<TeacherRequest> acceptByUserId(Long userId) {
        return acceptById(getTeacherRequestByUserId(userId).getId());
    }

    //

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private TeacherRequest getTeacherRequestByUserId(Long id) {
        User user = getUser(id);
        return teacherRequestRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("TeacherRequest not found"));
    }

    private TeacherRequest getTeacherRequest(Long id) {
        return teacherRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TeacherRequest not found"));
    }

    private Role getRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

    private void checkIfAlreadyExists(User user) {
        if (teacherRequestRepository.findByUser(user).isPresent())
            throw new AlreadyExistsException("You have already requested to become a teacher");
    }

}
