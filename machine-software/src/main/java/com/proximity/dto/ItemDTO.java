package com.proximity.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class ItemDTO {
	private Integer id;

	@NotNull
	@Min(0)
	private Float price;

	@NotNull
	@Size(min = 0, max = 250)
	private String name;

	@NotNull
	@Size(min = 1, max = 250)
	private String code;

	@NotNull
	@Min(0)
	private Integer stock;

	
}
