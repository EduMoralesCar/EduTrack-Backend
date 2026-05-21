package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.CourseDTO;
import com.utp.EduTrack.domain.dto.CourseRequestDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.domain.mapper.CourseMapper;
import com.utp.EduTrack.persistance.entity.Course;
import com.utp.EduTrack.persistance.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    public List<CourseDTO> findAll() {
        return courseRepository.findAll().stream().map(courseMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CourseDTO findById(Long id) {
        return courseMapper.toDto(getCourseEntity(id));
    }

    public CourseDTO create(CourseRequestDTO request) {
        validateUniqueCode(request.getCode(), null);
        Course saved = courseRepository.save(courseMapper.toEntity(request));
        return courseMapper.toDto(saved);
    }

    public CourseDTO update(Long id, CourseRequestDTO request) {
        Course course = getCourseEntity(id);
        validateUniqueCode(request.getCode(), id);
        courseMapper.updateEntity(course, request);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso no encontrado con id: " + id);
        }
        courseRepository.deleteById(id);
    }

    public Course getCourseEntity(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
    }

    private void validateUniqueCode(String code, Long excludeId) {
        String normalized = code.trim().toUpperCase();
        boolean exists = excludeId == null
                ? courseRepository.existsByCode(normalized)
                : courseRepository.existsByCodeAndIdNot(normalized, excludeId);

        if (exists) {
            throw new BusinessException("El código de curso ya existe: " + normalized);
        }
    }
}
