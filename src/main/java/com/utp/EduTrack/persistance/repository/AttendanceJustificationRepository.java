package com.utp.EduTrack.persistance.repository;

import com.utp.EduTrack.persistance.entity.AttendanceJustification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceJustificationRepository extends JpaRepository<AttendanceJustification, Long> {
    List<AttendanceJustification> findByAttendanceId(Long attendanceId);
    List<AttendanceJustification> findByAttendanceIdIn(List<Long> attendanceIds);
    List<AttendanceJustification> findByAttendanceSectionId(Long sectionId);
}
