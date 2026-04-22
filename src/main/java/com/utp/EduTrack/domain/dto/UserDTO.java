package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean isActive;
}
