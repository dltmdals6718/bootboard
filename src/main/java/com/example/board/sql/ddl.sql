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
작성일도 추가해보자.
