package com.github.pathfinder.core.web.exception.mapper;

import com.github.pathfinder.core.exception.ErrorCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

@Mapper
public interface ExceptionMapper {

    ExceptionMapper INSTANCE = Mappers.getMapper(ExceptionMapper.class);

    HttpStatus status(ErrorCode code);

}
