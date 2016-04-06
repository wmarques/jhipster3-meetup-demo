package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Bankaccount;
import com.mycompany.myapp.repository.BankaccountRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Bankaccount.
 */
@RestController
@RequestMapping("/api")
public class BankaccountResource {

    private final Logger log = LoggerFactory.getLogger(BankaccountResource.class);
        
    @Inject
    private BankaccountRepository bankaccountRepository;
    
    /**
     * POST  /bankaccounts : Create a new bankaccount.
     *
     * @param bankaccount the bankaccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bankaccount, or with status 400 (Bad Request) if the bankaccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bankaccounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bankaccount> createBankaccount(@Valid @RequestBody Bankaccount bankaccount) throws URISyntaxException {
        log.debug("REST request to save Bankaccount : {}", bankaccount);
        if (bankaccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bankaccount", "idexists", "A new bankaccount cannot already have an ID")).body(null);
        }
        Bankaccount result = bankaccountRepository.save(bankaccount);
        return ResponseEntity.created(new URI("/api/bankaccounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bankaccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bankaccounts : Updates an existing bankaccount.
     *
     * @param bankaccount the bankaccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bankaccount,
     * or with status 400 (Bad Request) if the bankaccount is not valid,
     * or with status 500 (Internal Server Error) if the bankaccount couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bankaccounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bankaccount> updateBankaccount(@Valid @RequestBody Bankaccount bankaccount) throws URISyntaxException {
        log.debug("REST request to update Bankaccount : {}", bankaccount);
        if (bankaccount.getId() == null) {
            return createBankaccount(bankaccount);
        }
        Bankaccount result = bankaccountRepository.save(bankaccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bankaccount", bankaccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bankaccounts : get all the bankaccounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bankaccounts in body
     */
    @RequestMapping(value = "/bankaccounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bankaccount> getAllBankaccounts() {
        log.debug("REST request to get all Bankaccounts");
        List<Bankaccount> bankaccounts = bankaccountRepository.findAll();
        return bankaccounts;
    }

    /**
     * GET  /bankaccounts/:id : get the "id" bankaccount.
     *
     * @param id the id of the bankaccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bankaccount, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bankaccounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bankaccount> getBankaccount(@PathVariable Long id) {
        log.debug("REST request to get Bankaccount : {}", id);
        Bankaccount bankaccount = bankaccountRepository.findOne(id);
        return Optional.ofNullable(bankaccount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bankaccounts/:id : delete the "id" bankaccount.
     *
     * @param id the id of the bankaccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bankaccounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBankaccount(@PathVariable Long id) {
        log.debug("REST request to delete Bankaccount : {}", id);
        bankaccountRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bankaccount", id.toString())).build();
    }

}
