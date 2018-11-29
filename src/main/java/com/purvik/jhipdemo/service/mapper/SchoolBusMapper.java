package com.purvik.jhipdemo.service.mapper;

import com.purvik.jhipdemo.domain.*;
import com.purvik.jhipdemo.service.dto.SchoolBusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SchoolBus and its DTO SchoolBusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SchoolBusMapper extends EntityMapper<SchoolBusDTO, SchoolBus> {



    default SchoolBus fromId(Long id) {
        if (id == null) {
            return null;
        }
        SchoolBus schoolBus = new SchoolBus();
        schoolBus.setId(id);
        return schoolBus;
    }
}
