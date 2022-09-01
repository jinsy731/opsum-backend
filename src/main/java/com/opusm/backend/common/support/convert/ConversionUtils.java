package com.opusm.backend.common.support.convert;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.ui.ModelMap;

import java.util.HashSet;
import java.util.Set;

public class ConversionUtils {

    public static <T> T entityToDto(Class<T> dtoType, Object entity) {
        return ModelMappers.modelMapper.map(entity, dtoType);
    }

    public static <T> void entityUpdate(Object src, Object target) {
        copyPropertiesIgnoreNull(src, target);
    }


    /**
     * Bean properties copy ignore null.
     *
     * @param src    source
     * @param target target
     */
    private static void copyPropertiesIgnoreNull(Object src, Object target) {

        org.springframework.beans.BeanUtils.copyProperties(src, target, getNamesOfNullProperties(src));
    }

    /**
     * Get names of null properties
     *
     * @param source source object
     */
    private static String[] getNamesOfNullProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
