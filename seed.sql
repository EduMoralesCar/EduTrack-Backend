-- Reset database tables
TRUNCATE TABLE attendance_justifications CASCADE;
TRUNCATE TABLE attendances CASCADE;
TRUNCATE TABLE grades CASCADE;
TRUNCATE TABLE assignment_submissions CASCADE;
TRUNCATE TABLE assignments CASCADE;
TRUNCATE TABLE course_materials CASCADE;
TRUNCATE TABLE enrollments CASCADE;
TRUNCATE TABLE sections CASCADE;
TRUNCATE TABLE courses CASCADE;
TRUNCATE TABLE users CASCADE;

-- Insert users (Passwords are plaintext and will be encrypted by the app on startup)
INSERT INTO users (id, username, password, email, role, is_active)
VALUES 
(1, 'JUAN PABLO', 'Admin123!', 'juanpablo@edutrack.com', 'ADMIN', true),
(2, 'FERMIN LOPEZ', 'Docente123!', 'ferminlopez@edutrack.com', 'TEACHER', true),
(3, 'EDU MORALES', 'Estudiante123!', 'edumorales@edutrack.com', 'STUDENT', true),
(4, 'MARIA GOMEZ', 'Estudiante123!', 'mariagomez@edutrack.com', 'STUDENT', true),
(5, 'JUAN PEREZ', 'Estudiante123!', 'juanperez@edutrack.com', 'STUDENT', true),
(6, 'LUIS RAMIREZ', 'Estudiante123!', 'luisramirez@edutrack.com', 'STUDENT', true)
ON CONFLICT (id) DO NOTHING;

-- Insert course
INSERT INTO courses (id, code, name, description, credits) 
VALUES (1, 'WEB2026', 'Desarrollo Web Full Stack', 'Curso de especialización en React y Spring Boot', 4)
ON CONFLICT (id) DO NOTHING;

-- Insert Section for 2026-I
INSERT INTO sections (id, code, period, course_id, teacher_id)
VALUES (1, 'SEC-A', '2026-I', 1, 2)
ON CONFLICT (id) DO NOTHING;

-- Enroll students in section
INSERT INTO enrollments (id, student_id, section_id, status)
VALUES 
(1, 3, 1, 'ENROLLED'),
(2, 4, 1, 'ENROLLED'),
(3, 5, 1, 'ENROLLED'),
(4, 6, 1, 'ENROLLED')
ON CONFLICT (id) DO NOTHING;

-- Insert Course Materials
INSERT INTO course_materials (id, title, file_path, week_number, upload_date, section_id)
VALUES 
(1, 'Syllabus del Curso', 'uploads/syllabus.pdf', 1, '2026-03-15 08:00:00', 1),
(2, 'Introducción a React', 'uploads/react_intro.pdf', 2, '2026-03-22 08:00:00', 1),
(3, 'Componentes y Props', 'uploads/components_props.pdf', 3, '2026-03-29 08:00:00', 1),
(4, 'Estado y Ciclo de Vida', 'uploads/state_lifecycle.pdf', 4, '2026-04-05 08:00:00', 1)
ON CONFLICT (id) DO NOTHING;

-- Insert Assignments (PA in week 2, PC1 in week 5, PA in week 6, PC2 in week 10, PA in week 12, PC3 in week 15, EXFINAL in week 18)
INSERT INTO assignments (id, name, type, category, week_number, start_date, end_date, section_id)
VALUES 
(1, 'Tarea 1: Componentes', 'TAREA', 'PA', 2, '2026-03-22 08:00:00', '2026-03-29 23:59:00', 1),
(2, 'Práctica Calificada 1', 'PRACTICA', 'PC1', 5, '2026-04-12 08:00:00', '2026-04-12 20:00:00', 1),
(3, 'Tarea 2: Hooks y Efectos', 'TAREA', 'PA', 6, '2026-04-19 08:00:00', '2026-04-26 23:59:00', 1),
(4, 'Práctica Calificada 2', 'PRACTICA', 'PC2', 10, '2026-05-17 08:00:00', '2026-05-17 20:00:00', 1),
(5, 'Tarea 3: Context y Redux', 'TAREA', 'PA', 12, '2026-05-31 08:00:00', '2026-06-07 23:59:00', 1),
(6, 'Práctica Calificada 3', 'PRACTICA', 'PC3', 15, '2026-06-21 08:00:00', '2026-06-21 20:00:00', 1),
(7, 'Examen Final', 'EXAMEN', 'EXFINAL', 18, '2026-07-12 08:00:00', '2026-07-12 20:00:00', 1)
ON CONFLICT (id) DO NOTHING;

-- Insert Grades
-- Student EDU MORALES (id:3)
INSERT INTO grades (id, score, teacher_comment, assignment_id, student_id)
VALUES 
(1, 18.0, 'Buen trabajo en la estructuración de componentes', 1, 3),
(2, 16.0, 'Buen examen, repasar la parte teórica', 2, 3),
(3, 17.5, 'Uso correcto de useEffect y dependencias', 3, 3),
(4, 15.0, 'Bien resuelto', 4, 3),
(5, 19.0, 'Excelente implementación de Redux Toolkit', 5, 3),
(6, 14.0, 'Faltó pulir el manejo de errores en el API', 6, 3),
(7, 15.0, 'Examen final aprobado con buen desempeño general', 7, 3)
ON CONFLICT (id) DO NOTHING;

-- Student MARIA GOMEZ (id:4)
INSERT INTO grades (id, score, teacher_comment, assignment_id, student_id)
VALUES 
(8, 17.0, 'Excelente', 1, 4),
(9, 15.0, 'Aprobado', 2, 4),
(10, 16.0, 'Buen uso de Hooks', 3, 4),
(11, 16.0, 'Completo', 4, 4),
(12, 18.0, 'Muy estructurado', 5, 4),
(13, 15.0, 'Aprobado', 6, 4),
(14, 16.0, 'Buen examen', 7, 4)
ON CONFLICT (id) DO NOTHING;

-- Student JUAN PEREZ (id:5)
INSERT INTO grades (id, score, teacher_comment, assignment_id, student_id)
VALUES 
(15, 14.0, 'Regular', 1, 5),
(16, 11.0, 'Por poco desaprueba', 2, 5),
(17, 15.0, 'Bien', 3, 5),
(18, 13.0, 'Regular', 4, 5),
(19, 14.0, 'Entregado a tiempo', 5, 5),
(20, 12.0, 'Faltó profundidad', 6, 5),
(21, 12.0, 'Aprobado', 7, 5)
ON CONFLICT (id) DO NOTHING;

-- Student LUIS RAMIREZ (id:6)
INSERT INTO grades (id, score, teacher_comment, assignment_id, student_id)
VALUES 
(22, 15.0, 'Buen intento', 1, 6),
(23, 14.0, 'Aprobado', 2, 6),
(24, 13.0, 'Mejorar código', 3, 6),
(25, 12.0, 'Bajo rendimiento', 4, 6),
(26, 15.0, 'Buen trabajo en equipo', 5, 6),
(27, 13.0, 'Regular', 6, 6),
(28, 14.0, 'Buen examen final', 7, 6)
ON CONFLICT (id) DO NOTHING;

-- Insert Attendances
-- 10 weeks of class (June 2026 is week 10)
-- Class dates: '2026-03-16', '2026-03-23', '2026-03-30', '2026-04-06', '2026-04-13', '2026-04-20', '2026-04-27', '2026-05-04', '2026-05-11', '2026-05-18'
INSERT INTO attendances (id, date, status, section_id, student_id)
VALUES
(1, '2026-03-16', 'PRESENTE', 1, 3), (2, '2026-03-16', 'PRESENTE', 1, 4), (3, '2026-03-16', 'PRESENTE', 1, 5), (4, '2026-03-16', 'PRESENTE', 1, 6),
(5, '2026-03-23', 'PRESENTE', 1, 3), (6, '2026-03-23', 'PRESENTE', 1, 4), (7, '2026-03-23', 'AUSENTE', 1, 5),  (8, '2026-03-23', 'PRESENTE', 1, 6),
(9, '2026-03-30', 'PRESENTE', 1, 3), (10, '2026-03-30', 'TARDE', 1, 4),    (11, '2026-03-30', 'PRESENTE', 1, 5), (12, '2026-03-30', 'PRESENTE', 1, 6),
(13, '2026-04-06', 'TARDE', 1, 3),    (14, '2026-04-06', 'PRESENTE', 1, 4), (15, '2026-04-06', 'PRESENTE', 1, 5), (16, '2026-04-06', 'PRESENTE', 1, 6),
(17, '2026-04-13', 'PRESENTE', 1, 3), (18, '2026-04-13', 'PRESENTE', 1, 4), (19, '2026-04-13', 'PRESENTE', 1, 5), (20, '2026-04-13', 'AUSENTE', 1, 6),
(21, '2026-04-20', 'PRESENTE', 1, 3), (22, '2026-04-20', 'PRESENTE', 1, 4), (23, '2026-04-20', 'AUSENTE', 1, 5),  (24, '2026-04-20', 'PRESENTE', 1, 6),
(25, '2026-04-27', 'PRESENTE', 1, 3), (26, '2026-04-27', 'PRESENTE', 1, 4), (27, '2026-04-27', 'PRESENTE', 1, 5), (28, '2026-04-27', 'PRESENTE', 1, 6),
(29, '2026-05-04', 'PRESENTE', 1, 3), (30, '2026-05-04', 'TARDE', 1, 4),    (31, '2026-05-04', 'PRESENTE', 1, 5), (32, '2026-05-04', 'PRESENTE', 1, 6),
(33, '2026-05-11', 'PRESENTE', 1, 3), (34, '2026-05-11', 'PRESENTE', 1, 4), (35, '2026-05-11', 'TARDE', 1, 5),    (36, '2026-05-11', 'PRESENTE', 1, 6),
(37, '2026-05-18', 'PRESENTE', 1, 3), (38, '2026-05-18', 'PRESENTE', 1, 4), (39, '2026-05-18', 'PRESENTE', 1, 5), (40, '2026-05-18', 'PRESENTE', 1, 6)
ON CONFLICT (id) DO NOTHING;

-- Reset sequences (PostgreSQL syntax for auto-increment)
SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('sections_id_seq', (SELECT MAX(id) FROM sections));
SELECT setval('enrollments_id_seq', (SELECT MAX(id) FROM enrollments));
SELECT setval('course_materials_id_seq', (SELECT MAX(id) FROM course_materials));
SELECT setval('assignments_id_seq', (SELECT MAX(id) FROM assignments));
SELECT setval('grades_id_seq', (SELECT MAX(id) FROM grades));
SELECT setval('attendances_id_seq', (SELECT MAX(id) FROM attendances));
