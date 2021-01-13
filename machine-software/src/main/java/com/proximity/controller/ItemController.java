package com.proximity.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proximity.dto.ItemDTO;
import com.proximity.service.IItemService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/item")
public class ItemController {

	protected final static int DEFAULT_PAGE_SIZE = 8;

	@Autowired
	private IItemService itemService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	@ResponseBody
	public ItemDTO createItem(@Valid @RequestBody ItemDTO req) {
		return itemService.createItem(req);
	}

	@PutMapping("/{id}")
	@ResponseBody
	public ItemDTO updateItem(@PathVariable Integer id, @Valid @RequestBody ItemDTO req) {
		ItemDTO response = itemService.updateItem(id, req);
		return response;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ItemDTO getItem(@PathVariable Integer id) {
		ItemDTO response = itemService.getItem(id);
		return response;
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	public void deleteProduct(@PathVariable Integer id) {
		itemService.deleteItem(id);
	}

	@GetMapping
	public Page<ItemDTO> getItem(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		Page<ItemDTO> response = itemService.getAllItem(pageable);
		return response;
	}

}
