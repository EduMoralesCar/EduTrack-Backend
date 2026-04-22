package com.utp.EduTrack.domain.repository;

import com.utp.EduTrack.persistance.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByAssignmentId(Long assignmentId);
    List<Grade> findByStudentId(Long studentId);
}
