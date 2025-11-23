CREATE TABLE IF NOT EXISTS TB_INSIGHTS (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resume_id UUID NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_insights_resume FOREIGN KEY(resume_id) REFERENCES TB_RESUME(id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_insights_resume_id ON TB_INSIGHTS(resume_id);
