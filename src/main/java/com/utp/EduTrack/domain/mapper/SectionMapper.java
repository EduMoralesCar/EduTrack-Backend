package com.utp.EduTrack.domain.mapper;

import com.utp.EduTrack.domain.dto.SectionDTO;
import com.utp.EduTrack.persistance.entity.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {

    public SectionDTO toDto(Section section) {
        SectionDTO.SectionDTOBuilder builder = SectionDTO.builder()
                .id(section.getId())
                .courseId(section.getCourse().getId())
                .courseName(section.getCourse().getName())
                .courseCode(section.getCourse().getCode())
                .period(section.getPeriod())
                .code(section.getCode());

        if (section.getTeacher() != null) {
            builder.teacherId(section.getTeacher().getId())
                    .teacherUsername(section.getTeacher().getUsername());
        }

        return builder.build();
    }
}
