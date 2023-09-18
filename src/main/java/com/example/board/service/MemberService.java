package com.example.board.service;

import com.example.board.domain.Member;
import com.example.board.domain.MemberType;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        if(member.getMemberType()== MemberType.NORMAL) { // 시큐리티 배우고 해보자.

        } else if (member.getMemberType() == MemberType.KAKAO) {
            Optional<Member> findMember = memberRepository.findByMemberTypeAndSnsIdentifier(MemberType.KAKAO, member.getSnsIdentifier());
            if(findMember.isPresent()) { // 기존 카카오 회원
                return findMember.get();
            }
        }

        return memberRepository.save(member); // 새 회원
    }
}
