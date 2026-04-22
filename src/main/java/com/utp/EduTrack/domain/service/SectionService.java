package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.domain.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    public List<Section> getSectionsByCourse(Long courseId) {
        return sectionRepository.findByCourseId(courseId);
    }
}
