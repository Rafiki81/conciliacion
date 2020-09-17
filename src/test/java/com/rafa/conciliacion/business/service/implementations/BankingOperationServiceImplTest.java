package com.rafa.conciliacion.business.service.implementations;

import com.rafa.conciliacion.business.model.BankingOperation;
import com.rafa.conciliacion.integration.repositories.BankingOperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankingOperationServiceImplTest {

    @Mock
    BankingOperationRepository bankingOperationRepository;

    @InjectMocks
    BankingOperationServiceImpl bankingOperationService;

    @Test
    void createBankingOperations() {
        BankingOperation bankingOperation = new BankingOperation();
        List<BankingOperation> bankingOperationList = new ArrayList<>();
        bankingOperationList.add(bankingOperation);

        when(bankingOperationRepository.saveAll(bankingOperationList)).thenReturn(bankingOperationList);

        List<BankingOperation> savedBankingOperationList = bankingOperationService.createBankingOperations(bankingOperationList);
        verify(bankingOperationRepository).saveAll(bankingOperationList);

        assertThat(bankingOperationList).isNotNull();

    }

    @Test
    void listBankingOperations() {

        BankingOperation bankingOperation = new BankingOperation();
        List<BankingOperation> bankingOperationList = new ArrayList<>();
        bankingOperationList.add(bankingOperation);

        when(bankingOperationRepository.findAll()).thenReturn(bankingOperationList);
        List<BankingOperation> foundBankingOperations = bankingOperationService.listBankingOperations();

        verify(bankingOperationRepository).findAll();

        assertThat(foundBankingOperations).hasSize(1);

    }

    @Test
    void reconciliateBankingOperations() {

        BankingOperation bankingOperationToReconiliate = new BankingOperation();
        List<BankingOperation> bankingOperationListToReconciliate = new ArrayList<>();
        BankingOperation bankingOperation = new BankingOperation();
        List<BankingOperation> bankingOperationList = new ArrayList<>();

        bankingOperationToReconiliate.setDate(Date.from(new Date().toInstant().minus(30, ChronoUnit.MINUTES)));
        bankingOperationToReconiliate.setCustomerId("1");
        bankingOperationToReconiliate.setAmount(30);
        bankingOperationToReconiliate.setReconciliated(false);
        bankingOperationListToReconciliate.add(bankingOperationToReconiliate);

        bankingOperation.setDate(new Date());
        bankingOperation.setCustomerId("1");
        bankingOperation.setAmount(30);
        bankingOperation.setReconciliated(false);
        bankingOperationList.add(bankingOperation);

        Mockito.lenient().when(bankingOperationService.reconciliateBankingOperations(bankingOperationListToReconciliate,0.2,1)).thenReturn(bankingOperationListToReconciliate);

        assertThat(bankingOperationListToReconciliate).hasSize(1);

    }

    @Test
    void getReconciliated() {
        BankingOperation bankingOperation = new BankingOperation();
        bankingOperation.setReconciliated(true);
        List<BankingOperation> bankingOperationList = new ArrayList<>();
        bankingOperationList.add(bankingOperation);

        when(bankingOperationRepository.findAll()).thenReturn(bankingOperationList);
        List<BankingOperation> foundBankingOperations = bankingOperationService.getReconciliated();

        verify(bankingOperationRepository).findAll();

        assertThat(foundBankingOperations).hasSize(1);
    }

    @Test
    void getNonReconciliated() {
        BankingOperation bankingOperation = new BankingOperation();
        bankingOperation.setReconciliated(false);
        List<BankingOperation> bankingOperationList = new ArrayList<>();
        bankingOperationList.add(bankingOperation);

        when(bankingOperationRepository.findAll()).thenReturn(bankingOperationList);
        List<BankingOperation> foundBankingOperations = bankingOperationService.getNonReconciliated();

        verify(bankingOperationRepository).findAll();

        assertThat(foundBankingOperations).hasSize(1);
    }

    @Test
    void MatchBankingOperationsByAmountAndDate(){

        BankingOperation bankingOperationToReconiliate = new BankingOperation();
        List<BankingOperation> bankingOperationListToReconciliate = new ArrayList<>();
        BankingOperation bankingOperation = new BankingOperation();
        List<BankingOperation> bankingOperationList = new ArrayList<>();

        bankingOperationToReconiliate.setDate(Date.from(new Date().toInstant().minus(30, ChronoUnit.MINUTES)));
        bankingOperationToReconiliate.setCustomerId("1");
        bankingOperationToReconiliate.setAmount(30);
        bankingOperationToReconiliate.setReconciliated(false);
        bankingOperationListToReconciliate.add(bankingOperationToReconiliate);

        bankingOperation.setDate(new Date());
        bankingOperation.setCustomerId("1");
        bankingOperation.setAmount(30);
        bankingOperation.setReconciliated(false);
        bankingOperationList.add(bankingOperation);

        Mockito.lenient().when(bankingOperationService.MatchBankingOperationsByAmountAndDate(bankingOperationListToReconciliate,0,1)).thenReturn(bankingOperationListToReconciliate);

        assertThat(bankingOperationListToReconciliate).hasSize(1);
    }
}