package com.utp.EduTrack.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Direct assignment grade, but linked through submission if needed.
    // For this, we can link directly to AssignmentSubmission, or to Assignment+Student.
    // Given the submission might be optional (e.g. an exam taken in class), linking to Assignment and Student is safer.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Validated between 1 and 20 in the service layer
    @Column(nullable = false)
    private Double score;

    @Column(columnDefinition = "TEXT")
    private String teacherComment;
}
