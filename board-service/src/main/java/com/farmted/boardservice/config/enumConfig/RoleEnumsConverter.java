package com.farmted.boardservice.config.enumConfig;

import com.farmted.boardservice.enums.RoleEnums;
import org.springframework.core.convert.converter.Converter;

public class RoleEnumsConverter implements Converter<String, RoleEnums> {
    @Override
    public RoleEnums convert(String source) {
        return RoleEnums.roleCheck(source);
    }
}

