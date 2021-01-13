package com.proximity.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.proximity.model.MachineModel;



@Repository
public interface MachineRepository extends PagingAndSortingRepository<MachineModel, Integer> {
   
	

	
}
