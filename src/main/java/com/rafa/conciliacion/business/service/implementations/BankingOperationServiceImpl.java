package com.rafa.conciliacion.business.service.implementations;

import com.rafa.conciliacion.business.model.BankingOperation;
import com.rafa.conciliacion.business.service.BankingOperationService;
import com.rafa.conciliacion.integration.repositories.BankingOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankingOperationServiceImpl implements BankingOperationService {

    @Autowired
    private BankingOperationRepository bankingOperationRepository;

    @Override
    public List<BankingOperation> createBankingOperations(List<BankingOperation> bankingOperations) {

        bankingOperationRepository.saveAll(bankingOperations);
        return bankingOperations;

    }

    @Override
    public List<BankingOperation> listBankingOperations() {
        return bankingOperationRepository.findAll();
    }

    @Override
    public List<BankingOperation> reconciliateBankingOperations(List<BankingOperation> bankingOperations) {

        List<BankingOperation> nonReconciliated = bankingOperationRepository.findAll().stream()
                .filter(bankingOperation -> !bankingOperation.isReconciliated())
                .filter(bankingOperation -> bankingOperations.stream()
                        .anyMatch(bankingOperationRecieved ->
                                (bankingOperationRecieved.getCustomerId().equals(bankingOperation.getCustomerId())) &&
                                        ((bankingOperationRecieved.getAmount() >= bankingOperation.getAmount() - 0.2 &&
                                                bankingOperationRecieved.getAmount() <= bankingOperation.getAmount() + 0.2)||
                                                (bankingOperationRecieved.getDate().before(Date.from(bankingOperation.getDate().toInstant().plus(1, ChronoUnit.HOURS))) &&
                                                bankingOperationRecieved.getDate().after(Date.from(bankingOperation.getDate().toInstant().minus(1, ChronoUnit.HOURS)))
                        ))))
                .collect(Collectors.toList());


        nonReconciliated.stream().forEach(p -> p.setReconciliated(true));
        bankingOperationRepository.saveAll(nonReconciliated);
        return nonReconciliated;

    }

    @Override
    public List<BankingOperation> getReconciliated() {
        return bankingOperationRepository.findAll().stream()
                .filter(p -> p.isReconciliated())
                .collect(Collectors.toList());
    }

    @Override
    public List<BankingOperation> getNonReconciliated() {
        return bankingOperationRepository.findAll().stream()
                .filter(p -> !p.isReconciliated())
                .collect(Collectors.toList());
    }
}
