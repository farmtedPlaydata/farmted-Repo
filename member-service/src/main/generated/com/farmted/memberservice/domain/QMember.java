package com.farmted.memberservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 467541267L;

    public static final QMember member = new QMember("member1");

    public final QTimeStamp _super = new QTimeStamp(this);

    public final BooleanPath checkIn = createBoolean("checkIn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath memberAddress = createString("memberAddress");

    public final StringPath memberAddressDetail = createString("memberAddressDetail");

    public final NumberPath<Long> memberBalance = createNumber("memberBalance", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memberName = createString("memberName");

    public final StringPath memberPhone = createString("memberPhone");

    public final StringPath memberProfile = createString("memberProfile");

    public final EnumPath<com.farmted.memberservice.enums.RoleEnums> memberRole = createEnum("memberRole", com.farmted.memberservice.enums.RoleEnums.class);

    public final BooleanPath memberStatus = createBoolean("memberStatus");

    public final StringPath memberUuid = createString("memberUuid");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

