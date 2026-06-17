package com.utp.EduTrack.persistance.repository;

import com.utp.EduTrack.persistance.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    List<CourseMaterial> findBySectionId(Long sectionId);
}
