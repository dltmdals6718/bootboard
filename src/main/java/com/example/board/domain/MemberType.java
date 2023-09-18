package com.example.board.domain;

import lombok.Getter;

@Getter
public enum MemberType {

    NORMAL("일반 회원"),
    KAKAO("카카오 회원");
    private final String description;

    MemberType(String description) {
        this.description = description;
    }

}
