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

/**
 *  This is the Service for BankingOperations Microservice.
 *
 *  Support all the business logic and manages the requests from the controller to our repository.
 *
 *  Has five methods:
 *
 *  createBankingOperations, creates BankingOperations by giving an list of BankingOperations.
 *  listBankingOperations, retrieves form de repository the list of all BankingOperations.
 *  reconciliateBankingOperations, by giving a list of BankingOperations operate the reconciliation and reconciliates this BankingOperations.
 *  getReconciliated, retrieves form de repository the list of the reconciliated BankingOperations.
 *  getNonReconciliated, retrieves form de repository the list of non reconciliated BankingOperations.
 *
 *  @author rperez-beato@viewnext.com
 */
@Service
public class BankingOperationServiceImpl implements BankingOperationService {

    @Autowired
    private BankingOperationRepository bankingOperationRepository;


    /**
     * Creates BankingOperations on the repository by recieving a list of BankingOperations.
     *
     * @param bankingOperations List of BankingOperations
     * @return bankingOperations, a list of BankingOperations that has been saved on the repository.
     */
    @Override
    public List<BankingOperation> createBankingOperations(List<BankingOperation> bankingOperations) {

        bankingOperationRepository.saveAll(bankingOperations);
        return bankingOperations;

    }

    /**
     * Retrieves form de repository the list of all BankingOperations.
     *
     * @return bankingOperations, a list of all the BankingOperations saved on the repository
     */
    @Override
    public List<BankingOperation> listBankingOperations() {

        return bankingOperationRepository.findAll();
    }

    /**
     * By giving a list of BankingOperations operate the reconciliation and reconciliates this BankingOperations.
     * In this method, you can reconciliate a list of BankingOperations.
     *
     * A BankingOperation is reconciliated if has the same customerId, is done with the same amount (+-0.2) or is done at the same
     * time plus or minus an hour.
     *
     *
     * @param bankingOperations
     * @return nonReconciliated List of reconciliated BankingOperations
     */
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


        nonReconciliated.forEach(p -> p.setReconciliated(true));
        bankingOperationRepository.saveAll(nonReconciliated);
        return nonReconciliated;

    }

    /**
     * Retrieves form de repository the list of all the reconciliated BankingOperations.
     *
     * @return A list of the reconciliated BankingOperations saved on the repository.
     */
    @Override
    public List<BankingOperation> getReconciliated() {
        return bankingOperationRepository.findAll().stream()
                .filter(BankingOperation::isReconciliated)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves form de repository the list of all the non reconciliated BankingOperations.
     *
     * @return A list of the non reconciliated BankingOperations saved on the repository.
     */
    @Override
    public List<BankingOperation> getNonReconciliated() {
        return bankingOperationRepository.findAll().stream()
                .filter(p -> !p.isReconciliated())
                .collect(Collectors.toList());
    }
}
