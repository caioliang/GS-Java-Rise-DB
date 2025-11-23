--------------------------------------------------------
-- Tabela TB_INSIGHTS
--------------------------------------------------------
CREATE TABLE TB_INSIGHTS (
                             id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                             resume_id RAW(16) NULL,
                             payload CLOB CHECK (payload IS JSON),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             CONSTRAINT fk_insights_resume FOREIGN KEY (resume_id)
                                 REFERENCES TB_RESUME(id)
                                 ON DELETE CASCADE

);

COMMIT ;