package com.farmted.memberservice.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.farmted.memberservice.dto.response.QMemberResponseDto is a Querydsl Projection type for MemberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberResponseDto extends ConstructorExpression<MemberResponseDto> {

    private static final long serialVersionUID = 111292855L;

    public QMemberResponseDto(com.querydsl.core.types.Expression<String> memberUuid, com.querydsl.core.types.Expression<String> memberName, com.querydsl.core.types.Expression<com.farmted.memberservice.enums.RoleEnums> memberRole) {
        super(MemberResponseDto.class, new Class<?>[]{String.class, String.class, com.farmted.memberservice.enums.RoleEnums.class}, memberUuid, memberName, memberRole);
    }

}

