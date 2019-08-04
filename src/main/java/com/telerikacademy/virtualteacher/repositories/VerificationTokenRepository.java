package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

}
