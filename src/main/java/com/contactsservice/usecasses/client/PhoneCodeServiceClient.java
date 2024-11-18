package com.contactsservice.usecasses.client;

import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PhoneCodeServiceClient {

    @Autowired
    RestTemplate restTemplate;

    public PhoneCodeResponseDto getPhoneCodeInfo(String phoneCodeId){
        return restTemplate.getForObject("http://PHONE-CODE-SERVICE/api/v1/phone-codes/" + phoneCodeId, PhoneCodeResponseDto.class);
    }
}
