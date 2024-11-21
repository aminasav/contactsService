package com.contactsservice.config;

import com.contactsservice.api.exception.ServiceUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new BadRequestException();
            case 404 -> new NotFoundException();
            case 500 -> new InternalServerErrorException();
            case 503 -> new ServiceUnavailableException("Service unavailable");
            default -> new Exception();
        };
    }
}