package com.getgreetapp.greetapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.getgreetapp.greetapp.domain.Greet;
import com.getgreetapp.greetapp.repository.GreetRepository;
import com.getgreetapp.greetapp.web.rest.errors.BadRequestAlertException;
import com.getgreetapp.greetapp.web.rest.util.HeaderUtil;
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
 * REST controller for managing Greet.
 */
@RestController
@RequestMapping("/api")
public class GreetResource {

    private final Logger log = LoggerFactory.getLogger(GreetResource.class);

    private static final String ENTITY_NAME = "greet";

    private final GreetRepository greetRepository;

    public GreetResource(GreetRepository greetRepository) {
        this.greetRepository = greetRepository;
    }

    /**
     * POST  /greets : Create a new greet.
     *
     * @param greet the greet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new greet, or with status 400 (Bad Request) if the greet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/greets")
    @Timed
    public ResponseEntity<Greet> createGreet(@Valid @RequestBody Greet greet) throws URISyntaxException {
        log.debug("REST request to save Greet : {}", greet);
        if (greet.getId() != null) {
            throw new BadRequestAlertException("A new greet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Greet result = greetRepository.save(greet);
        return ResponseEntity.created(new URI("/api/greets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /greets : Updates an existing greet.
     *
     * @param greet the greet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated greet,
     * or with status 400 (Bad Request) if the greet is not valid,
     * or with status 500 (Internal Server Error) if the greet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/greets")
    @Timed
    public ResponseEntity<Greet> updateGreet(@Valid @RequestBody Greet greet) throws URISyntaxException {
        log.debug("REST request to update Greet : {}", greet);
        if (greet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Greet result = greetRepository.save(greet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, greet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /greets : get all the greets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of greets in body
     */
    @GetMapping("/greets")
    @Timed
    public List<Greet> getAllGreets() {
        log.debug("REST request to get all Greets");
        return greetRepository.findAll();
    }

    /**
     * GET  /greets-by-user/:userId : get all the greets by user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of greets in body
     */
    @GetMapping("/greets-by-user/{userId}")
    @Timed
    public List<Greet> getAllGreetsByUser(@PathVariable Long userId) {
        log.debug("REST request to get all Greets by user");
        return greetRepository.findByUser(userId);
    }

    /**
     * GET  /greets-by-user/:groupId : get all the greets by group.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of greets in body
     */
    @GetMapping("/greets-by-group/{groupId}")
    @Timed
    public List<Greet> getAllGreetsBGroup(@PathVariable Long groupId) {
        log.debug("REST request to get all Greets by user");
        return greetRepository.findByGroup(groupId);
    }

    /**
     * GET  /greets/:id : get the "id" greet.
     *
     * @param id the id of the greet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the greet, or with status 404 (Not Found)
     */
    @GetMapping("/greets/{id}")
    @Timed
    public ResponseEntity<Greet> getGreet(@PathVariable Long id) {
        log.debug("REST request to get Greet : {}", id);
        Optional<Greet> greet = greetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(greet);
    }

    /**
     * DELETE  /greets/:id : delete the "id" greet.
     *
     * @param id the id of the greet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/greets/{id}")
    @Timed
    public ResponseEntity<Void> deleteGreet(@PathVariable Long id) {
        log.debug("REST request to delete Greet : {}", id);

        greetRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}