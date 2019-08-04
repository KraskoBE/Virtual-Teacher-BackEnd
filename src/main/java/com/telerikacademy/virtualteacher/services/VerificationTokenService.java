package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.VerificationToken;

public interface VerificationTokenService {

    VerificationToken findById(Long id);

}
