package com.utp.EduTrack.dto;

import com.utp.EduTrack.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean isActive;
}
