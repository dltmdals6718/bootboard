package com.example.board.repository;

import com.example.board.domain.Member;
import com.example.board.domain.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberTypeAndSnsIdentifier(MemberType memberType, Long snsIdentifier);
}
