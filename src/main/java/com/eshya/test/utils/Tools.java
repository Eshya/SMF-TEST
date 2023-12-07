package com.eshya.test.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;

public class Tools {
    public static String base64Encoding(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String base64Decoding(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }
    public static String encrypt(String text) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = "";

        for (int i = 0; i < 12; i++) {
            // "123456" - plain text - user input from user interface
            pwd = encoder.encode(text);

        }

        return pwd;
    }
}
