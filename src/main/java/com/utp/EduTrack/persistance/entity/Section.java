package com.utp.EduTrack.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "sections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "period", "code"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(nullable = false, length = 20)
    private String period;

    @Column(nullable = false, length = 10)
    private String code;
}
