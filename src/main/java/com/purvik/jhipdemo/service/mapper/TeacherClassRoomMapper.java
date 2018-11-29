package com.purvik.jhipdemo.service.mapper;

import com.purvik.jhipdemo.domain.Teacher;
import com.purvik.jhipdemo.service.dto.TeacherClassroomDTO;
import com.purvik.jhipdemo.service.dto.TeacherDTO;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.jpa.repository.support.EntityManagerBeanDefinitionRegistrarPostProcessor;

import java.lang.annotation.Target;
import java.util.List;

public interface TeacherClassRoomMapper extends EntityMapper<TeacherClassroomDTO, Teacher> {


    Teacher toEntity(TeacherClassroomDTO teacherDTO);

    @Mapping(target = "classrooms", ignore = true)
    TeacherClassroomDTO toDto(Teacher teacher);

    default Teacher fromId(Long id) {
        if (id == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setId(id);
        return teacher;
    }

}
