package com.utp.EduTrack.dto;

import com.utp.EduTrack.entity.Role;
import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;
    private String email;
    private Role role;
}
