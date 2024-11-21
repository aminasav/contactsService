package com.contactsservice.usecasses.client;

import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PHONE-CODE-SERVICE")
public interface PhoneCodeServiceFeignClient {

    @GetMapping("/api/v1/phone-codes/{phoneCodeId}")
    PhoneCodeResponseDto getPhoneCodeInfo(@PathVariable String phoneCodeId);
}
