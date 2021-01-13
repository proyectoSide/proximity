package com.proximity.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proximity.dto.MachineDTO;
import com.proximity.dto.MachineReportDTO;
import com.proximity.service.IMachinesService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/machines")
public class MachinesController {

	protected final static int DEFAULT_PAGE_SIZE = 8;
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		return handle(e);
	}

	@Autowired
	private IMachinesService machinesService;

	@GetMapping
	@ResponseBody
	public Page<MachineDTO> getMachines(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable ) {
		Page<MachineDTO> response = machinesService.getMachines(pageable);
		return response;
	}
	
	@GetMapping("/report")
	@ResponseBody
	public Page<MachineReportDTO> getMachinesReport(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable ) {
		Page<MachineReportDTO> response = machinesService.getMachinesReport(pageable);
		return response;
	}

	@PutMapping("/{id}/unlock/")
	@ResponseBody
	public void unlockMachine(@PathVariable Integer id ) {
		machinesService.unlockMachine(id);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public MachineDTO getMachine(@PathVariable Integer id) {
		MachineDTO response = machinesService.getMachine(id);
		return response;
	}
	
	/*
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	@ResponseBody
	public MachineDTO createMachine(@Valid @RequestBody MachineDTO req) {
		return machinesService.createMachine(req);
	}

	@PutMapping("/{id}")
	@ResponseBody
	public MachineDTO updateMachine(@PathVariable Integer id, @Valid @RequestBody MachineDTO req) {
		MachineDTO response = machinesService.updateMachine(id, req);
		return response;
	}

	
	@DeleteMapping("/{id}")
	@ResponseBody
	public void deleteMachine(@PathVariable Integer id) {
		machinesService.deleteMachine(id);
	}
	*/
	
	public static ResponseEntity<String> handle(Exception e) {
		String dummy = "{ message : " + e.getMessage() + "  }";
		e.printStackTrace();
		return new ResponseEntity<String>(dummy, HttpStatus.BAD_REQUEST);
	}

}
