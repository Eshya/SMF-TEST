package com.eshya.test.payload.auth;

import com.eshya.test.model.UserPortal;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthRes {
    private String username;

    private String token;

    private Date expiresAt;

    private Date issuedAt;
}
