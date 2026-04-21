package com.utp.EduTrack.repository;

import com.utp.EduTrack.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findBySectionId(Long sectionId);
    List<Enrollment> findByStudentId(Long studentId);
}
