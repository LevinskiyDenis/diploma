package com.example.filesharing.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FileMapperUtil {

    @Autowired
    ModelMapper modelMapper;

    public <T, D> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        Page<D> map = entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
        return map;
    }

    public <T, D> D mapEntityIntoDto(T t, Class<D> dtoClass) {
        return modelMapper.map(t, dtoClass);
    }

}
