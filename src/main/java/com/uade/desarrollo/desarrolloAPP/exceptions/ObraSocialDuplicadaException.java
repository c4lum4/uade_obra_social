package com.uade.desarrollo.desarrolloAPP.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El usuario ya tiene una obra social asociada")
public class ObraSocialDuplicadaException extends RuntimeException {
    public ObraSocialDuplicadaException(String message) {
        super(message);
    }
}
