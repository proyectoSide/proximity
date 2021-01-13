package com.proximity.model.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proximity.model.TransactionModel;



@Repository
public interface TransactionRepository extends PagingAndSortingRepository<TransactionModel, Integer> {
   
	
	@Query("select a from TransactionModel a where a.sellDate <= :sellDate")
    List<TransactionModel> findAllWithCreationDateTimeBefore(
      @Param("sellDate") Date sellDate);
	
}
