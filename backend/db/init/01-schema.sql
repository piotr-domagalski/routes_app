CREATE TABLE routes (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(127) NULL,
    description VARCHAR(511) NULL,
    distanceMeters INT NULL,
    type ENUM("LOOP", "ONEWAY") NULL,
    activity ENUM("RUN", "BIKE", "BOTH") NULL
);
