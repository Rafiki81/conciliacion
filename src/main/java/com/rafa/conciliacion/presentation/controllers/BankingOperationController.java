package com.rafa.conciliacion.presentation.controllers;

import com.rafa.conciliacion.business.model.BankingOperation;
import com.rafa.conciliacion.business.service.BankingOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankingOperations")
public class BankingOperationController {

    @Autowired
    private BankingOperationService bankingOperationService;

    @PostMapping
    public ResponseEntity<List<BankingOperation>> createBankingOperations(
            @RequestBody List<BankingOperation> bankingOperations){
        return new ResponseEntity<>(bankingOperationService.createBankingOperations(bankingOperations), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BankingOperation>> listBankingOperations(@RequestParam(name = "isReconciliated", required = false) boolean isReconciliated){

        if(isReconciliated){
            return new ResponseEntity<>(bankingOperationService.getReconciliated(), HttpStatus.OK);
        }else if(!isReconciliated){
            return new ResponseEntity<>(bankingOperationService.getNonReconciliated(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(bankingOperationService.listBankingOperations(), HttpStatus.OK);
        }

    }

    @PostMapping("/reconciliations")
    public ResponseEntity<List<BankingOperation>> reconciliation(
            @RequestBody List<BankingOperation> bankingOperations){
        return new ResponseEntity<>(bankingOperationService.reconciliateBankingOperations(bankingOperations), HttpStatus.OK);
    }


}
