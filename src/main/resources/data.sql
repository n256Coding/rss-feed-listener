DROP TABLE IF EXISTS feed;

CREATE TABLE feed
(
    id                 INTEGER PRIMARY KEY AUTO_INCREMENT,
    identifier         VARCHAR(200) NOT NULL,
    integrity_checksum VARCHAR(100) NOT NULL,
    title              VARCHAR(200) NOT NULL,
    description        TEXT         NOT NULL,
    published_date     TIMESTAMP    NOT NULL,
    updated_date       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_identifier (identifier),
    UNIQUE KEY uk_checksum (integrity_checksum)
);
