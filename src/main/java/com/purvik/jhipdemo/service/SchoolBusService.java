package com.purvik.jhipdemo.service;

import com.purvik.jhipdemo.domain.SchoolBus;
import com.purvik.jhipdemo.repository.SchoolBusRepository;
import com.purvik.jhipdemo.service.dto.SchoolBusDTO;
import com.purvik.jhipdemo.service.mapper.SchoolBusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SchoolBus.
 */
@Service
@Transactional
public class SchoolBusService {

    private final Logger log = LoggerFactory.getLogger(SchoolBusService.class);

    private SchoolBusRepository schoolBusRepository;

    private SchoolBusMapper schoolBusMapper;

    public SchoolBusService(SchoolBusRepository schoolBusRepository, SchoolBusMapper schoolBusMapper) {
        this.schoolBusRepository = schoolBusRepository;
        this.schoolBusMapper = schoolBusMapper;
    }

    /**
     * Save a schoolBus.
     *
     * @param schoolBusDTO the entity to save
     * @return the persisted entity
     */
    public SchoolBusDTO save(SchoolBusDTO schoolBusDTO) {
        log.debug("Request to save SchoolBus : {}", schoolBusDTO);

        SchoolBus schoolBus = schoolBusMapper.toEntity(schoolBusDTO);
        schoolBus = schoolBusRepository.save(schoolBus);
        return schoolBusMapper.toDto(schoolBus);
    }

    /**
     * Get all the schoolBuses.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SchoolBusDTO> findAll() {
        log.debug("Request to get all SchoolBuses");
        return schoolBusRepository.findAll().stream()
            .map(schoolBusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one schoolBus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SchoolBusDTO> findOne(Long id) {
        log.debug("Request to get SchoolBus : {}", id);
        return schoolBusRepository.findById(id)
            .map(schoolBusMapper::toDto);
    }

    /**
     * Delete the schoolBus by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolBus : {}", id);
        schoolBusRepository.deleteById(id);
    }
}
