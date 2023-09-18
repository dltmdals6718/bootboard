package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {

    // 누군가 플랫폼 탈퇴 후. 다른 누군가가 똑같은 플랫폼으로 똑같은 이메일로 가입하면 기존 회원으로 인식하므로
    // SNS 로그인시 기존,신규 구별은 : snsIdentifier와 memberType으로 찾자.
    // snsIdentifier로만 하면 다른 플래폼의 식별자와 겹칠 수도 있음.

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private Long snsIdentifier; // 로그인 플랫폼에서 제공하는 PK

    @Enumerated(EnumType.STRING)
    private MemberType memberType; // 일반, 카카오
}
