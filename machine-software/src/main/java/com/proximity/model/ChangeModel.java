package com.proximity.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "change")
public class ChangeModel {

	@Id
	private String code;
	private Integer quantity;


}
