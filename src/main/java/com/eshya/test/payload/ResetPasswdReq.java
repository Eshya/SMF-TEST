package com.eshya.test.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswdReq {
    private String username;

    @JsonProperty("new_password")
    private String newPassword;
}
