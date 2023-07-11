CREATE DATABASE board;

USE board;

CREATE TABLE poster (
                        id bigint not null auto_increment,
                        title varchar(100),
                        writer varchar(100),
                        content varchar(300),
                        regdate DATETIME,
                        PRIMARY KEY (id)
);


CREATE TABLE comment (
    id bigint NOT NULL auto_increment,
    pno bigint NOT NULL,
    writer VARCHAR(100) NOT NULL,
    content VARCHAR(300) NOT NULL,
    regdate DATETIME NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(pno) REFERENCES poster(id)
);