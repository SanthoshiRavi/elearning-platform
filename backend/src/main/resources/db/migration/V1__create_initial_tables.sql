CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    created_by BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_courses_created_by
                     FOREIGN KEY (created_by)
                     REFERENCES users(id)
);

CREATE TABLE course_contents (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    content_title VARCHAR(150) NOT NULL,
    content_type VARCHAR(20) NOT NULL,
    content_url TEXT NOT NULL,
    sequence_order INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_course_contents_course
                             FOREIGN KEY (course_id)
                             REFERENCES courses(id)
                             ON DELETE CASCADE
);
 CREATE TABLE enrollments (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT NOT NULL,
     course_id BIGINT NOT NULL,
     enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

     CONSTRAINT fk_enrollments_user
                          FOREIGN KEY (user_id)
                          REFERENCES users(id)
                          ON DELETE CASCADE,
     CONSTRAINT fk_enrollments_course
                        FOREIGN KEY (course_id)
                        REFERENCES courses(id)
                        ON DELETE CASCADE,
     CONSTRAINT uk_user_course_enrollment
         UNIQUE(user_id,course_id)

 );