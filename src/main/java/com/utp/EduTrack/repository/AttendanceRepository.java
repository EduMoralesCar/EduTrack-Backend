package com.utp.EduTrack.repository;

import com.utp.EduTrack.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySectionId(Long sectionId);
    List<Attendance> findByStudentId(Long studentId);
}
