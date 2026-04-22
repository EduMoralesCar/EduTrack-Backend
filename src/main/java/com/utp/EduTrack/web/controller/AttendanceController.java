package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.persistance.entity.Attendance;
import com.utp.EduTrack.domain.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/sections/{sectionId}/attendance")
    public ResponseEntity<Attendance> recordAttendance(@PathVariable Long sectionId, @RequestBody Attendance attendance) {
        return ResponseEntity.ok(attendanceService.recordAttendance(attendance));
    }

    @PutMapping("/attendance/{attendanceId}/excuse")
    public ResponseEntity<Attendance> excuseAbsence(@PathVariable Long attendanceId) {
        return ResponseEntity.ok(attendanceService.excuseAbsence(attendanceId));
    }

    @GetMapping("/students/{studentId}/attendance-summary")
    public ResponseEntity<List<Attendance>> getAttendanceSummary(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceSummaryByStudent(studentId));
    }
}
