package com.eshya.test.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthReq {
    private String username;

    private String password;
}
