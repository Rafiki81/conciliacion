package com.rafa.conciliacion.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafa.conciliacion.business.model.BankingOperation;
import com.rafa.conciliacion.business.service.BankingOperationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankingOperationController.class)
class BankingOperationControllerTest {

    @Autowired
    private BankingOperationController bankingOperationController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankingOperationService bankingOperationService;


    @Test
    void createBankingOperations() throws Exception{

        BankingOperation bankingOperationOne = new BankingOperation();

        bankingOperationOne.setReconciliated(false);
        bankingOperationOne.setAmount(120);
        bankingOperationOne.setCustomerId("1");
        bankingOperationOne.setAccount("0049 6767 18934");
        bankingOperationOne.setDate(new Date());

        List<BankingOperation> bankingOperationControllerListOne = new ArrayList<>();
        bankingOperationControllerListOne.add(bankingOperationOne);

        when(bankingOperationService.createBankingOperations(bankingOperationControllerListOne))
                .thenReturn(bankingOperationControllerListOne);

        mockMvc.perform(MockMvcRequestBuilders.post("/bankingOperations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/bankingOperations")
                .content(new ObjectMapper().writeValueAsString(bankingOperationControllerListOne))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void listBankingOperations()throws Exception {


        BankingOperation bankingOperationOne = new BankingOperation();

        bankingOperationOne.setReconciliated(false);
        bankingOperationOne.setAmount(120);
        bankingOperationOne.setCustomerId("1");
        bankingOperationOne.setAccount("0049 6767 18934");
        bankingOperationOne.setDate(new Date());

        List<BankingOperation> bankingOperationControllerListOne = new ArrayList<>();
        bankingOperationControllerListOne.add(bankingOperationOne);

        BankingOperation bankingOperationTwo = new BankingOperation();

        bankingOperationTwo.setReconciliated(true);
        bankingOperationTwo.setAmount(120);
        bankingOperationTwo.setCustomerId("1");
        bankingOperationTwo.setAccount("0049 6767 18934");
        bankingOperationTwo.setDate(new Date());

        List<BankingOperation> bankingOperationControllerListTwo = new ArrayList<>();
        bankingOperationControllerListOne.add(bankingOperationTwo);

        when(bankingOperationService.getNonReconciliated())
                .thenReturn(bankingOperationControllerListOne);
        when(bankingOperationService.getReconciliated())
                .thenReturn(bankingOperationControllerListTwo);


        mockMvc.perform(MockMvcRequestBuilders.get("/bankingOperations?isReconciliated=false"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/bankingOperations?isReconciliated=true"))
                .andExpect(status().isOk());

    }

    @Test
    void reconciliation() throws Exception {
        BankingOperation bankingOperationOne = new BankingOperation();

        bankingOperationOne.setReconciliated(false);
        bankingOperationOne.setAmount(120);
        bankingOperationOne.setCustomerId("1");
        bankingOperationOne.setAccount("0049 6767 18934");
        bankingOperationOne.setDate(new Date());

        List<BankingOperation> bankingOperationControllerListOne = new ArrayList<>();
        bankingOperationControllerListOne.add(bankingOperationOne);

        when(bankingOperationService.reconciliateBankingOperations(bankingOperationControllerListOne,0.2,1))
                .thenReturn(bankingOperationControllerListOne);

        mockMvc.perform(MockMvcRequestBuilders.post("/bankingOperations/reconciliations?amountRange=0.2&hoursRange=1"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.post("/bankingOperations/reconciliations?amountRange=0.2&hoursRange=1")
                .content(new ObjectMapper().writeValueAsString(bankingOperationControllerListOne))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}