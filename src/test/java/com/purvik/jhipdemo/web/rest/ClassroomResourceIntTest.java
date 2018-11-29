package com.purvik.jhipdemo.web.rest;

import com.purvik.jhipdemo.TestDemo2App;

import com.purvik.jhipdemo.domain.Classroom;
import com.purvik.jhipdemo.domain.Teacher;
import com.purvik.jhipdemo.repository.ClassroomRepository;
import com.purvik.jhipdemo.service.ClassroomService;
import com.purvik.jhipdemo.service.StudentService;
import com.purvik.jhipdemo.service.SubjectService;
import com.purvik.jhipdemo.service.dto.ClassroomDTO;
import com.purvik.jhipdemo.service.mapper.ClassroomMapper;
import com.purvik.jhipdemo.web.rest.errors.ExceptionTranslator;
import com.purvik.jhipdemo.service.ClassroomQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.purvik.jhipdemo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClassroomResource REST controller.
 *
 * @see ClassroomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDemo2App.class)
public class ClassroomResourceIntTest {

    private static final Long DEFAULT_ROOM_NO = 1L;
    private static final Long UPDATED_ROOM_NO = 2L;

    private static final Long DEFAULT_ROOM_STANDARD = 1L;
    private static final Long UPDATED_ROOM_STANDARD = 2L;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Mock
    private ClassroomRepository classroomRepositoryMock;

    @Autowired
    private ClassroomMapper classroomMapper;
    

    @Mock
    private ClassroomService classroomServiceMock;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassroomQueryService classroomQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassroomMockMvc;

    private Classroom classroom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassroomResource classroomResource = new ClassroomResource(classroomService, classroomQueryService, subjectService, studentService);
        this.restClassroomMockMvc = MockMvcBuilders.standaloneSetup(classroomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createEntity(EntityManager em) {
        Classroom classroom = new Classroom()
            .roomNo(DEFAULT_ROOM_NO)
            .roomStandard(DEFAULT_ROOM_STANDARD);
        return classroom;
    }

    @Before
    public void initTest() {
        classroom = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassroom() throws Exception {
        int databaseSizeBeforeCreate = classroomRepository.findAll().size();

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);
        restClassroomMockMvc.perform(post("/api/classrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classroomDTO)))
            .andExpect(status().isCreated());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeCreate + 1);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getRoomNo()).isEqualTo(DEFAULT_ROOM_NO);
        assertThat(testClassroom.getRoomStandard()).isEqualTo(DEFAULT_ROOM_STANDARD);
    }

    @Test
    @Transactional
    public void createClassroomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classroomRepository.findAll().size();

        // Create the Classroom with an existing ID
        classroom.setId(1L);
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassroomMockMvc.perform(post("/api/classrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRoomNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = classroomRepository.findAll().size();
        // set the field null
        classroom.setRoomNo(null);

        // Create the Classroom, which fails.
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        restClassroomMockMvc.perform(post("/api/classrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoomStandardIsRequired() throws Exception {
        int databaseSizeBeforeTest = classroomRepository.findAll().size();
        // set the field null
        classroom.setRoomStandard(null);

        // Create the Classroom, which fails.
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        restClassroomMockMvc.perform(post("/api/classrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassrooms() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList
        restClassroomMockMvc.perform(get("/api/classrooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNo").value(hasItem(DEFAULT_ROOM_NO.intValue())))
            .andExpect(jsonPath("$.[*].roomStandard").value(hasItem(DEFAULT_ROOM_STANDARD.intValue())));
    }

    public void getAllClassroomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ClassroomResource classroomResource = new ClassroomResource(classroomServiceMock, classroomQueryService, subjectService, studentService);
            when(classroomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restClassroomMockMvc = MockMvcBuilders.standaloneSetup(classroomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restClassroomMockMvc.perform(get("/api/classrooms?eagerload=true"))
        .andExpect(status().isOk());

            verify(classroomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get the classroom
        restClassroomMockMvc.perform(get("/api/classrooms/{id}", classroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classroom.getId().intValue()))
            .andExpect(jsonPath("$.roomNo").value(DEFAULT_ROOM_NO.intValue()))
            .andExpect(jsonPath("$.roomStandard").value(DEFAULT_ROOM_STANDARD.intValue()));
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomNoIsEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomNo equals to DEFAULT_ROOM_NO
        defaultClassroomShouldBeFound("roomNo.equals=" + DEFAULT_ROOM_NO);

        // Get all the classroomList where roomNo equals to UPDATED_ROOM_NO
        defaultClassroomShouldNotBeFound("roomNo.equals=" + UPDATED_ROOM_NO);
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomNoIsInShouldWork() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomNo in DEFAULT_ROOM_NO or UPDATED_ROOM_NO
        defaultClassroomShouldBeFound("roomNo.in=" + DEFAULT_ROOM_NO + "," + UPDATED_ROOM_NO);

        // Get all the classroomList where roomNo equals to UPDATED_ROOM_NO
        defaultClassroomShouldNotBeFound("roomNo.in=" + UPDATED_ROOM_NO);
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomNo is not null
        defaultClassroomShouldBeFound("roomNo.specified=true");

        // Get all the classroomList where roomNo is null
        defaultClassroomShouldNotBeFound("roomNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomNo greater than or equals to DEFAULT_ROOM_NO
        defaultClassroomShouldBeFound("roomNo.greaterOrEqualThan=" + DEFAULT_ROOM_NO);

        // Get all the classroomList where roomNo greater than or equals to UPDATED_ROOM_NO
        defaultClassroomShouldNotBeFound("roomNo.greaterOrEqualThan=" + UPDATED_ROOM_NO);
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomNoIsLessThanSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomNo less than or equals to DEFAULT_ROOM_NO
        defaultClassroomShouldNotBeFound("roomNo.lessThan=" + DEFAULT_ROOM_NO);

        // Get all the classroomList where roomNo less than or equals to UPDATED_ROOM_NO
        defaultClassroomShouldBeFound("roomNo.lessThan=" + UPDATED_ROOM_NO);
    }


    @Test
    @Transactional
    public void getAllClassroomsByRoomStandardIsEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomStandard equals to DEFAULT_ROOM_STANDARD
        defaultClassroomShouldBeFound("roomStandard.equals=" + DEFAULT_ROOM_STANDARD);

        // Get all the classroomList where roomStandard equals to UPDATED_ROOM_STANDARD
        defaultClassroomShouldNotBeFound("roomStandard.equals=" + UPDATED_ROOM_STANDARD);
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomStandardIsInShouldWork() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomStandard in DEFAULT_ROOM_STANDARD or UPDATED_ROOM_STANDARD
        defaultClassroomShouldBeFound("roomStandard.in=" + DEFAULT_ROOM_STANDARD + "," + UPDATED_ROOM_STANDARD);

        // Get all the classroomList where roomStandard equals to UPDATED_ROOM_STANDARD
        defaultClassroomShouldNotBeFound("roomStandard.in=" + UPDATED_ROOM_STANDARD);
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomStandardIsNullOrNotNull() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomStandard is not null
        defaultClassroomShouldBeFound("roomStandard.specified=true");

        // Get all the classroomList where roomStandard is null
        defaultClassroomShouldNotBeFound("roomStandard.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomStandardIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomStandard greater than or equals to DEFAULT_ROOM_STANDARD
        defaultClassroomShouldBeFound("roomStandard.greaterOrEqualThan=" + DEFAULT_ROOM_STANDARD);

        // Get all the classroomList where roomStandard greater than or equals to UPDATED_ROOM_STANDARD
        defaultClassroomShouldNotBeFound("roomStandard.greaterOrEqualThan=" + UPDATED_ROOM_STANDARD);
    }

    @Test
    @Transactional
    public void getAllClassroomsByRoomStandardIsLessThanSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where roomStandard less than or equals to DEFAULT_ROOM_STANDARD
        defaultClassroomShouldNotBeFound("roomStandard.lessThan=" + DEFAULT_ROOM_STANDARD);

        // Get all the classroomList where roomStandard less than or equals to UPDATED_ROOM_STANDARD
        defaultClassroomShouldBeFound("roomStandard.lessThan=" + UPDATED_ROOM_STANDARD);
    }


    @Test
    @Transactional
    public void getAllClassroomsByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        Teacher teacher = TeacherResourceIntTest.createEntity(em);
        em.persist(teacher);
        em.flush();
        classroom.addTeacher(teacher);
        classroomRepository.saveAndFlush(classroom);
        Long teacherId = teacher.getId();

        // Get all the classroomList where teacher equals to teacherId
        defaultClassroomShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the classroomList where teacher equals to teacherId + 1
        defaultClassroomShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultClassroomShouldBeFound(String filter) throws Exception {
        restClassroomMockMvc.perform(get("/api/classrooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNo").value(hasItem(DEFAULT_ROOM_NO.intValue())))
            .andExpect(jsonPath("$.[*].roomStandard").value(hasItem(DEFAULT_ROOM_STANDARD.intValue())));

        // Check, that the count call also returns 1
        restClassroomMockMvc.perform(get("/api/classrooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultClassroomShouldNotBeFound(String filter) throws Exception {
        restClassroomMockMvc.perform(get("/api/classrooms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassroomMockMvc.perform(get("/api/classrooms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClassroom() throws Exception {
        // Get the classroom
        restClassroomMockMvc.perform(get("/api/classrooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom
        Classroom updatedClassroom = classroomRepository.findById(classroom.getId()).get();
        // Disconnect from session so that the updates on updatedClassroom are not directly saved in db
        em.detach(updatedClassroom);
        updatedClassroom
            .roomNo(UPDATED_ROOM_NO)
            .roomStandard(UPDATED_ROOM_STANDARD);
        ClassroomDTO classroomDTO = classroomMapper.toDto(updatedClassroom);

        restClassroomMockMvc.perform(put("/api/classrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classroomDTO)))
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getRoomNo()).isEqualTo(UPDATED_ROOM_NO);
        assertThat(testClassroom.getRoomStandard()).isEqualTo(UPDATED_ROOM_STANDARD);
    }

    @Test
    @Transactional
    public void updateNonExistingClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc.perform(put("/api/classrooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeDelete = classroomRepository.findAll().size();

        // Get the classroom
        restClassroomMockMvc.perform(delete("/api/classrooms/{id}", classroom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classroom.class);
        Classroom classroom1 = new Classroom();
        classroom1.setId(1L);
        Classroom classroom2 = new Classroom();
        classroom2.setId(classroom1.getId());
        assertThat(classroom1).isEqualTo(classroom2);
        classroom2.setId(2L);
        assertThat(classroom1).isNotEqualTo(classroom2);
        classroom1.setId(null);
        assertThat(classroom1).isNotEqualTo(classroom2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassroomDTO.class);
        ClassroomDTO classroomDTO1 = new ClassroomDTO();
        classroomDTO1.setId(1L);
        ClassroomDTO classroomDTO2 = new ClassroomDTO();
        assertThat(classroomDTO1).isNotEqualTo(classroomDTO2);
        classroomDTO2.setId(classroomDTO1.getId());
        assertThat(classroomDTO1).isEqualTo(classroomDTO2);
        classroomDTO2.setId(2L);
        assertThat(classroomDTO1).isNotEqualTo(classroomDTO2);
        classroomDTO1.setId(null);
        assertThat(classroomDTO1).isNotEqualTo(classroomDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(classroomMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(classroomMapper.fromId(null)).isNull();
    }
}
