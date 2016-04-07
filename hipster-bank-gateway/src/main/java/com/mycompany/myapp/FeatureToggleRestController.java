package com.mycompany.myapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RefreshScope
@RestController
class FeatureToggleRestController {

    @Value("${feature-toggle.account}")
    private String accountServiceVersion;

    @RequestMapping(value = "/feature-toggle")
    @ResponseBody
    HashMap<String,String> getFeatures() {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("account", accountServiceVersion);
        return hm;
    }
}

