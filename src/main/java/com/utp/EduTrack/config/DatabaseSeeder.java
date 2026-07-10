package com.utp.EduTrack.config;

import com.utp.EduTrack.persistance.entity.*;
import com.utp.EduTrack.persistance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no users exist
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data population.");
            return;
        }

        log.info("Starting database seeding process...");

        // 1. Seed Users
        User admin = User.builder()
                .username("JUAN PABLO")
                .email("admin@edutrack.edu")
                .password(passwordEncoder.encode("Admin123!"))
                .role(Role.ADMIN)
                .active(true)
                .build();

        User teacher = User.builder()
                .username("FERMIN LOPEZ")
                .email("fermin@edutrack.edu")
                .password(passwordEncoder.encode("Docente123!"))
                .role(Role.TEACHER)
                .active(true)
                .build();

        User student = User.builder()
                .username("EDU MORALES")
                .email("edu@edutrack.edu")
                .password(passwordEncoder.encode("Estudiante123!"))
                .role(Role.STUDENT)
                .active(true)
                .build();

        userRepository.saveAll(Arrays.asList(admin, teacher, student));
        log.info("Default users seeded successfully.");

        // 2. Seed Courses
        Course course1 = Course.builder()
                .code("DWI-2026")
                .name("Desarrollo Web Integrado")
                .credits(3)
                .description("Curso de especialización en React y Spring Boot")
                .build();

        Course course2 = Course.builder()
                .code("DFS-2026")
                .name("Desarrollo Web Full Stack")
                .credits(4)
                .description("Curso de especialización en Angular y Spring Boot")
                .build();

        Course course3 = Course.builder()
                .code("DS-2026")
                .name("Desarrollo de Software")
                .credits(4)
                .description("Curso de especialización en CMMI")
                .build();

        courseRepository.saveAll(Arrays.asList(course1, course2, course3));
        log.info("Default courses seeded successfully.");

        // 3. Seed Sections
        Section section1 = Section.builder()
                .course(course1)
                .teacher(teacher)
                .period("2026-I")
                .code("SEC-A")
                .build();

        Section section2 = Section.builder()
                .course(course2)
                .teacher(teacher)
                .period("2026-I")
                .code("SEC-B")
                .build();

        sectionRepository.saveAll(Arrays.asList(section1, section2));
        log.info("Default sections seeded successfully.");

        // 4. Seed Student Enrollments
        Enrollment enrollment1 = Enrollment.builder()
                .student(student)
                .section(section1)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        Enrollment enrollment2 = Enrollment.builder()
                .student(student)
                .section(section2)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        enrollmentRepository.saveAll(Arrays.asList(enrollment1, enrollment2));
        log.info("Default student enrollments seeded successfully.");

        log.info("Database seeding process completed successfully.");
    }
}
