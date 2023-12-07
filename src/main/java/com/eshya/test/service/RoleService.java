package com.eshya.test.service;


import com.eshya.test.model.Role;
import com.eshya.test.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // findbyName
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
