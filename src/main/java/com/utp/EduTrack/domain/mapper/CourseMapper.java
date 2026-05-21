package com.utp.EduTrack.domain.mapper;

import com.utp.EduTrack.domain.dto.CourseDTO;
import com.utp.EduTrack.domain.dto.CourseRequestDTO;
import com.utp.EduTrack.persistance.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDto(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .description(course.getDescription())
                .credits(course.getCredits())
                .build();
    }

    public Course toEntity(CourseRequestDTO request) {
        return Course.builder()
                .name(request.getName())
                .code(request.getCode().trim().toUpperCase())
                .description(request.getDescription())
                .credits(request.getCredits())
                .build();
    }

    public void updateEntity(Course course, CourseRequestDTO request) {
        course.setName(request.getName());
        course.setCode(request.getCode().trim().toUpperCase());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
    }
}
