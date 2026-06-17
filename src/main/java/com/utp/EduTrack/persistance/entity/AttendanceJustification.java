package com.utp.EduTrack.persistance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_justifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceJustification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    // Optional: link to uploaded proof (e.g., medical certificate)
    @Column(name = "proof_file_path")
    private String proofFilePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JustificationStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
}
