package com.eshya.test.controller;

import com.eshya.test.exception.ResourceNotFoundException;
import com.eshya.test.model.UserPortal;
import com.eshya.test.payload.AuthReq;
import com.eshya.test.payload.AuthRes;
import com.eshya.test.payload.DefaultMessage;
import com.eshya.test.payload.ResetPasswdReq;
import com.eshya.test.service.UserDetailsImpl;
import com.eshya.test.service.UserPortalService;
import com.eshya.test.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/"+ Constant.API_NAME +"/auth")
public class AuthenticationController {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserPortalService userPortalSrv;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationController(UserPortalService userPortalSrv, AuthenticationManager authManager, JwtUtils jwtUtils) {
        this.userPortalSrv = userPortalSrv;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    @TimeTraker
    @PostMapping("/token")
    public ResponseEntity<AuthRes> getToken(@RequestBody AuthReq authReq){
       logger.info("get token for user :{} and password: {}", authReq.getUsername(), authReq.getPassword());

        UserPortal userPortal = userPortalSrv.findByUsername(authReq.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Username or password is wrong!", authReq.getUsername()), "Username or password",
                        authReq.getUsername()));
        logger.info("user portal is found: {} and status account: {}", userPortal.getUsername(), userPortal.getStatus());

        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(),
                AESUtil.decrypt(authReq.getPassword(), Constant.UI_SECRET_KEY)));

        if(userPortal.getStatus().equals("INACTIVE")){
            throw new ResourceNotFoundException("Your account is inactive", "status", userPortal.getStatus());
        }
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        // generate token
        String jwt = jwtUtils.generateJwtToken(userDetails);

        String username = jwtUtils.getUsernameFromToken(jwt);
        Date expireAt = jwtUtils.getExpirationDateFromToken(jwt);
        Date issuedAt = jwtUtils.getIssuedAtFromToken(jwt);

        // set response
        AuthRes authRes = new AuthRes();
        authRes.setToken(jwt);
        authRes.setExpiresAt(expireAt);
        authRes.setIssuedAt(issuedAt);
        authRes.setUsername(username);

        userPortal.setToken(jwt);
        userPortalSrv.save(userPortal);

        logger.info("token generated for user : {}", authReq.getUsername());
        return new ResponseEntity<>(authRes, HttpStatus.OK);
    }

    @TimeTraker
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswdReq resetPasswdReq, @RequestHeader("Authorization") String token){

        logger.info("reset password for user : {}", resetPasswdReq.getUsername());
        String changer = jwtUtils.getUsernameFromToken(token);
        UserPortal userPortal = userPortalSrv.findByUsername(resetPasswdReq.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Username or password is wrong!", resetPasswdReq.getUsername()), "Username or password",
                        resetPasswdReq.getUsername()));
        logger.info("user portal is found: {} and status account: {}", userPortal.getUsername(), userPortal.getStatus());

        if(userPortal.getStatus().equals("INACTIVE")){
            throw new ResourceNotFoundException("Your account is inactive", "status", userPortal.getStatus());
        }


        String newPassword = AESUtil.decrypt(resetPasswdReq.getNewPassword(), Constant.UI_SECRET_KEY);


        userPortal.setPassword(Tools.encrypt(newPassword));
        userPortal.setModifiedDate(new Date());
        userPortalSrv.save(userPortal);

        logger.info("password is reset for user : {}", resetPasswdReq.getUsername());
        // create response { "message": "Password is reset successfully" }

        return new ResponseEntity<>(new DefaultMessage("Password is reset successfully"), HttpStatus.OK);
    }
}
