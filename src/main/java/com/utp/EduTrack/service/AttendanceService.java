package com.utp.EduTrack.service;

import com.utp.EduTrack.entity.Attendance;
import com.utp.EduTrack.entity.AttendanceStatus;
import com.utp.EduTrack.repository.AttendanceRepository;
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
