package com.farmted.memberservice.repository;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberResponseDto;
import com.farmted.memberservice.dto.response.QMemberResponseDto;
import com.farmted.memberservice.enums.RoleEnums;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.function.LongSupplier;

import static com.farmted.memberservice.domain.QMember.member;
import static org.springframework.util.StringUtils.hasText;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        super(Member.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberResponseDto> findAll(SearchMemberParam param, Pageable pageable) {
        List<MemberResponseDto> content = queryFactory
                .select(getMember())
                .from(member)
                .where(
                        nameEq(param.getMemberName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        nameEq(param.getMemberName())
                );


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameEq(String memberName) {
        return hasText(memberName) ? member.memberName.eq(memberName) : null;
    }

    private QMemberResponseDto getMember() {
        return new QMemberResponseDto(
                member.memberUuid,
                member.memberName,
                member.memberRole
        );
    }
}
