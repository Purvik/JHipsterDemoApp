package com.purvik.jhipdemo.service.mapper;

import com.purvik.jhipdemo.domain.*;
import com.purvik.jhipdemo.service.dto.SubjectDTO;

import com.purvik.jhipdemo.service.mapper.aftermapper.SubjectAfterMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity Subject and its DTO SubjectDTO.
 */
@Mapper(componentModel = "spring", uses = {ClassroomMapper.class, SubjectAfterMapper.class})
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {

    @Mapping(source = "classroom.id", target = "classroomId")
    @Mapping(target = "date", ignore = true)
    SubjectDTO toDto(Subject subject);

    @Mapping(source = "classroomId", target = "classroom")
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "date", ignore = true)
    Subject toEntity(SubjectDTO subjectDTO);

    default Subject fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subject subject = new Subject();
        subject.setId(id);
        return subject;
    }
}
