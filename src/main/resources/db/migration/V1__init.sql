CREATE TABLE TB_USER (
                         id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                         name VARCHAR2(100) NOT NULL,
                         cpf VARCHAR2(11) NOT NULL UNIQUE,
                         birth_date DATE NOT NULL
);

--------------------------------------------------------
-- Tabela TB_AUTH
--------------------------------------------------------
CREATE TABLE TB_AUTH (
                         user_id RAW(16) PRIMARY KEY,
                         email VARCHAR2(150) NOT NULL UNIQUE,
                         password VARCHAR2(255) NOT NULL,
                         CONSTRAINT fk_auth_user FOREIGN KEY (user_id)
                             REFERENCES TB_USER(id)
                             ON DELETE CASCADE
);

--------------------------------------------------------
-- Tabela TB_RESUME
--------------------------------------------------------
CREATE TABLE TB_RESUME (
                           id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                           objective CLOB NOT NULL,
                           user_id RAW(16) NOT NULL UNIQUE,
                           CONSTRAINT fk_resume_user FOREIGN KEY (user_id)
                               REFERENCES TB_USER(id)
                               ON DELETE CASCADE
);

--------------------------------------------------------
-- Tabela TB_WORK_EXPERIENCE
--------------------------------------------------------
CREATE TABLE TB_WORK_EXPERIENCE (
                                    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                                    company VARCHAR2(100) NOT NULL,
                                    role VARCHAR2(100) NOT NULL,
                                    start_date DATE NOT NULL,
                                    end_date DATE,
                                    description CLOB NOT NULL,
                                    resume_id RAW(16) NOT NULL,
                                    CONSTRAINT fk_work_resume FOREIGN KEY (resume_id)
                                        REFERENCES TB_RESUME(id)
                                        ON DELETE CASCADE
);

--------------------------------------------------------
-- Tabela TB_EDUCATIONAL_EXPERIENCE
--------------------------------------------------------
CREATE TABLE TB_EDUCATIONAL_EXPERIENCE (
                                           id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                                           institution VARCHAR2(100) NOT NULL,
                                           course VARCHAR2(100) NOT NULL,
                                           start_date DATE NOT NULL,
                                           end_date DATE,
                                           description CLOB NOT NULL,
                                           resume_id RAW(16) NOT NULL,
                                           CONSTRAINT fk_educational_resume FOREIGN KEY (resume_id)
                                               REFERENCES TB_RESUME(id)
                                               ON DELETE CASCADE
);

COMMIT;