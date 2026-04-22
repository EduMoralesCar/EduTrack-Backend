package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.persistance.entity.Enrollment;
import com.utp.EduTrack.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public Enrollment enrollStudent(Enrollment enrollment) {
        // Here we could add business logic to check if student is already enrolled
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsBySection(Long sectionId) {
        return enrollmentRepository.findBySectionId(sectionId);
    }
}
