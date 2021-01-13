package com.proximity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.proximity.dto.CashAvailableResponseDTO;
import com.proximity.dto.MachineDTO;
import com.proximity.dto.MachineReportDTO;
import com.proximity.model.MachineModel;
import com.proximity.model.repository.MachineRepository;
import com.proximity.service.IMachinesService;

@Service
public class MachinesServiceImpl implements IMachinesService {

	private final static Logger LOGGER = Logger.getLogger(MachinesServiceImpl.class.getName());

	@Autowired
	private MachineRepository machineRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Page<MachineReportDTO> getMachinesReport(Pageable pageable) {

		Page<MachineModel> machinesPage = machineRepository.findAll(pageable);

		List<MachineReportDTO> machineList = new ArrayList<MachineReportDTO>();

		machinesPage.getContent().parallelStream().forEach(m -> {

			MachineReportDTO report = modelMapper.map(m, MachineReportDTO.class);
			report.setActive(false);

			try {
				ResponseEntity<CashAvailableResponseDTO> response = restTemplate
						.getForEntity(m.getHost() + "/machine/cash/profit", CashAvailableResponseDTO.class);

				report.setActive(response.getStatusCode().equals(HttpStatus.OK));
				if (report.isActive()) {
					report.setTotal(response.getBody().getAmmount());
				}
			} catch (ResourceAccessException e) {
				// Exception in comunication con the machine
			}
			machineList.add(report);

		});

		Page<MachineReportDTO> response = new PageImpl<MachineReportDTO>(machineList, pageable,
				machinesPage.getTotalElements());
		return response;

	}

	@Override
	public Page<MachineDTO> getMachines(Pageable pageable) {
		Page<MachineModel> machinesPage = machineRepository.findAll(pageable);

		List<MachineDTO> aux = new ArrayList<MachineDTO>();
		for (MachineModel prodModel : machinesPage.getContent()) {
			aux.add(modelMapper.map(prodModel, MachineDTO.class));
		}
		Page<MachineDTO> response = new PageImpl<MachineDTO>(aux, pageable, machinesPage.getTotalElements());
		return response;

	}

	@Override
	public MachineDTO getMachine(Integer id) {
		Optional<MachineModel> machinesPage = machineRepository.findById(id);

		if (machinesPage.isEmpty()) {
			return null;
		}

		return modelMapper.map(machinesPage.get(), MachineDTO.class);
	}

	@Override
	public MachineDTO createMachine(@Valid MachineDTO req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MachineDTO updateMachine(Integer id, @Valid MachineDTO req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMachine(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlockMachine(Integer id) {

		Optional<MachineModel> machine = machineRepository.findById(id);

		if (machine.isEmpty())
			throw new RuntimeException("La maquina Id: " + id + " es invalida.");

		MachineModel m = machine.get();

		ResponseEntity<Void> r2 = restTemplate.exchange(m.getHost() + "/machine/unlock?pass=" + machine.get().getPassword(), HttpMethod.PUT,
				null, Void.class);


	}

}
