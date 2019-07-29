package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.TeacherRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRequestRepository extends JpaRepository<TeacherRequest, Long> {

    @Modifying
    @Query("update TeacherRequest tr set tr.enabled = false where tr.id= :teacherRequestId")
    void deleteById(@Param("teacherRequestId") Long id);

}
