package com.proximity.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proximity.dto.MachineDTO;
import com.proximity.dto.MachineReportDTO;

public interface IMachinesService {

	Page<MachineDTO> getMachines(Pageable pageable);

	MachineDTO getMachine(Integer id);

	MachineDTO createMachine(@Valid MachineDTO req);

	MachineDTO updateMachine(Integer id, @Valid MachineDTO req);

	void deleteMachine(Integer id);

	Page<MachineReportDTO> getMachinesReport(Pageable pageable);

	void unlockMachine(Integer id);

}
