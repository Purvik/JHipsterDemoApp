package com.purvik.jhipdemo.service;

import com.purvik.jhipdemo.domain.Classroom;
import com.purvik.jhipdemo.repository.ClassroomRepository;
import com.purvik.jhipdemo.service.dto.ClassroomDTO;
import com.purvik.jhipdemo.service.dto.TeacherClassroomDTO;
import com.purvik.jhipdemo.service.mapper.ClassroomMapper;
import com.purvik.jhipdemo.service.mapper.TeacherClassRoomMapper;
import com.purvik.jhipdemo.service.mapper.TeacherMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Classroom.
 */
@Service
@Transactional
public class ClassroomService {

    private final Logger log = LoggerFactory.getLogger(ClassroomService.class);

    private ClassroomRepository classroomRepository;

    private ClassroomMapper classroomMapper;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper) {
        this.classroomRepository = classroomRepository;
        this.classroomMapper = classroomMapper;
    }

    /**
     * Save a classroom.
     *
     * @param classroomDTO the entity to save
     * @return the persisted entity
     */
    public ClassroomDTO save(ClassroomDTO classroomDTO) {
        log.debug("Request to save Classroom : {}", classroomDTO);

        Classroom classroom = classroomMapper.toEntity(classroomDTO);
        classroom = classroomRepository.save(classroom);
        return classroomMapper.toDto(classroom);
    }

    /**
     * Get all the classrooms.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ClassroomDTO> findAll() {
        log.debug("Request to get all Classrooms");
        return classroomRepository.findAllWithEagerRelationships().stream()
            .map(classroomMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Classroom with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ClassroomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return classroomRepository.findAllWithEagerRelationships(pageable).map(classroomMapper::toDto);
    }
    

    /**
     * Get one classroom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ClassroomDTO> findOne(Long id) {
        log.debug("Request to get Classroom : {}", id);
        return classroomRepository.findOneWithEagerRelationships(id)
            .map(classroomMapper::toDto);
    }

    /**
     * Delete the classroom by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Classroom : {}", id);
        classroomRepository.deleteById(id);
    }

    /**
     * List Teacher of a Particulr Classroom
     * @param id of the classroom
     * */
    public List<TeacherClassroomDTO> listTeacherOfClassId(Long id){
        return classroomRepository.listTeacherOfClassId(id).stream()
            .map(teacher -> {
                TeacherClassroomDTO teacherClassroomDTO = new TeacherClassroomDTO();
                teacherClassroomDTO.setId(teacher.getId());
                teacherClassroomDTO.setEmail(teacher.getEmail());
                teacherClassroomDTO.setFirstName(teacher.getFirstName());
                teacherClassroomDTO.setLastName(teacher.getLastName());
                return teacherClassroomDTO;
            })
            .collect(Collectors.toList());
    }
}
