package com.purvik.jhipdemo.service;

import com.purvik.jhipdemo.domain.Student;
import com.purvik.jhipdemo.repository.StudentRepository;
import com.purvik.jhipdemo.service.dto.StudentDTO;
import com.purvik.jhipdemo.service.dto.SubjectDTO;
import com.purvik.jhipdemo.service.mapper.StudentMapper;
import com.purvik.jhipdemo.web.rest.errors.GlobalErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    private StudentRepository studentRepository;

    private StudentMapper studentMapper;

    private SubjectService subjectService;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, SubjectService subjectService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.subjectService = subjectService;
    }

    /**
     * Save a student.
     *
     * @param studentDTO the entity to save
     * @return the persisted entity
     */
    public StudentDTO save(StudentDTO studentDTO) {
        log.debug("Request to save Student : {}", studentDTO);

        Student student = studentMapper.toEntity(studentDTO);
        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    /**
     * Get all the students.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findAll() {
        log.debug("Request to get all Students");
        return studentRepository.findAll().stream()
            .map(studentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one student by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StudentDTO> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findById(id)
            .map(studentMapper::toDto);
    }

    /**
     * Delete the student by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }

    /**
     * Find List<Student> by Classroom
     * */
    public List<StudentDTO> findAllByClassroomId(Long id){
        return studentRepository.findAllByClassroomId(id)
            .stream()
            .map(studentMapper::toDto)
            .collect(Collectors.toList());

    }

    /**
     * Find Subjects for a particular Student
     * @param id of the student to get Classroom Id
     * */
    public List<SubjectDTO> findAllSubjectOfStudent(Long id) {
        Long classroomId;
        Optional<StudentDTO> studentDTO = studentRepository.findById(id).map(studentMapper::toDto);

        if (studentDTO.isPresent()) {
            classroomId = studentDTO.get().getClassroomId();
            return subjectService.findAllByClassroomId(classroomId);
        }else{
            throw new GlobalErrors("No Student Found with id: " + id,101, HttpStatus.NOT_FOUND.value());
        }
    }

}
