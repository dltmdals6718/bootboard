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

ALTER TABLE poster ADD COLUMN comment_cnt int not null default 0;

CREATE TABLE comment (
    id bigint NOT NULL auto_increment,
    pno bigint NOT NULL,
    writer VARCHAR(100) NOT NULL,
    content VARCHAR(300) NOT NULL,
    regdate DATETIME NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(pno) REFERENCES poster(id)
);

-- 대댓글 기능 추가
ALTER TABLE comment ADD COLUMN parent_comment_id int;
ALTER TABLE comment ADD COLUMN is_parent int not null;

UPDATE comment AS A
INNER JOIN comment AS B ON A.id = B.id
SET A.parent_comment_id = A.id;

UPDATE comment SET is_parent = 1;

create table upload_file (
                             id bigint auto_increment,
                             pno bigint,
                             upload_file_name VARCHAR(255),
                             store_file_name VARCHAR(255),
                             PRIMARY KEY (id),
                             FOREIGN KEY (pno) REFERENCES poster(id)
);

alter table poster add fix boolean; -- BOOLEAN = tinyint(1) --


CREATE TABLE MEMBER (
    id bigint AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    sns_identifier VARCHAR(255),
    member_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Poster 테이블의 외래 키 writer를 Member.id와 매핑
ALTER TABLE poster DROP writer;
ALTER TABLE poster ADD writer bigint;
ALTER TABLE poster ADD FOREIGN KEY (writer) REFERENCES member(id);

-- Comment 테이블의 pno -> poster_id로 컬럼명 변경.
ALTER TABLE comment RENAME COLUMN pno TO poster_id;