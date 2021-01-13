package com.proximity.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.proximity.model.ItemModel;



@Repository
public interface ItemRepository extends PagingAndSortingRepository<ItemModel, Integer> {


	
}
