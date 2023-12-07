package com.eshya.test.controller;

import com.eshya.test.utils.AESUtil;
import com.eshya.test.utils.Constant;
import com.eshya.test.utils.TimeTraker;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/"+ Constant.API_NAME +"/utility")
public class UtilityController {

    @TimeTraker
    @PostMapping("/create-encryption")
    public Map<String, String> createEncryption(@RequestBody Map<String, Object> requestBody) {
//        if(branchNow.equals("development")) {
        String dataValue = (String) requestBody.get("data");
        String encryption = AESUtil.encrypt(dataValue, Constant.UI_SECRET_KEY);
        String decryption = AESUtil.decrypt(encryption, Constant.UI_SECRET_KEY);
        Map<String, String> response = Map.of("encryption", encryption,
                "decryption", decryption);
        return response;
//        }
//        return Map.of("message", "Not Implemented");
    }
}
