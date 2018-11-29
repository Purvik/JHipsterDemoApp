package com.purvik.jhipdemo.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.purvik.jhipdemo.domain.Classroom;
import com.purvik.jhipdemo.domain.*; // for static metamodels
import com.purvik.jhipdemo.repository.ClassroomRepository;
import com.purvik.jhipdemo.service.dto.ClassroomCriteria;
import com.purvik.jhipdemo.service.dto.ClassroomDTO;
import com.purvik.jhipdemo.service.mapper.ClassroomMapper;

/**
 * Service for executing complex queries for Classroom entities in the database.
 * The main input is a {@link ClassroomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassroomDTO} or a {@link Page} of {@link ClassroomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassroomQueryService extends QueryService<Classroom> {

    private final Logger log = LoggerFactory.getLogger(ClassroomQueryService.class);

    private ClassroomRepository classroomRepository;

    private ClassroomMapper classroomMapper;

    public ClassroomQueryService(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper) {
        this.classroomRepository = classroomRepository;
        this.classroomMapper = classroomMapper;
    }

    /**
     * Return a {@link List} of {@link ClassroomDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassroomDTO> findByCriteria(ClassroomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Classroom> specification = createSpecification(criteria);
        return classroomMapper.toDto(classroomRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClassroomDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassroomDTO> findByCriteria(ClassroomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classroom> specification = createSpecification(criteria);
        return classroomRepository.findAll(specification, page)
            .map(classroomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassroomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Classroom> specification = createSpecification(criteria);
        return classroomRepository.count(specification);
    }

    /**
     * Function to convert ClassroomCriteria to a {@link Specification}
     */
    private Specification<Classroom> createSpecification(ClassroomCriteria criteria) {
        Specification<Classroom> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Classroom_.id));
            }
            if (criteria.getRoomNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomNo(), Classroom_.roomNo));
            }
            if (criteria.getRoomStandard() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomStandard(), Classroom_.roomStandard));
            }
            if (criteria.getTeacherId() != null) {
                specification = specification.and(buildSpecification(criteria.getTeacherId(),
                    root -> root.join(Classroom_.teachers, JoinType.LEFT).get(Teacher_.id)));
            }
        }
        return specification;
    }
}
