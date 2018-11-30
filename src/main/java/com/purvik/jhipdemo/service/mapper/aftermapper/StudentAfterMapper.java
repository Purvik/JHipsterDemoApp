package com.purvik.jhipdemo.service.mapper.aftermapper;

import com.purvik.jhipdemo.domain.Student;
import com.purvik.jhipdemo.service.dto.StudentDTO;
import com.purvik.jhipdemo.service.util.SupportUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public class StudentAfterMapper {

    @AfterMapping
    public void mapLocalDateToString(Student student, @MappingTarget StudentDTO studentDTO) {
        if (student.getBirthDate() != null) {
            studentDTO.setBirthDate(SupportUtils.convertLocalDateToString(student.getBirthDate()));
        }
    }

    @AfterMapping
    public void mapStringToLocalDate(@MappingTarget Student student, StudentDTO studentDTO) {
        if (!StringUtils.isBlank(studentDTO.getBirthDate())) {
            student.setBirthDate(SupportUtils.convertStringDateToLocalDate(studentDTO.getBirthDate()));
        }
    }
}
