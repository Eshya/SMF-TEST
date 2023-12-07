package com.eshya.test.service;

import com.eshya.test.model.UserPortal;
import com.eshya.test.repository.UserPortalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPortalService {
    @Autowired
    private UserPortalRepository userPortalRepo;

    public Optional<UserPortal> findByUsername(String username) {
        // TODO Auto-generated method stub
        return userPortalRepo.findByUsername(username);
    }

    public UserPortal save(UserPortal userPortal) {
        return userPortalRepo.save(userPortal);
    }
    public Optional<Object> findByToken(String token) {
        return userPortalRepo.findByToken(token);
    }
}
