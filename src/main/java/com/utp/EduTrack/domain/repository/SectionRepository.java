package com.utp.EduTrack.domain.repository;

import com.utp.EduTrack.persistance.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseId(Long courseId);
    List<Section> findByTeacherId(Long teacherId);
}
