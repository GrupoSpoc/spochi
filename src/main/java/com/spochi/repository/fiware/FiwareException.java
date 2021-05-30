package com.spochi.repository.fiware;

import com.spochi.controller.HttpStatus;
import com.spochi.controller.exception.BadRequestException;
import lombok.Getter;

public class FiwareException extends BadRequestException {
    @Getter private Integer code;

    public FiwareException(String message) {
        super(message, HttpStatus.FIWARE_ERROR);
    }

    public FiwareException(String message, Integer code) {
        this(message);
        this.code = code;
    }
}
