package com.proximity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proximity.dto.ItemDTO;
import com.proximity.exception.InvalidItemException;
import com.proximity.model.ItemModel;
import com.proximity.model.repository.ItemRepository;
import com.proximity.service.IItemService;

@Service
public class ItemServiceImpl implements IItemService {

	private final static Logger LOGGER = Logger.getLogger(ItemServiceImpl.class.getName());

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ItemDTO createItem(ItemDTO req) {
		ItemModel newItem = modelMapper.map(req, ItemModel.class);
		ItemModel saveResult = itemRepository.save(newItem);
		ItemDTO response = modelMapper.map(saveResult, ItemDTO.class);
		return response;
	}

	@Override
	public ItemDTO updateItem(Integer id, @Valid ItemDTO req) {

		Optional<ItemModel> optionalItem = itemRepository.findById(id);
		if (!optionalItem.isPresent()) {
			LOGGER.log(Level.SEVERE, "Invalid Item ID:" + id);
			throw new InvalidItemException();
		}

		ItemModel prod = optionalItem.get();
		prod.setName(req.getName());
		prod.setCode(req.getCode());

		ItemModel saveResult = itemRepository.save(prod);
		return modelMapper.map(saveResult, ItemDTO.class);

	}

	@Override
	public Page<ItemDTO> getAllItem(Pageable pageable) {
		Page<ItemModel> reportList = itemRepository.findAll(pageable);
		List<ItemDTO> aux = new ArrayList<ItemDTO>();
		for (ItemModel prodModel : reportList.getContent()) {
			aux.add(modelMapper.map(prodModel, ItemDTO.class));
		}
		Page<ItemDTO> response = new PageImpl<ItemDTO>(aux, pageable, reportList.getTotalElements());
		return response;
	}

	@Override
	public ItemDTO getItem(Integer id) {
		Optional<ItemModel> optionalItem = itemRepository.findById(id);
		if (!optionalItem.isPresent()) {
			LOGGER.log(Level.SEVERE, "Invalid Product ID:" + id);
			throw new InvalidItemException();
		}
		return modelMapper.map(optionalItem.get(), ItemDTO.class);
	}

	@Override
	public void deleteItem(Integer id) {
		itemRepository.deleteById(id);
	}

}
