package com.proximity.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.proximity.model.ChangeModel;



@Repository
public interface ChangeRepository extends PagingAndSortingRepository<ChangeModel, String> {

	
}
