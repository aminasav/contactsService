package com.contactsservice.usecasses.client;

import com.contactsservice.config.ContactConfig;
import com.contactsservice.usecasses.dto.PhoneCodeResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "PHONE-CODE-SERVICE", configuration = ContactConfig.class)
public interface PhoneCodeServiceFeignClient {
    Logger logger = LoggerFactory.getLogger(PhoneCodeServiceFeignClient.class);
    //@CircuitBreaker(name = "phone-codes-service", fallbackMethod = "getPhoneCodeInfoFallback")
    //@Retry(name = "phone-codes-service")
    @GetMapping("/api/v1/phone-codes/{phoneCodeId}")
    PhoneCodeResponseDto getPhoneCodeInfo(@PathVariable String phoneCodeId);

    default PhoneCodeResponseDto getPhoneCodeInfoFallback(String phoneCodeId, Throwable t) {
        logger.warn("PHONE-CODE-SERVICE is not available, using fallback: {}", t.getMessage());
        return new PhoneCodeResponseDto(null, null, null);
    }
}
