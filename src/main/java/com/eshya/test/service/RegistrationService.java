package com.eshya.test.service;

import com.eshya.test.dto.RegistrationDTO;
import com.eshya.test.exception.BadRequestException;
import com.eshya.test.model.Role;
import com.eshya.test.model.UserPortal;
import com.eshya.test.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.eshya.test.utils.Constant;

@Service
public class RegistrationService {

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final UserPortalService userPortalService;
    private final RoleService roleService;

    @Autowired
    public RegistrationService(UserPortalService userPortalService, RoleService roleService) {
        this.userPortalService = userPortalService;
        this.roleService = roleService;
    }

    public RegistrationDTO register(UserPortal userPortal) {
        logger.info("Registering user: {}", userPortal.getUsername());

        // decrypt password
        String decPasswd = userPortal.getPassword();

        logger.info("registration performed");
        RegistrationDTO registrationDTO = new RegistrationDTO();
        Optional<UserPortal> optUsrPortal = userPortalService.findByUsername(registrationDTO.getUsername());
        if(optUsrPortal.isPresent()) {
            logger.info("username {} already exist", userPortal.getUsername());
            throw new BadRequestException("username already exist");
        }

        userPortal.setPassword(Tools.encrypt(decPasswd)); // encrypt before save or update

        Role roles = roleService.findByName(Constant.ROLE_USER).orElseThrow(() -> new BadRequestException("Role not found"));
        userPortal.setRole(roles);
        userPortalService.save(userPortal);

        registrationDTO.setUserPortalId(userPortal.getId());
        registrationDTO.setRegistrationDate(userPortal.getCreationDate());
        registrationDTO.setRole(userPortal.getRole());
        registrationDTO.setModifiedDate(userPortal.getModifiedDate());
        return registrationDTO;


    }
}
