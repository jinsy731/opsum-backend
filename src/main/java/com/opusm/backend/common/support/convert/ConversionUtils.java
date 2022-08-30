package com.opusm.backend.common.support.convert;

import org.springframework.ui.ModelMap;

public class ConversionUtils {

    public static <T> T entityToDto(Class<T> dtoType, Object entity) {
        return ModelMappers.modelMapper.map(entity, dtoType);
    }
}
