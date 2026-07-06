package com.utp.EduTrack.domain.mapper;

import com.utp.EduTrack.domain.dto.NotificationDTO;
import com.utp.EduTrack.persistance.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDTO toDto(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .studentId(notification.getStudent().getId())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .read(notification.isRead())
                .build();
    }
}
