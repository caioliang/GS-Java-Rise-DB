-- Initial schema for Rise application

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS TB_USER (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    birth_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS TB_RESUME (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    objective TEXT NOT NULL,
    user_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_resume_user FOREIGN KEY(user_id) REFERENCES TB_USER(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TB_WORK_EXPERIENCE (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT NOT NULL,
    resume_id UUID NOT NULL,
    CONSTRAINT fk_work_resume FOREIGN KEY(resume_id) REFERENCES TB_RESUME(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TB_EDUCATIONAL_EXPERIENCE (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    institution VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT NOT NULL,
    resume_id UUID NOT NULL,
    CONSTRAINT fk_educational_resume FOREIGN KEY(resume_id) REFERENCES TB_RESUME(id) ON DELETE CASCADE
);

