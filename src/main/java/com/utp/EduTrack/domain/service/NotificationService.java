package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.NotificationDTO;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.domain.mapper.NotificationMapper;
import com.utp.EduTrack.persistance.entity.Notification;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public void sendNotification(User student, String message) {
        Notification notification = Notification.builder()
                .student(student)
                .message(message)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsForStudent(Long studentId) {
        return notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long studentId) {
        return notificationRepository.countByStudentIdAndReadFalse(studentId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long studentId) {
        List<Notification> unread = notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .filter(n -> !n.isRead())
                .toList();
        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }
}
