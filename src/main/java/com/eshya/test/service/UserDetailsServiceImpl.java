package com.eshya.test.service;

import com.eshya.test.model.UserPortal;
import com.eshya.test.repository.UserPortalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserPortalRepository userPortalRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserPortal> userPortalOptional = userPortalRepo.findByUsernameWithRoles(username);
        UserPortal userPortal = userPortalOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found. username: " + username));

        return UserDetailsImpl.build(userPortal);
    }
}
