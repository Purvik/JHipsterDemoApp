package com.purvik.jhipdemo.web.rest;

import com.purvik.jhipdemo.TestDemo2App;

import com.purvik.jhipdemo.domain.SchoolBus;
import com.purvik.jhipdemo.repository.SchoolBusRepository;
import com.purvik.jhipdemo.service.SchoolBusService;
import com.purvik.jhipdemo.service.dto.SchoolBusDTO;
import com.purvik.jhipdemo.service.mapper.SchoolBusMapper;
import com.purvik.jhipdemo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.purvik.jhipdemo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SchoolBusResource REST controller.
 *
 * @see SchoolBusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDemo2App.class)
public class SchoolBusResourceIntTest {

    private static final Long DEFAULT_BUS_NO = 1L;
    private static final Long UPDATED_BUS_NO = 2L;

    private static final String DEFAULT_BUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_DRIVER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUS_DRIVER_NAME = "BBBBBBBBBB";

    @Autowired
    private SchoolBusRepository schoolBusRepository;

    @Autowired
    private SchoolBusMapper schoolBusMapper;
    
    @Autowired
    private SchoolBusService schoolBusService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchoolBusMockMvc;

    private SchoolBus schoolBus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SchoolBusResource schoolBusResource = new SchoolBusResource(schoolBusService);
        this.restSchoolBusMockMvc = MockMvcBuilders.standaloneSetup(schoolBusResource)
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
    public static SchoolBus createEntity(EntityManager em) {
        SchoolBus schoolBus = new SchoolBus()
            .busNo(DEFAULT_BUS_NO)
            .busType(DEFAULT_BUS_TYPE)
            .busDriverName(DEFAULT_BUS_DRIVER_NAME);
        return schoolBus;
    }

    @Before
    public void initTest() {
        schoolBus = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchoolBus() throws Exception {
        int databaseSizeBeforeCreate = schoolBusRepository.findAll().size();

        // Create the SchoolBus
        SchoolBusDTO schoolBusDTO = schoolBusMapper.toDto(schoolBus);
        restSchoolBusMockMvc.perform(post("/api/school-buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolBusDTO)))
            .andExpect(status().isCreated());

        // Validate the SchoolBus in the database
        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolBus testSchoolBus = schoolBusList.get(schoolBusList.size() - 1);
        assertThat(testSchoolBus.getBusNo()).isEqualTo(DEFAULT_BUS_NO);
        assertThat(testSchoolBus.getBusType()).isEqualTo(DEFAULT_BUS_TYPE);
        assertThat(testSchoolBus.getBusDriverName()).isEqualTo(DEFAULT_BUS_DRIVER_NAME);
    }

    @Test
    @Transactional
    public void createSchoolBusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schoolBusRepository.findAll().size();

        // Create the SchoolBus with an existing ID
        schoolBus.setId(1L);
        SchoolBusDTO schoolBusDTO = schoolBusMapper.toDto(schoolBus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolBusMockMvc.perform(post("/api/school-buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolBusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolBus in the database
        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBusNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolBusRepository.findAll().size();
        // set the field null
        schoolBus.setBusNo(null);

        // Create the SchoolBus, which fails.
        SchoolBusDTO schoolBusDTO = schoolBusMapper.toDto(schoolBus);

        restSchoolBusMockMvc.perform(post("/api/school-buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolBusDTO)))
            .andExpect(status().isBadRequest());

        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBusDriverNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolBusRepository.findAll().size();
        // set the field null
        schoolBus.setBusDriverName(null);

        // Create the SchoolBus, which fails.
        SchoolBusDTO schoolBusDTO = schoolBusMapper.toDto(schoolBus);

        restSchoolBusMockMvc.perform(post("/api/school-buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolBusDTO)))
            .andExpect(status().isBadRequest());

        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchoolBuses() throws Exception {
        // Initialize the database
        schoolBusRepository.saveAndFlush(schoolBus);

        // Get all the schoolBusList
        restSchoolBusMockMvc.perform(get("/api/school-buses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolBus.getId().intValue())))
            .andExpect(jsonPath("$.[*].busNo").value(hasItem(DEFAULT_BUS_NO.intValue())))
            .andExpect(jsonPath("$.[*].busType").value(hasItem(DEFAULT_BUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].busDriverName").value(hasItem(DEFAULT_BUS_DRIVER_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getSchoolBus() throws Exception {
        // Initialize the database
        schoolBusRepository.saveAndFlush(schoolBus);

        // Get the schoolBus
        restSchoolBusMockMvc.perform(get("/api/school-buses/{id}", schoolBus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schoolBus.getId().intValue()))
            .andExpect(jsonPath("$.busNo").value(DEFAULT_BUS_NO.intValue()))
            .andExpect(jsonPath("$.busType").value(DEFAULT_BUS_TYPE.toString()))
            .andExpect(jsonPath("$.busDriverName").value(DEFAULT_BUS_DRIVER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSchoolBus() throws Exception {
        // Get the schoolBus
        restSchoolBusMockMvc.perform(get("/api/school-buses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchoolBus() throws Exception {
        // Initialize the database
        schoolBusRepository.saveAndFlush(schoolBus);

        int databaseSizeBeforeUpdate = schoolBusRepository.findAll().size();

        // Update the schoolBus
        SchoolBus updatedSchoolBus = schoolBusRepository.findById(schoolBus.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolBus are not directly saved in db
        em.detach(updatedSchoolBus);
        updatedSchoolBus
            .busNo(UPDATED_BUS_NO)
            .busType(UPDATED_BUS_TYPE)
            .busDriverName(UPDATED_BUS_DRIVER_NAME);
        SchoolBusDTO schoolBusDTO = schoolBusMapper.toDto(updatedSchoolBus);

        restSchoolBusMockMvc.perform(put("/api/school-buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolBusDTO)))
            .andExpect(status().isOk());

        // Validate the SchoolBus in the database
        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeUpdate);
        SchoolBus testSchoolBus = schoolBusList.get(schoolBusList.size() - 1);
        assertThat(testSchoolBus.getBusNo()).isEqualTo(UPDATED_BUS_NO);
        assertThat(testSchoolBus.getBusType()).isEqualTo(UPDATED_BUS_TYPE);
        assertThat(testSchoolBus.getBusDriverName()).isEqualTo(UPDATED_BUS_DRIVER_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSchoolBus() throws Exception {
        int databaseSizeBeforeUpdate = schoolBusRepository.findAll().size();

        // Create the SchoolBus
        SchoolBusDTO schoolBusDTO = schoolBusMapper.toDto(schoolBus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolBusMockMvc.perform(put("/api/school-buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schoolBusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolBus in the database
        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSchoolBus() throws Exception {
        // Initialize the database
        schoolBusRepository.saveAndFlush(schoolBus);

        int databaseSizeBeforeDelete = schoolBusRepository.findAll().size();

        // Get the schoolBus
        restSchoolBusMockMvc.perform(delete("/api/school-buses/{id}", schoolBus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SchoolBus> schoolBusList = schoolBusRepository.findAll();
        assertThat(schoolBusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolBus.class);
        SchoolBus schoolBus1 = new SchoolBus();
        schoolBus1.setId(1L);
        SchoolBus schoolBus2 = new SchoolBus();
        schoolBus2.setId(schoolBus1.getId());
        assertThat(schoolBus1).isEqualTo(schoolBus2);
        schoolBus2.setId(2L);
        assertThat(schoolBus1).isNotEqualTo(schoolBus2);
        schoolBus1.setId(null);
        assertThat(schoolBus1).isNotEqualTo(schoolBus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolBusDTO.class);
        SchoolBusDTO schoolBusDTO1 = new SchoolBusDTO();
        schoolBusDTO1.setId(1L);
        SchoolBusDTO schoolBusDTO2 = new SchoolBusDTO();
        assertThat(schoolBusDTO1).isNotEqualTo(schoolBusDTO2);
        schoolBusDTO2.setId(schoolBusDTO1.getId());
        assertThat(schoolBusDTO1).isEqualTo(schoolBusDTO2);
        schoolBusDTO2.setId(2L);
        assertThat(schoolBusDTO1).isNotEqualTo(schoolBusDTO2);
        schoolBusDTO1.setId(null);
        assertThat(schoolBusDTO1).isNotEqualTo(schoolBusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(schoolBusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(schoolBusMapper.fromId(null)).isNull();
    }
}
