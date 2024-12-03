package com.contactsservice.config;

import com.contactsservice.api.exception.BadRequestException;
import com.contactsservice.api.exception.GeneralServiceException;
import com.contactsservice.api.exception.InternalServerErrorException;
import com.contactsservice.api.exception.NotFoundException;
import com.contactsservice.api.exception.ServiceUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());

        Exception exception = switch (response.status()) {
            case 400 -> new BadRequestException("Invalid request");
            case 404 -> new NotFoundException("Resource not found");
            case 500 -> new InternalServerErrorException("Internal server error");
            case 503 -> new ServiceUnavailableException("Service temporarily unavailable");
            default -> new GeneralServiceException("An error occurred");
        };

        log.error("Error in {} with status code {} and message: {}",
                methodKey, httpStatus, exception.getMessage());

        return exception;
    }
}