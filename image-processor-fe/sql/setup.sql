CREATE DATABASE web CHARACTER SET utf8 COLLATE utf8_general_ci; 

USE web;

CREATE TABLE accounts (
    id int AUTO_INCREMENT NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(2056) NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE types (
    id int AUTO_INCREMENT NOT NULL,
    type_name varchar(255) NOT NULL,
    extension varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE files (
    id int AUTO_INCREMENT NOT NULL,
    created_by int NOT NULL,
    path VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    size int NOT NULL,
    uploaded_on DATETIME NOT NULL,
    last_changed DATETIME NOT NULL,
    type int NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (created_by) REFERENCES accounts(id),
    FOREIGN KEY (type) REFERENCES types(id),
    CONSTRAINT created_by UNIQUE (created_by, name)
);

CREATE TABLE shares (
    id int AUTO_INCREMENT NOT NULL,
    shared_by int NOT NULL,
    shared_to int NOT NULL,
    file_name varchar(255) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (shared_by) REFERENCES accounts(id),
    FOREIGN KEY (shared_to) REFERENCES accounts(id)
);


