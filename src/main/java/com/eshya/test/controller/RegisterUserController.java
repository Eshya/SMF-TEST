package com.eshya.test.controller;

import com.eshya.test.dto.RegistrationDTO;
import com.eshya.test.model.UserPortal;
import com.eshya.test.payload.RegistrationReq;
import com.eshya.test.service.RegistrationService;
import com.eshya.test.service.UserPortalService;
import com.eshya.test.utils.AESUtil;
import com.eshya.test.utils.Constant;
import com.eshya.test.utils.JwtUtils;
import com.eshya.test.utils.TimeTraker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/"+ Constant.API_NAME )
public class RegisterUserController {
    private final Logger logger = LoggerFactory.getLogger(RegisterUserController.class);
    private final RegistrationService registrationService;
    private final UserPortalService userPortalService;
    private final JwtUtils jwtUtils;

    @Autowired
    public RegisterUserController(RegistrationService registrationService, UserPortalService userPortalService, JwtUtils jwtUtils) {
        this.registrationService = registrationService;
        this.userPortalService = userPortalService;
        this.jwtUtils = jwtUtils;
    }

    @TimeTraker
    @PostMapping("/register")
    public ResponseEntity<RegistrationDTO> register(@RequestBody RegistrationReq request){
        logger.info("registering user : {}", request.getUsername());


        UserPortal userPortal = new UserPortal();
        userPortal.setUsername(request.getUsername());
        userPortal.setPassword(AESUtil.decrypt(request.getPassword(), Constant.UI_SECRET_KEY));
        userPortal.setStatus(Constant.STATUS_ACTIVE);
        userPortal.setCreationDate(new Date());
        userPortal.setModifiedDate(new Date());

        RegistrationDTO registrationDTO = registrationService.register(userPortal);

        registrationDTO.setPassword(userPortal.getPassword());
        registrationDTO.setUsername(userPortal.getUsername());

        return new ResponseEntity<>(registrationDTO, HttpStatus.OK);
    }

}
