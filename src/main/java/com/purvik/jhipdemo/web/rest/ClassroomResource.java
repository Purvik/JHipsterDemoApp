package com.purvik.jhipdemo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.purvik.jhipdemo.service.ClassroomService;
import com.purvik.jhipdemo.service.StudentService;
import com.purvik.jhipdemo.service.SubjectService;
import com.purvik.jhipdemo.service.dto.*;
import com.purvik.jhipdemo.web.rest.errors.BadRequestAlertException;
import com.purvik.jhipdemo.web.rest.util.HeaderUtil;
import com.purvik.jhipdemo.service.ClassroomQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Classroom.
 */
@RestController
@RequestMapping("/api")
public class ClassroomResource {

    private final Logger log = LoggerFactory.getLogger(ClassroomResource.class);

    private static final String ENTITY_NAME = "classroom";

    private ClassroomService classroomService;

    private ClassroomQueryService classroomQueryService;

    private SubjectService  subjectService;

    private StudentService studentService;

    public ClassroomResource(ClassroomService classroomService, ClassroomQueryService classroomQueryService, SubjectService subjectService, StudentService studentService) {
        this.classroomService = classroomService;
        this.classroomQueryService = classroomQueryService;
        this.subjectService = subjectService;
        this.studentService = studentService;
    }

    /**
     * POST  /classrooms : Create a new classroom.
     *
     * @param classroomDTO the classroomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classroomDTO, or with status 400 (Bad Request) if the classroom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/classrooms")
    @Timed
    public ResponseEntity<ClassroomDTO> createClassroom(@Valid @RequestBody ClassroomDTO classroomDTO) throws URISyntaxException {
        log.debug("REST request to save Classroom : {}", classroomDTO);
        if (classroomDTO.getId() != null) {
            throw new BadRequestAlertException("A new classroom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassroomDTO result = classroomService.save(classroomDTO);
        return ResponseEntity.created(new URI("/api/classrooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classrooms : Updates an existing classroom.
     *
     * @param classroomDTO the classroomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classroomDTO,
     * or with status 400 (Bad Request) if the classroomDTO is not valid,
     * or with status 500 (Internal Server Error) if the classroomDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/classrooms")
    @Timed
    public ResponseEntity<ClassroomDTO> updateClassroom(@Valid @RequestBody ClassroomDTO classroomDTO) throws URISyntaxException {
        log.debug("REST request to update Classroom : {}", classroomDTO);
        if (classroomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassroomDTO result = classroomService.save(classroomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classroomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classrooms : get all the classrooms.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of classrooms in body
     */
    @GetMapping("/classrooms")
    @Timed
    public ResponseEntity<List<ClassroomDTO>> getAllClassrooms(ClassroomCriteria criteria) {
        log.debug("REST request to get Classrooms by criteria: {}", criteria);
        List<ClassroomDTO> entityList = classroomQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /classrooms/count : count all the classrooms.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/classrooms/count")
    @Timed
    public ResponseEntity<Long> countClassrooms(ClassroomCriteria criteria) {
        log.debug("REST request to count Classrooms by criteria: {}", criteria);
        return ResponseEntity.ok().body(classroomQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /classrooms/:id : get the "id" classroom.
     *
     * @param id the id of the classroomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classroomDTO, or with status 404 (Not Found)
     */
    @GetMapping("/classrooms/{id}")
    @Timed
    public ResponseEntity<ClassroomDTO> getClassroom(@PathVariable Long id) {
        log.debug("REST request to get Classroom : {}", id);
        Optional<ClassroomDTO> classroomDTO = classroomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classroomDTO);
    }

    /**
     * DELETE  /classrooms/:id : delete the "id" classroom.
     *
     * @param id the id of the classroomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/classrooms/{id}")
    @Timed
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        log.debug("REST request to delete Classroom : {}", id);
        classroomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/classrooms/{id}/teachers")
    @Timed
    public List<TeacherClassroomDTO> listTeacherOfClassId(@PathVariable Long id){
        return classroomService.listTeacherOfClassId(id);
    }

    @GetMapping("/classrooms/{id}/subjects")
    @Timed
    public List<SubjectDTO> findAllByClassroomId(@PathVariable Long id){
        return subjectService.findAllByClassroomId(id);
    }

    @GetMapping("/classrooms/{id}/students")
    @Timed
    public List<StudentDTO> findAllStudentsByClassroomId(@PathVariable Long id){
        return studentService.findAllByClassroomId(id);
    }

}
