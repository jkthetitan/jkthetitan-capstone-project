CREATE TABLE IF NOT EXISTS roles (
    role_id SMALLINT GENERATED ALWAYS AS IDENTITY,
    role_name VARCHAR(50) NOT NULL,

    CONSTRAINT pk_role PRIMARY KEY (role_id),

    CONSTRAINT uq_role_name UNIQUE (role_name)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,

    CONSTRAINT pk_user PRIMARY KEY (user_id),

    CONSTRAINT uq_email UNIQUE (email),

    CONSTRAINT uq_username UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_role_id BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    role_id SMALLINT NOT NULL,

    CONSTRAINT pk_user_role PRIMARY KEY (user_role_id),

    CONSTRAINT uq_user_role UNIQUE (user_id, role_id),

    CONSTRAINT fk_user_in_user_roles
        FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,

    CONSTRAINT fk_role_in_user_roles
        FOREIGN KEY (role_id)
        REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id SMALLINT GENERATED ALWAYS AS IDENTITY,
    genre_name VARCHAR(50) NOT NULL,

    CONSTRAINT pk_genre PRIMARY KEY (genre_id),

    CONSTRAINT uq_genre_name UNIQUE (genre_name)
);

CREATE TABLE IF NOT EXISTS authors (
    author_id BIGINT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,

    CONSTRAINT pk_author PRIMARY KEY (author_id)
);

CREATE TABLE IF NOT EXISTS books (
    book_id BIGINT GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) NOT NULL,
    pages INT NULL,
    description TEXT NULL,
    isbn VARCHAR(13) NOT NULL,
    release_year SMALLINT NULL,
    lang VARCHAR(20) NULL,

    CONSTRAINT pk_book PRIMARY KEY (book_id),

    CONSTRAINT uq_isbn UNIQUE (isbn)
);

CREATE TABLE IF NOT EXISTS author_books (
    author_book_id BIGINT GENERATED ALWAYS AS IDENTITY,
    author_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,

    CONSTRAINT pk_author_book PRIMARY KEY (author_book_id),

    CONSTRAINT fk_author_in_author_books
        FOREIGN KEY (author_id)
        REFERENCES authors(author_id),

    CONSTRAINT fk_book_in_author_books
        FOREIGN KEY (book_id)
        REFERENCES books(book_id),

    CONSTRAINT uq_author_book UNIQUE (author_id, book_id)
);

CREATE TABLE IF NOT EXISTS book_genres (
    book_genre_id BIGINT GENERATED ALWAYS AS IDENTITY,
    book_id BIGINT NOT NULL,
    genre_id SMALLINT NOT NULL,

    CONSTRAINT pk_book_genre PRIMARY KEY (book_genre_id),

    CONSTRAINT fk_book_in_book_genres
        FOREIGN KEY (book_id)
        REFERENCES books(book_id),

    CONSTRAINT fk_genre_in_book_genres
        FOREIGN KEY (genre_id)
        REFERENCES genres(genre_id),

    CONSTRAINT uq_book_genre UNIQUE (book_id, genre_id)
);

CREATE TABLE IF NOT EXISTS book_copies (
    copy_id BIGINT GENERATED ALWAYS AS IDENTITY,
    book_id BIGINT NOT NULL,
    barcode VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE',

    CONSTRAINT pk_copy PRIMARY KEY (copy_id),

    CONSTRAINT fk_book_in_copies
        FOREIGN KEY (book_id)
        REFERENCES books(book_id),

    CONSTRAINT uq_barcode UNIQUE (barcode),

    CONSTRAINT ck_book_status
        CHECK (status IN (
            'AVAILABLE', 'ISSUED', 'RESERVED'
        ))
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED ALWAYS AS IDENTITY,
    reader_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'SUBMITTED',
    request_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    request_type VARCHAR(20),

    approved_by BIGINT NULL,
    decision_datetime TIMESTAMP NULL,

    copy_id BIGINT NULL,
    issued_by BIGINT NULL,
    issue_datetime TIMESTAMP NULL,
    due_datetime TIMESTAMP NULL,
    return_datetime TIMESTAMP NULL,

    CONSTRAINT pk_request PRIMARY KEY (request_id),

    CONSTRAINT fk_reader_in_requests
        FOREIGN KEY (reader_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_approved_by_in_requests
        FOREIGN KEY (approved_by)
        REFERENCES users(user_id),

    CONSTRAINT fk_issued_by_in_requests
        FOREIGN KEY (issued_by)
        REFERENCES users(user_id),

    CONSTRAINT fk_book_in_requests
        FOREIGN KEY (book_id)
        REFERENCES books(book_id),

    CONSTRAINT fk_copy_in_requests
        FOREIGN KEY (copy_id)
        REFERENCES book_copies(copy_id),

    CONSTRAINT ck_request_status
        CHECK (status in (
            'SUBMITTED', 'REJECTED', 'APPROVED', 'ISSUED', 'OVERDUE', 'RETURNED'
        )),

    CONSTRAINT ck_request_type
        CHECK (request_type in (
           'IN-LIBRARY', 'HOME'
        ))
);
