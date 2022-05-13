package com.hanghae.finalp.repository;

import com.hanghae.finalp.entity.MemberGroup;
import com.hanghae.finalp.entity.mappedsuperclass.Authority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {

    @Query("select gm from MemberGroup gm join fetch gm.group where gm.group.id = :memberId")
    Slice<MemberGroup> findMyGroupByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    Optional<MemberGroup> findByMemberIdAndGroupId(Long memberId, Long GroupId);

    Optional<MemberGroup> findByAuthorityAndMemberId(Authority authority, Long memberId);

}