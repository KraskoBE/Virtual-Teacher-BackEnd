package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("update Notification l set l.enabled = false where l.id= :notificationId")
    void deleteById(@Param("notificationId") Long notificationId);

}
