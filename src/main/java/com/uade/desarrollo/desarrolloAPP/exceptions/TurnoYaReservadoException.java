package com.uade.desarrollo.desarrolloAPP.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El turno ya se encuentra reservado por otro usuario")
public class TurnoYaReservadoException extends RuntimeException {
    public TurnoYaReservadoException(String message) {
        super(message);
    }
}
