package com.rafa.conciliacion.business.model;

import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
public class BankingOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date;
    private String customerId;
    private String account;
    private double amount;
    private boolean isReconciliated;

}
