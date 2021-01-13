package com.proximity.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.proximity.model.CashMovementModel;



@Repository
public interface CashMovementsRepository extends PagingAndSortingRepository<CashMovementModel, Integer> {

	
}
