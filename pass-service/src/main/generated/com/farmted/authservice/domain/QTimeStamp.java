package com.farmted.passservice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTimeStamp is a Querydsl query type for TimeStamp
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QTimeStamp extends EntityPathBase<TimeStamp> {

    private static final long serialVersionUID = 1823152235L;

    public static final QTimeStamp timeStamp = new QTimeStamp("timeStamp");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public QTimeStamp(String variable) {
        super(TimeStamp.class, forVariable(variable));
    }

    public QTimeStamp(Path<? extends TimeStamp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTimeStamp(PathMetadata metadata) {
        super(TimeStamp.class, metadata);
    }

}

