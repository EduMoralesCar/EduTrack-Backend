package com.utp.EduTrack.config;

import com.utp.EduTrack.persistance.entity.*;
import com.utp.EduTrack.persistance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1) // Runs first
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMaterialRepository courseMaterialRepository;
    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceJustificationRepository attendanceJustificationRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("La base de datos ya contiene datos (Usuarios: {}). Omitiendo limpieza y semillado para conservar registros.", userRepository.count());
            return;
        }
        log.info("Iniciando limpieza y semillado de datos de demostración en la base de datos por solicitud...");
        performResetAndSeed();
        log.info("¡Semillado completado con éxito!");
    }
    private void performResetAndSeed() {
        // 1. Delete in cascade order
        jdbcTemplate.execute("ALTER TABLE attendances DROP CONSTRAINT IF EXISTS attendances_status_check");
        jdbcTemplate.execute("ALTER TABLE enrollments DROP CONSTRAINT IF EXISTS enrollments_status_check");
        jdbcTemplate.execute("ALTER TABLE attendance_justifications DROP CONSTRAINT IF EXISTS attendance_justifications_status_check");

        attendanceJustificationRepository.deleteAllInBatch();
        attendanceRepository.deleteAllInBatch();
        gradeRepository.deleteAllInBatch();
        assignmentSubmissionRepository.deleteAllInBatch();
        assignmentRepository.deleteAllInBatch();
        courseMaterialRepository.deleteAllInBatch();
        enrollmentRepository.deleteAllInBatch();
        sectionRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        userRepository.flush();

        // 2. Create Users
        User admin = User.builder()
                .username("JUAN PABLO")
                .password(passwordEncoder.encode("Admin123!"))
                .email("juanpablo@edutrack.com")
                .role(Role.ADMIN)
                .active(true)
                .build();

        User teacher = User.builder()
                .username("FERMIN LOPEZ")
                .password(passwordEncoder.encode("Docente123!"))
                .email("ferminlopez@edutrack.com")
                .role(Role.TEACHER)
                .active(true)
                .build();

        User studentEdu = User.builder()
                .username("EDU MORALES")
                .password(passwordEncoder.encode("Estudiante123!"))
                .email("edumorales@edutrack.com")
                .role(Role.STUDENT)
                .active(true)
                .build();

        User studentMaria = User.builder()
                .username("MARIA GOMEZ")
                .password(passwordEncoder.encode("Estudiante123!"))
                .email("mariagomez@edutrack.com")
                .role(Role.STUDENT)
                .active(true)
                .build();

        User studentJuan = User.builder()
                .username("JUAN PEREZ")
                .password(passwordEncoder.encode("Estudiante123!"))
                .email("juanperez@edutrack.com")
                .role(Role.STUDENT)
                .active(true)
                .build();

        User studentLuis = User.builder()
                .username("LUIS RAMIREZ")
                .password(passwordEncoder.encode("Estudiante123!"))
                .email("luisramirez@edutrack.com")
                .role(Role.STUDENT)
                .active(true)
                .build();

        userRepository.save(admin);
        userRepository.save(teacher);
        userRepository.save(studentEdu);
        userRepository.save(studentMaria);
        userRepository.save(studentJuan);
        userRepository.save(studentLuis);

        // 3. Create Course
        Course course = Course.builder()
                .code("WEB2026")
                .name("Desarrollo Web Full Stack")
                .description("Curso de especialización en React y Spring Boot")
                .credits(4)
                .build();
        courseRepository.save(course);

        // 4. Create Section (2026-I, SEC-A)
        Section section = Section.builder()
                .course(course)
                .teacher(teacher)
                .period("2026-I")
                .code("SEC-A")
                .build();
        sectionRepository.save(section);

        // 5. Create Enrollments
        Enrollment enrollEdu = Enrollment.builder().student(studentEdu).section(section).status(EnrollmentStatus.ENROLLED).build();
        Enrollment enrollMaria = Enrollment.builder().student(studentMaria).section(section).status(EnrollmentStatus.ENROLLED).build();
        Enrollment enrollJuan = Enrollment.builder().student(studentJuan).section(section).status(EnrollmentStatus.ENROLLED).build();
        Enrollment enrollLuis = Enrollment.builder().student(studentLuis).section(section).status(EnrollmentStatus.ENROLLED).build();

        enrollmentRepository.save(enrollEdu);
        enrollmentRepository.save(enrollMaria);
        enrollmentRepository.save(enrollJuan);
        enrollmentRepository.save(enrollLuis);

        // 6. Create Course Materials (Inician vacíos por solicitud de pruebas del usuario)

        // 7. Create Assignments
        List<Assignment> assignments = new ArrayList<>();
        // Tarea 1: Componentes (PA, Semana 2)
        Assignment t1 = Assignment.builder()
                .name("Tarea 1: Componentes")
                .type(AssignmentType.TAREA)
                .category("PA")
                .weekNumber(2)
                .startDate(LocalDateTime.of(2026, 3, 22, 8, 0))
                .endDate(LocalDateTime.of(2026, 3, 29, 23, 59))
                .section(section)
                .build();
        // PC1 (Semana 5)
        Assignment pc1 = Assignment.builder()
                .name("Práctica Calificada 1")
                .type(AssignmentType.PRACTICA)
                .category("PC1")
                .weekNumber(5)
                .startDate(LocalDateTime.of(2026, 4, 12, 8, 0))
                .endDate(LocalDateTime.of(2026, 4, 12, 20, 0))
                .section(section)
                .build();
        // Tarea 2: Hooks (PA, Semana 6)
        Assignment t2 = Assignment.builder()
                .name("Tarea 2: Hooks y Efectos")
                .type(AssignmentType.TAREA)
                .category("PA")
                .weekNumber(6)
                .startDate(LocalDateTime.of(2026, 4, 19, 8, 0))
                .endDate(LocalDateTime.of(2026, 4, 26, 23, 59))
                .section(section)
                .build();
        // PC2 (Semana 10)
        Assignment pc2 = Assignment.builder()
                .name("Práctica Calificada 2")
                .type(AssignmentType.PRACTICA)
                .category("PC2")
                .weekNumber(10)
                .startDate(LocalDateTime.of(2026, 5, 17, 8, 0))
                .endDate(LocalDateTime.of(2026, 5, 17, 20, 0))
                .section(section)
                .build();
        // Tarea 3: Redux (PA, Semana 12)
        Assignment t3 = Assignment.builder()
                .name("Tarea 3: Context y Redux")
                .type(AssignmentType.TAREA)
                .category("PA")
                .weekNumber(12)
                .startDate(LocalDateTime.of(2026, 5, 31, 8, 0))
                .endDate(LocalDateTime.of(2026, 6, 7, 23, 59))
                .section(section)
                .build();
        // PC3 (Semana 15)
        Assignment pc3 = Assignment.builder()
                .name("Práctica Calificada 3")
                .type(AssignmentType.PRACTICA)
                .category("PC3")
                .weekNumber(15)
                .startDate(LocalDateTime.of(2026, 6, 21, 8, 0))
                .endDate(LocalDateTime.of(2026, 6, 21, 20, 0))
                .section(section)
                .build();
        // Examen Final (Semana 18)
        Assignment exFinal = Assignment.builder()
                .name("Examen Final")
                .type(AssignmentType.EXAMEN)
                .category("EXFINAL")
                .weekNumber(18)
                .startDate(LocalDateTime.of(2026, 7, 12, 8, 0))
                .endDate(LocalDateTime.of(2026, 7, 12, 20, 0))
                .section(section)
                .build();

        assignments.add(t1);
        assignments.add(pc1);
        assignments.add(t2);
        assignments.add(pc2);
        assignments.add(t3);
        assignments.add(pc3);
        assignments.add(exFinal);
        assignmentRepository.saveAll(assignments);

        // 8. Create Grades
        List<Grade> grades = new ArrayList<>();
        // EDU MORALES
        grades.add(Grade.builder().assignment(t1).student(studentEdu).score(18.0).teacherComment("Buen trabajo en la estructuración de componentes").build());
        grades.add(Grade.builder().assignment(pc1).student(studentEdu).score(16.0).teacherComment("Buen examen, repasar la parte teórica").build());
        grades.add(Grade.builder().assignment(t2).student(studentEdu).score(17.5).teacherComment("Uso correcto de useEffect y dependencias").build());
        grades.add(Grade.builder().assignment(pc2).student(studentEdu).score(15.0).teacherComment("Bien resuelto").build());
        grades.add(Grade.builder().assignment(t3).student(studentEdu).score(19.0).teacherComment("Excelente implementación de Redux Toolkit").build());
        grades.add(Grade.builder().assignment(pc3).student(studentEdu).score(14.0).teacherComment("Faltó pulir el manejo de errores en el API").build());
        grades.add(Grade.builder().assignment(exFinal).student(studentEdu).score(15.0).teacherComment("Examen final aprobado con buen desempeño general").build());

        // MARIA GOMEZ
        grades.add(Grade.builder().assignment(t1).student(studentMaria).score(17.0).teacherComment("Excelente").build());
        grades.add(Grade.builder().assignment(pc1).student(studentMaria).score(15.0).teacherComment("Aprobado").build());
        grades.add(Grade.builder().assignment(t2).student(studentMaria).score(16.0).teacherComment("Buen uso de Hooks").build());
        grades.add(Grade.builder().assignment(pc2).student(studentMaria).score(16.0).teacherComment("Completo").build());
        grades.add(Grade.builder().assignment(t3).student(studentMaria).score(18.0).teacherComment("Muy estructurado").build());
        grades.add(Grade.builder().assignment(pc3).student(studentMaria).score(15.0).teacherComment("Aprobado").build());
        grades.add(Grade.builder().assignment(exFinal).student(studentMaria).score(16.0).teacherComment("Buen examen").build());

        // JUAN PEREZ
        grades.add(Grade.builder().assignment(t1).student(studentJuan).score(14.0).teacherComment("Regular").build());
        grades.add(Grade.builder().assignment(pc1).student(studentJuan).score(11.0).teacherComment("Por poco desaprueba").build());
        grades.add(Grade.builder().assignment(t2).student(studentJuan).score(15.0).teacherComment("Bien").build());
        grades.add(Grade.builder().assignment(pc2).student(studentJuan).score(13.0).teacherComment("Regular").build());
        grades.add(Grade.builder().assignment(t3).student(studentJuan).score(14.0).teacherComment("Entregado a tiempo").build());
        grades.add(Grade.builder().assignment(pc3).student(studentJuan).score(12.0).teacherComment("Faltó profundidad").build());
        grades.add(Grade.builder().assignment(exFinal).student(studentJuan).score(12.0).teacherComment("Aprobado").build());

        // LUIS RAMIREZ
        grades.add(Grade.builder().assignment(t1).student(studentLuis).score(15.0).teacherComment("Buen intento").build());
        grades.add(Grade.builder().assignment(pc1).student(studentLuis).score(14.0).teacherComment("Aprobado").build());
        grades.add(Grade.builder().assignment(t2).student(studentLuis).score(13.0).teacherComment("Mejorar código").build());
        grades.add(Grade.builder().assignment(pc2).student(studentLuis).score(12.0).teacherComment("Bajo rendimiento").build());
        grades.add(Grade.builder().assignment(t3).student(studentLuis).score(15.0).teacherComment("Buen trabajo en equipo").build());
        grades.add(Grade.builder().assignment(pc3).student(studentLuis).score(13.0).teacherComment("Regular").build());
        grades.add(Grade.builder().assignment(exFinal).student(studentLuis).score(14.0).teacherComment("Buen examen final").build());

        gradeRepository.saveAll(grades);

        // 9. Create Attendance records
        List<Attendance> attendances = new ArrayList<>();
        LocalDate[] classDates = {
                LocalDate.of(2026, 3, 16),
                LocalDate.of(2026, 3, 23),
                LocalDate.of(2026, 3, 30),
                LocalDate.of(2026, 4, 6),
                LocalDate.of(2026, 4, 13),
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                LocalDate.of(2026, 5, 11),
                LocalDate.of(2026, 5, 18)
        };

        for (LocalDate date : classDates) {
            // EDU MORALES (All present, one late on date 4)
            AttendanceStatus eduStatus = date.equals(classDates[3]) ? AttendanceStatus.TARDE : AttendanceStatus.PRESENTE;
            attendances.add(Attendance.builder().section(section).student(studentEdu).date(date).status(eduStatus).build());

            // MARIA GOMEZ (Some late)
            AttendanceStatus mariaStatus = (date.equals(classDates[2]) || date.equals(classDates[7])) ? AttendanceStatus.TARDE : AttendanceStatus.PRESENTE;
            attendances.add(Attendance.builder().section(section).student(studentMaria).date(date).status(mariaStatus).build());

            // JUAN PEREZ (Some absent and late)
            AttendanceStatus juanStatus = AttendanceStatus.PRESENTE;
            if (date.equals(classDates[1]) || date.equals(classDates[5])) {
                juanStatus = AttendanceStatus.AUSENTE;
            } else if (date.equals(classDates[8])) {
                juanStatus = AttendanceStatus.TARDE;
            }
            attendances.add(Attendance.builder().section(section).student(studentJuan).date(date).status(juanStatus).build());

            // LUIS RAMIREZ (One absent)
            AttendanceStatus luisStatus = date.equals(classDates[4]) ? AttendanceStatus.AUSENTE : AttendanceStatus.PRESENTE;
            attendances.add(Attendance.builder().section(section).student(studentLuis).date(date).status(luisStatus).build());
        }

        attendanceRepository.saveAll(attendances);
    }
}
