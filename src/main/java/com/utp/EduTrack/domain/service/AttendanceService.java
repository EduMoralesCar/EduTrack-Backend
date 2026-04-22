package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.persistance.entity.Attendance;
import com.utp.EduTrack.persistance.entity.AttendanceStatus;
import com.utp.EduTrack.domain.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public Attendance recordAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceBySection(Long sectionId) {
        return attendanceRepository.findBySectionId(sectionId);
    }

    public Attendance excuseAbsence(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        
        attendance.setStatus(AttendanceStatus.EXCUSED);
        return attendanceRepository.save(attendance);
    }
    
    public List<Attendance> getAttendanceSummaryByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}
