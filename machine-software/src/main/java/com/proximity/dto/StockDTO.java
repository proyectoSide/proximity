package com.proximity.dto;
import javax.validation.constraints.Min;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class StockDTO {

	@NotNull
    @Min(0)
	private Integer ammount;

}
