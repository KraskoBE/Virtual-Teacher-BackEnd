package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.VerificationToken;
import com.telerikacademy.virtualteacher.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service("VerificationTokenService")
@AllArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService{

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken findById(Long id) {
        return verificationTokenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("VerificationToken not found"));
    }
}
