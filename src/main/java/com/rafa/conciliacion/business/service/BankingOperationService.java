package com.rafa.conciliacion.business.service;

import com.rafa.conciliacion.business.model.BankingOperation;

import java.util.List;

public interface BankingOperationService {

    public List<BankingOperation> createBankingOperations(List<BankingOperation> bankingOperations);

    public List<BankingOperation> listBankingOperations();

    public List<BankingOperation> reconciliateBankingOperations(List<BankingOperation> bankingOperations);

    public List<BankingOperation> getReconciliated();

    public List<BankingOperation> getNonReconciliated();

}
