package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.AssignTeacherDTO;
import com.utp.EduTrack.domain.dto.SectionCreateDTO;
import com.utp.EduTrack.domain.dto.SectionDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.domain.mapper.SectionMapper;
import com.utp.EduTrack.persistance.entity.Course;
import com.utp.EduTrack.persistance.entity.Role;
import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final SectionMapper sectionMapper;

    @Transactional(readOnly = true)
    public List<SectionDTO> findAll() {
        return sectionRepository.findAll().stream().map(sectionMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<SectionDTO> findByCourse(Long courseId) {
        courseService.getCourseEntity(courseId);
        return sectionRepository.findByCourseId(courseId).stream()
                .map(sectionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SectionDTO findById(Long id) {
        return sectionMapper.toDto(getSectionEntity(id));
    }

    public SectionDTO create(SectionCreateDTO request) {
        Course course = courseService.getCourseEntity(request.getCourseId());
        String period = request.getPeriod().trim();
        String code = request.getCode().trim().toUpperCase();

        if (sectionRepository.findByCourseIdAndPeriodAndCode(course.getId(), period, code).isPresent()) {
            throw new BusinessException("Ya existe una sección con ese curso, periodo y código");
        }

        Section section = Section.builder()
                .course(course)
                .period(period)
                .code(code)
                .build();

        return sectionMapper.toDto(sectionRepository.save(section));
    }

    public SectionDTO update(Long id, SectionCreateDTO request) {
        Section section = getSectionEntity(id);
        Course course = courseService.getCourseEntity(request.getCourseId());
        String period = request.getPeriod().trim();
        String code = request.getCode().trim().toUpperCase();

        sectionRepository.findByCourseIdAndPeriodAndCode(course.getId(), period, code)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Ya existe una sección con ese curso, periodo y código");
                });

        section.setCourse(course);
        section.setPeriod(period);
        section.setCode(code);

        return sectionMapper.toDto(sectionRepository.save(section));
    }

    public void delete(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sección no encontrada con id: " + id);
        }
        sectionRepository.deleteById(id);
    }

    public SectionDTO assignTeacher(Long sectionId, AssignTeacherDTO request) {
        Section section = getSectionEntity(sectionId);
        User teacher = userService.getUserEntity(request.getTeacherId());

        if (teacher.getRole() != Role.TEACHER) {
            throw new BusinessException("El usuario asignado debe tener rol TEACHER");
        }
        if (!teacher.isActive()) {
            throw new BusinessException("El docente debe estar activo");
        }

        section.setTeacher(teacher);
        return sectionMapper.toDto(sectionRepository.save(section));
    }

    public Section getSectionEntity(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con id: " + id));
    }
}
