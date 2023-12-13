package com.farmted.memberservice.repository;


import com.farmted.memberservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByMemberUuid(String uuid);
    Member getMemberByMemberUuid(String uuid);
    void deleteMemberByMemberUuid(String uuid);
}
