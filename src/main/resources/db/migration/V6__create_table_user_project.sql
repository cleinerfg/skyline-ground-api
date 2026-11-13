CREATE TABLE user_project
(
    user_id     BIGINT       NOT NULL,
    project_id  BIGINT       NOT NULL,
    role        project_role NOT NULL DEFAULT 'VIEWER',
    added_by_id BIGINT       NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_project PRIMARY KEY (user_id, project_id),

    CONSTRAINT fk_user_project_user FOREIGN KEY (user_id)
        REFERENCES app_user (id) ON DELETE RESTRICT,

    CONSTRAINT fk_user_project_project FOREIGN KEY (project_id)
        REFERENCES project (id) ON DELETE RESTRICT,

    CONSTRAINT fk_user_project_added_by FOREIGN KEY (added_by_id)
        REFERENCES app_user (id) ON DELETE RESTRICT
);

-- Index required to optimize reverse lookup queries (e.g., "get all users for a specific project")
CREATE INDEX idx_user_project_project_id ON user_project (project_id);

CREATE UNIQUE INDEX uk_user_project_single_owner
    ON user_project (project_id) WHERE role = 'OWNER';
