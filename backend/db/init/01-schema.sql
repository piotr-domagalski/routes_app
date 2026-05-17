CREATE TABLE routes (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(127) NULL,
    description VARCHAR(511) NULL,
    distanceMeters INT NULL,
    type ENUM("LOOP", "ONEWAY") NULL,
    activity ENUM("RUN", "BIKE", "BOTH") NULL
);

CREATE TABLE users (
    username VARCHAR(32) NOT NULL PRIMARY KEY,
    display_name VARCHAR(64) NULL,
    password_hash VARCHAR(256) NOT NULL
);

CREATE TABLE sessions (
    token BINARY(64) NOT NULL PRIMARY KEY,
    user VARCHAR(32) NOT NULL,
    expires DATETIME NOT NULL,
    CONSTRAINT `fk_sessions_users`
        FOREIGN KEY (user) REFERENCES users(username)
        ON DELETE CASCADE
);
