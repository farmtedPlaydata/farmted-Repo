package com.farmted.passservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPass is a Querydsl query type for Pass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPass extends EntityPathBase<Pass> {

    private static final long serialVersionUID = 1732571004L;

    public static final QPass pass = new QPass("pass");

    public final QTimeStamp _super = new QTimeStamp(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath password = createString("password");

    public final EnumPath<com.farmted.passservice.enums.RoleEnums> role = createEnum("role", com.farmted.passservice.enums.RoleEnums.class);

    public final StringPath socialId = createString("socialId");

    public final EnumPath<com.farmted.passservice.enums.SocialType> socialType = createEnum("socialType", com.farmted.passservice.enums.SocialType.class);

    public final BooleanPath status = createBoolean("status");

    public final StringPath uuid = createString("uuid");

    public QPass(String variable) {
        super(Pass.class, forVariable(variable));
    }

    public QPass(Path<? extends Pass> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPass(PathMetadata metadata) {
        super(Pass.class, metadata);
    }

}

