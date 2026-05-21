package com.utp.EduTrack.domain.mapper;

import com.utp.EduTrack.domain.dto.UserDTO;
import com.utp.EduTrack.persistance.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
