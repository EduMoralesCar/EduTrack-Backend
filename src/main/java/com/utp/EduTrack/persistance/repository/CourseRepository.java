package com.utp.EduTrack.persistance.repository;

import com.utp.EduTrack.persistance.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
