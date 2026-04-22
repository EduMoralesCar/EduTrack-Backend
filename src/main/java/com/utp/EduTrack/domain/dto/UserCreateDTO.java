package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.Role;
import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;
    private String email;
    private Role role;
}
