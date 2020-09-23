package com.rafa.conciliacion.presentation.controllers;

import com.rafa.conciliacion.business.model.BankingOperation;
import com.rafa.conciliacion.business.service.BankingOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the Controller for Banking Operations Microservice.
 *
 * Has three methods:
 * createBankingOperations for creating Banking Operations by giving an list of Banking Operations,
 * listBankingOperations retrieves form de repository the list of Banking Operations, can filter for the reconciliated and the nonreconciliated,
 * reconciliation, by giving a list of Banking Operations operate the reconciliation and reconciliates this Banking Operations,
 *
 * @author rperez-beato@viewnext.com
 */
@RestController
@RequestMapping("/bankingOperations")
@CrossOrigin
@Api(value="Banking Operations")
public class BankingOperationController {

    @Autowired
    private BankingOperationService bankingOperationService;

    /**
     * Method that creates banking operations by passing a list of banking operations.
     *
     * @param bankingOperations List of Banking Operations
     * @return Returns the list of Banking Operations saved on the repository with the generated id.
     */
    @PostMapping
    @ApiOperation(value = "Creates Banking Operations from a List of Banking Operations")
    @ApiResponse(code = 200, message = "The new resources has been created")
    public ResponseEntity<List<BankingOperation>> createBankingOperations(
            @RequestBody List<BankingOperation> bankingOperations){
        return new ResponseEntity<>(bankingOperationService.createBankingOperations(bankingOperations), HttpStatus.CREATED);
    }

    /**
     * Method that lists banking operations, can filter the reconciliated or nonreconciliated ones.
     *
     * @param isReconciliated Not required. QueryParam for filtering the reconciliated or nonreconciliated operations
     * @return Returns the list of Banking Operations saved on the repository
     */
    @GetMapping
    @ApiOperation(value = "Retrieves Banking Operations from the repository")
    @ApiResponse(code = 200, message = "The Query has been resolved OK")
    public ResponseEntity<List<BankingOperation>> listBankingOperations(
            @RequestParam(value = "isReconciliated", required = false) Boolean isReconciliated){

        if(isReconciliated == null){
            return new ResponseEntity<>(bankingOperationService.listBankingOperations(), HttpStatus.OK);
        }
        if(Boolean.TRUE.equals(isReconciliated)){
            return new ResponseEntity<>(bankingOperationService.getReconciliated(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(bankingOperationService.getNonReconciliated(), HttpStatus.OK);
        }

    }

    /**
     * Method that reconciliates a list of Banking Operations.
     *
     * @param bankingOperations List of banking operations to be reconciliated
     * @param amountRange Amount Range in which the BankingOperations are reconciliated.
     * @param hoursRange Hours Range in which the BankingOperations are reconciliated.
     * @return Returns the list of Banking Operations that has been reconciliated
     */
    @PostMapping("/reconciliations")
    @ApiOperation(value = "Do the Reconciliate operation to a List of Banking Operations")
    @ApiResponse(code = 200, message = "The Operation has been resolved OK")
    public ResponseEntity<List<BankingOperation>> reconciliation(
            @RequestBody List<BankingOperation> bankingOperations,
            @RequestParam (value = "amountRange" , required = false) Double amountRange,
            @RequestParam (value = "hoursRange" , required = false) Integer hoursRange){

        if(amountRange == null){
            amountRange = 0.2;
        }

        if(hoursRange == null){
            hoursRange = 1;
        }

        return new ResponseEntity<>(bankingOperationService.reconciliateBankingOperations(bankingOperations,amountRange,hoursRange), HttpStatus.OK);

    }


}
