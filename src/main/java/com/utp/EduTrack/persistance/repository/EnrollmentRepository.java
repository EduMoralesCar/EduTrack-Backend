package com.utp.EduTrack.persistance.repository;

import com.utp.EduTrack.persistance.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findBySectionId(Long sectionId);
    List<Enrollment> findByStudentId(Long studentId);
    boolean existsByStudentIdAndSectionId(Long studentId, Long sectionId);
    java.util.Optional<Enrollment> findByStudentIdAndSectionId(Long studentId, Long sectionId);
}
