package com.purvik.jhipdemo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.purvik.jhipdemo.service.SchoolBusService;
import com.purvik.jhipdemo.web.rest.errors.BadRequestAlertException;
import com.purvik.jhipdemo.web.rest.util.HeaderUtil;
import com.purvik.jhipdemo.service.dto.SchoolBusDTO;
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
 * REST controller for managing SchoolBus.
 */
@RestController
@RequestMapping("/api")
public class SchoolBusResource {

    private final Logger log = LoggerFactory.getLogger(SchoolBusResource.class);

    private static final String ENTITY_NAME = "schoolBus";

    private SchoolBusService schoolBusService;

    public SchoolBusResource(SchoolBusService schoolBusService) {
        this.schoolBusService = schoolBusService;
    }

    /**
     * POST  /school-buses : Create a new schoolBus.
     *
     * @param schoolBusDTO the schoolBusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schoolBusDTO, or with status 400 (Bad Request) if the schoolBus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/school-buses")
    @Timed
    public ResponseEntity<SchoolBusDTO> createSchoolBus(@Valid @RequestBody SchoolBusDTO schoolBusDTO) throws URISyntaxException {
        log.debug("REST request to save SchoolBus : {}", schoolBusDTO);
        if (schoolBusDTO.getId() != null) {
            throw new BadRequestAlertException("A new schoolBus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolBusDTO result = schoolBusService.save(schoolBusDTO);
        return ResponseEntity.created(new URI("/api/school-buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /school-buses : Updates an existing schoolBus.
     *
     * @param schoolBusDTO the schoolBusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schoolBusDTO,
     * or with status 400 (Bad Request) if the schoolBusDTO is not valid,
     * or with status 500 (Internal Server Error) if the schoolBusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/school-buses")
    @Timed
    public ResponseEntity<SchoolBusDTO> updateSchoolBus(@Valid @RequestBody SchoolBusDTO schoolBusDTO) throws URISyntaxException {
        log.debug("REST request to update SchoolBus : {}", schoolBusDTO);
        if (schoolBusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SchoolBusDTO result = schoolBusService.save(schoolBusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schoolBusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /school-buses : get all the schoolBuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schoolBuses in body
     */
    @GetMapping("/school-buses")
    @Timed
    public List<SchoolBusDTO> getAllSchoolBuses() {
        log.debug("REST request to get all SchoolBuses");
        return schoolBusService.findAll();
    }

    /**
     * GET  /school-buses/:id : get the "id" schoolBus.
     *
     * @param id the id of the schoolBusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schoolBusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/school-buses/{id}")
    @Timed
    public ResponseEntity<SchoolBusDTO> getSchoolBus(@PathVariable Long id) {
        log.debug("REST request to get SchoolBus : {}", id);
        Optional<SchoolBusDTO> schoolBusDTO = schoolBusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolBusDTO);
    }

    /**
     * DELETE  /school-buses/:id : delete the "id" schoolBus.
     *
     * @param id the id of the schoolBusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/school-buses/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchoolBus(@PathVariable Long id) {
        log.debug("REST request to delete SchoolBus : {}", id);
        schoolBusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
