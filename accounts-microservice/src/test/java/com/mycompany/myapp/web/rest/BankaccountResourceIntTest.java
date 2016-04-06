package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AccountsMicroserviceApp;
import com.mycompany.myapp.domain.Bankaccount;
import com.mycompany.myapp.repository.BankaccountRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BankaccountResource REST controller.
 *
 * @see BankaccountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountsMicroserviceApp.class)
@WebAppConfiguration
@IntegrationTest
public class BankaccountResourceIntTest {

    private static final String DEFAULT_CLIENTNAME = "AAAAA";
    private static final String UPDATED_CLIENTNAME = "BBBBB";

    private static final Float DEFAULT_BALANCE = 1F;
    private static final Float UPDATED_BALANCE = 2F;

    @Inject
    private BankaccountRepository bankaccountRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBankaccountMockMvc;

    private Bankaccount bankaccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BankaccountResource bankaccountResource = new BankaccountResource();
        ReflectionTestUtils.setField(bankaccountResource, "bankaccountRepository", bankaccountRepository);
        this.restBankaccountMockMvc = MockMvcBuilders.standaloneSetup(bankaccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bankaccount = new Bankaccount();
        bankaccount.setClientname(DEFAULT_CLIENTNAME);
        bankaccount.setBalance(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createBankaccount() throws Exception {
        int databaseSizeBeforeCreate = bankaccountRepository.findAll().size();

        // Create the Bankaccount

        restBankaccountMockMvc.perform(post("/api/bankaccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankaccount)))
                .andExpect(status().isCreated());

        // Validate the Bankaccount in the database
        List<Bankaccount> bankaccounts = bankaccountRepository.findAll();
        assertThat(bankaccounts).hasSize(databaseSizeBeforeCreate + 1);
        Bankaccount testBankaccount = bankaccounts.get(bankaccounts.size() - 1);
        assertThat(testBankaccount.getClientname()).isEqualTo(DEFAULT_CLIENTNAME);
        assertThat(testBankaccount.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void checkClientnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankaccountRepository.findAll().size();
        // set the field null
        bankaccount.setClientname(null);

        // Create the Bankaccount, which fails.

        restBankaccountMockMvc.perform(post("/api/bankaccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankaccount)))
                .andExpect(status().isBadRequest());

        List<Bankaccount> bankaccounts = bankaccountRepository.findAll();
        assertThat(bankaccounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankaccountRepository.findAll().size();
        // set the field null
        bankaccount.setBalance(null);

        // Create the Bankaccount, which fails.

        restBankaccountMockMvc.perform(post("/api/bankaccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankaccount)))
                .andExpect(status().isBadRequest());

        List<Bankaccount> bankaccounts = bankaccountRepository.findAll();
        assertThat(bankaccounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBankaccounts() throws Exception {
        // Initialize the database
        bankaccountRepository.saveAndFlush(bankaccount);

        // Get all the bankaccounts
        restBankaccountMockMvc.perform(get("/api/bankaccounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bankaccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].clientname").value(hasItem(DEFAULT_CLIENTNAME.toString())))
                .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void getBankaccount() throws Exception {
        // Initialize the database
        bankaccountRepository.saveAndFlush(bankaccount);

        // Get the bankaccount
        restBankaccountMockMvc.perform(get("/api/bankaccounts/{id}", bankaccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bankaccount.getId().intValue()))
            .andExpect(jsonPath("$.clientname").value(DEFAULT_CLIENTNAME.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBankaccount() throws Exception {
        // Get the bankaccount
        restBankaccountMockMvc.perform(get("/api/bankaccounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBankaccount() throws Exception {
        // Initialize the database
        bankaccountRepository.saveAndFlush(bankaccount);
        int databaseSizeBeforeUpdate = bankaccountRepository.findAll().size();

        // Update the bankaccount
        Bankaccount updatedBankaccount = new Bankaccount();
        updatedBankaccount.setId(bankaccount.getId());
        updatedBankaccount.setClientname(UPDATED_CLIENTNAME);
        updatedBankaccount.setBalance(UPDATED_BALANCE);

        restBankaccountMockMvc.perform(put("/api/bankaccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBankaccount)))
                .andExpect(status().isOk());

        // Validate the Bankaccount in the database
        List<Bankaccount> bankaccounts = bankaccountRepository.findAll();
        assertThat(bankaccounts).hasSize(databaseSizeBeforeUpdate);
        Bankaccount testBankaccount = bankaccounts.get(bankaccounts.size() - 1);
        assertThat(testBankaccount.getClientname()).isEqualTo(UPDATED_CLIENTNAME);
        assertThat(testBankaccount.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void deleteBankaccount() throws Exception {
        // Initialize the database
        bankaccountRepository.saveAndFlush(bankaccount);
        int databaseSizeBeforeDelete = bankaccountRepository.findAll().size();

        // Get the bankaccount
        restBankaccountMockMvc.perform(delete("/api/bankaccounts/{id}", bankaccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Bankaccount> bankaccounts = bankaccountRepository.findAll();
        assertThat(bankaccounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
