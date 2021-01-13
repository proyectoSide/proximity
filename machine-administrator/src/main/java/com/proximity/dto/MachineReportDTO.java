package com.proximity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineReportDTO {

	private String code;
	private boolean isActive;
	private Integer total;
	private boolean readyToExtraction;
	
}
