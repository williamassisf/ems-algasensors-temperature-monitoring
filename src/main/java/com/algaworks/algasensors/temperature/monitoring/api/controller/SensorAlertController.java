package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

	private final SensorAlertRepository repository;

	@GetMapping
	public SensorAlertOutput getSensorAlert(@PathVariable TSID sensorId) {
		var sensorAlert = repository.findById(new SensorId(sensorId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return convertToModel(sensorAlert);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public SensorAlertOutput updateSensorAlert(@RequestBody SensorAlertInput input, @PathVariable TSID sensorId) {
		if (sensorId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		var sensorAlertOpt = repository.findById(new SensorId(sensorId));

		if (sensorAlertOpt.isPresent()) {
			var sensorAlert = sensorAlertOpt.get();
			sensorAlert.setMaxTemperature(input.getMaxTemperature());
			sensorAlert.setMinTemperature(input.getMinTemperature());
			repository.save(sensorAlert);
			return convertToModel(sensorAlert);
		}

		var sensorAlert = repository.save(
				SensorAlert.builder().id(new SensorId(sensorId)).maxTemperature(input.getMaxTemperature()).minTemperature(input.getMinTemperature()).build());
		return convertToModel(sensorAlert);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSensorAlert(@PathVariable TSID sensorId) {
		if (sensorId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		var sensorAlert = repository.findById(new SensorId(sensorId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		repository.delete(sensorAlert);
	}

	private SensorAlertOutput convertToModel(SensorAlert sensorAlert) {
		return SensorAlertOutput.builder()
								.id(sensorAlert.getId().getValue())
								.maxTemperature(sensorAlert.getMaxTemperature())
								.minTemperature(sensorAlert.getMinTemperature())
								.build();
	}
}
