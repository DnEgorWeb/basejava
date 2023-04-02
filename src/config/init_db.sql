CREATE TABLE IF NOT EXISTS resume (
  uuid      CHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                 NOT NULL
);

CREATE TABLE IF NOT EXISTS contact (
  id          SERIAL   PRIMARY KEY,
  resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type        TEXT     NOT NULL,
  value       TEXT     NOT NULL
);

CREATE TABLE IF NOT EXISTS section (
  id          SERIAL   PRIMARY KEY,
  resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type        TEXT     NOT NULL,
  value       TEXT     NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS contact_uuid_type_index
  ON contact (resume_uuid, type);

CREATE UNIQUE INDEX IF NOT EXISTS section_uuid_type_index
  ON section (resume_uuid, type);