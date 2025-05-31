package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

	private final SensorMonitoringRepository repository;

	@GetMapping
	public SensorMonitoringOutput getDatail(@PathVariable TSID sensorId) {
		var sensorMonitoring = findByIdOrDefault(sensorId);

		return SensorMonitoringOutput.builder()
									 .id(sensorMonitoring.getId().getValue())
									 .enabled(sensorMonitoring.getEnabled())
									 .lastTemperature(sensorMonitoring.getLastTemperature())
									 .updateAt(sensorMonitoring.getUpdateAt())
									 .build();
	}

	@PutMapping("/enable")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void enable(@PathVariable TSID sensorId) {
		var sensorMonitoring = findByIdOrDefault(sensorId);
		sensorMonitoring.setEnabled(true);
		repository.saveAndFlush(sensorMonitoring);
	}

	@DeleteMapping("/enable")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void disable(@PathVariable TSID sensorId) {
		var sensorMonitoring = findByIdOrDefault(sensorId);
		sensorMonitoring.setEnabled(false);
		repository.saveAndFlush(sensorMonitoring);
	}

	private SensorMonitoring findByIdOrDefault(TSID sensorId) {
		return repository.findById(new SensorId(sensorId))
						 .orElse(SensorMonitoring.builder().id(new SensorId(sensorId)).enabled(false).lastTemperature(null).updateAt(null).build());
	}
}
