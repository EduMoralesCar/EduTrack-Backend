package com.utp.EduTrack.persistance.repository;

import com.utp.EduTrack.persistance.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseId(Long courseId);
    List<Section> findByTeacherId(Long teacherId);
    Optional<Section> findByCourseIdAndPeriodAndCode(Long courseId, String period, String code);
}
