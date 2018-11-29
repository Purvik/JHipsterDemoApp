package com.purvik.jhipdemo.service;

import com.purvik.jhipdemo.domain.Subject;
import com.purvik.jhipdemo.repository.SubjectRepository;
import com.purvik.jhipdemo.service.dto.SubjectDTO;
import com.purvik.jhipdemo.service.dto.TeacherClassroomDTO;
import com.purvik.jhipdemo.service.mapper.SubjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Subject.
 */
@Service
@Transactional
public class SubjectService {

    private final Logger log = LoggerFactory.getLogger(SubjectService.class);

    private SubjectRepository subjectRepository;

    private SubjectMapper subjectMapper;


    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    /**
     * Save a subject.
     *
     * @param subjectDTO the entity to save
     * @return the persisted entity
     */
    public SubjectDTO save(SubjectDTO subjectDTO) {
        log.debug("Request to save Subject : {}", subjectDTO);

        Subject subject = subjectMapper.toEntity(subjectDTO);
        subject = subjectRepository.save(subject);
        return subjectMapper.toDto(subject);
    }

    /**
     * Get all the subjects.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SubjectDTO> findAll() {
        log.debug("Request to get all Subjects");
        return subjectRepository.findAll().stream()
            .map(subjectMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one subject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SubjectDTO> findOne(Long id) {
        log.debug("Request to get Subject : {}", id);
        return subjectRepository.findById(id)
            .map(subjectMapper::toDto);
    }

    /**
     * Delete the subject by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Subject : {}", id);
        subjectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SubjectDTO> findAllByClassroomId(Long id){
        return subjectRepository.findAllByClassroomId(id).stream()
            .map(subjectMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectDTO> findByCode(String searchterm){
        Long term = new Long(searchterm);
        return subjectRepository.findByCode(term).stream()
            .map(subjectMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectDTO> findByTitleOrDate(String title){
        return subjectRepository.findByTitleContainsAllIgnoreCase(title).stream()
            .map(subjectMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectDTO> findByDate(LocalDate datePart){

        List<Subject> subjectList = subjectRepository.findByDate(datePart);
        return subjectList.stream()
            .map(subjectMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get list of Teacher for a Subject
     * */
    public List<TeacherClassroomDTO> findAllTeacherBySubjectId(Long id){

        return subjectRepository.listTeacherBySubjectId(id).stream()
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
