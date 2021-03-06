package com.purvik.jhipdemo.service.mapper;

import com.purvik.jhipdemo.domain.*;
import com.purvik.jhipdemo.service.dto.StudentDTO;

import com.purvik.jhipdemo.service.mapper.aftermapper.StudentAfterMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Student and its DTO StudentDTO.
 */
@Mapper(componentModel = "spring", uses = {ClassroomMapper.class, StudentAfterMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {

    @Mapping(source = "classroom.id", target = "classroomId")
    @Mapping(target = "birthDate",ignore = true)
    StudentDTO toDto(Student student);

    @Mapping(source = "classroomId", target = "classroom")
    @Mapping(target = "birthDate", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
