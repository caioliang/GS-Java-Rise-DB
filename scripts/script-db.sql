-- EXTENSÃO PERMITIDA NO AZURE
--CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- TABELAS

CREATE TABLE IF NOT EXISTS TB_USER (
                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    birth_date DATE NOT NULL
    );

CREATE TABLE IF NOT EXISTS TB_AUTH (
                                       user_id UUID PRIMARY KEY,
                                       email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_auth_user FOREIGN KEY(user_id) REFERENCES TB_USER(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS TB_RESUME (
                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    objective VARCHAR(200),
    user_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_resume_user FOREIGN KEY(user_id) REFERENCES TB_USER(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS TB_WORK_EXPERIENCE (
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT NOT NULL,
    resume_id UUID NOT NULL,
    CONSTRAINT fk_work_resume FOREIGN KEY(resume_id) REFERENCES TB_RESUME(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS TB_EDUCATIONAL_EXPERIENCE (
                                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    institution VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT NOT NULL,
    resume_id UUID NOT NULL,
    CONSTRAINT fk_educational_resume FOREIGN KEY(resume_id) REFERENCES TB_RESUME(id) ON DELETE CASCADE
    );
CREATE TABLE IF NOT EXISTS TB_INSIGHTS (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_insights_resume FOREIGN KEY(resume_id) REFERENCES TB_RESUME(id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_insights_resume_id ON TB_INSIGHTS(resume_id);
