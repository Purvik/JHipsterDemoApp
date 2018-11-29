package com.purvik.jhipdemo.service.mapper.aftermapper;

import com.purvik.jhipdemo.domain.Subject;
import com.purvik.jhipdemo.service.dto.SubjectDTO;
import com.purvik.jhipdemo.service.util.SupportUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = {})
public class SubjectAfterMapper  {

    @AfterMapping
    public void mapZonedDateToString(Subject subject, @MappingTarget SubjectDTO subjectDTO) {
        if (subject.getDate() != null) {
            subjectDTO.setDate(SupportUtils.convertLocalDateToString(subject.getDate()));
        }
    }

    @AfterMapping
    public void mapStringToZoneDateTime(@MappingTarget Subject subject, SubjectDTO subjectDTO) {
        if (!StringUtils.isBlank(subjectDTO.getDate())) {
            subject.setDate(SupportUtils.convertStringDateToLocalDate(subjectDTO.getDate()));
        }
    }
}
