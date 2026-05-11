DROP TABLE IF EXISTS routes;

CREATE TABLE routes (
    id INT PRIMARY KEY,
    name VARCHAR(128) NULL,
    description VARCHAR(128) NULL,
    lengthM INT NULL
);
