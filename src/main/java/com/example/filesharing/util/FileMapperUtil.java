package com.example.filesharing.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FileMapperUtil {

    @Autowired
    ModelMapper modelMapper;

    /**
     * Maps the Page {@code entities} of <code>T</code> type which have to be mapped as input to {@code dtoClass} Page
     * of mapped object with <code>D</code> type.
     *
     * @param <D>      - type of DTO objects
     * @param <T>      - type of objects returned in page by repository
     * @param entities - page of entities that needs to be mapped
     * @param dtoClass - class of DTO
     * @return page - mapped page with objects of type <code>D</code>.
     * @NB <code>dtoClass</code> must has NoArgsConstructor!
     */
    public <T, D> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        Page<D> map = entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
        return map;
    }

    public <T, D> D mapEntityIntoDto(T t, Class<D> dtoClass) {
        return modelMapper.map(t, dtoClass);
    }

}
