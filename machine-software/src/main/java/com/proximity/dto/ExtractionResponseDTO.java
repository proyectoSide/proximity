package com.proximity.dto;
import java.util.List;

import com.proximity.enums.PaymentUnits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtractionResponseDTO {

	private List<PaymentUnits> money;

}
