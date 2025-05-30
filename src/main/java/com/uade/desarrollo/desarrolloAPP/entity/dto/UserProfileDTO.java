package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String home_address;
    private String phone_number;
}
