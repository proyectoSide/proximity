package com.proximity.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proximity.dto.ItemDTO;

public interface IItemService {

	/**
	 * Create a item and return its DTO.
	 * 
	 * @param req
	 * @return
	 */
	ItemDTO createItem(ItemDTO req);

	/**
	 * Returns all items paginated
	 * 
	 * @param pageable
	 * @return
	 */
	Page<ItemDTO> getAllItem(Pageable pageable);

	/**
	 * Return the DTO of a specific item
	 * 
	 * @param id
	 * @return
	 */
	ItemDTO getItem(Integer id);

	/**
	 * Remove a item.
	 * 
	 * @param id
	 */
	void deleteItem(Integer id);

	/**
	 * Update item and return its DTO.
	 * 
	 * @param id
	 * @param req
	 * @return
	 */
	ItemDTO updateItem(Integer id, @Valid ItemDTO req);

}
