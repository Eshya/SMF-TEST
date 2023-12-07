package com.eshya.test.dto;

import com.eshya.test.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegistrationDTO {
    private Long userPortalId;

    private String username;

    private String password;

    private Role role;

    private Date registrationDate;

    private Date modifiedDate;
}
