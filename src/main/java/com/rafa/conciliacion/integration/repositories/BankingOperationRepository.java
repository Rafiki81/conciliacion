package com.rafa.conciliacion.integration.repositories;

import com.rafa.conciliacion.business.model.BankingOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingOperationRepository extends JpaRepository<BankingOperation, Long> {
}
