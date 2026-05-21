-- RF04: la sección se crea sin docente; RF05 asigna el docente después.
ALTER TABLE sections ALTER COLUMN teacher_id DROP NOT NULL;
