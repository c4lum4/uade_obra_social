package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.dto.AuthenticationRequest;
import com.uade.desarrollo.desarrolloAPP.entity.dto.AuthenticationResponse;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;

public interface AuthenticationService {
    AuthenticationResponse register(UserRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
