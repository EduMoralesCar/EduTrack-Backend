package com.utp.EduTrack.persistance.repository;

import com.utp.EduTrack.persistance.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    long countByStudentIdAndReadFalse(Long studentId);
}
