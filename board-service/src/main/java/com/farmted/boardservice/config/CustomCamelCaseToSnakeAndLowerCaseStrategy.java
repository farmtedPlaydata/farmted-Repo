package com.farmted.boardservice.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;


import java.util.Locale;

public class CustomCamelCaseToSnakeAndLowerCaseStrategy extends CamelCaseToUnderscoresNamingStrategy {
    @Override
    protected Identifier getIdentifier(String name, final boolean quoted, final JdbcEnvironment jdbcEnvironment) {
        if ( isCaseInsensitive( jdbcEnvironment ) ) {
            name = name.toLowerCase( Locale.ROOT );
        }
        return new Identifier( name, quoted );
    }
}