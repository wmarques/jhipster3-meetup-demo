package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bankaccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bankaccount entity.
 */
public interface BankaccountRepository extends JpaRepository<Bankaccount,Long> {

}
