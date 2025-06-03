package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures")
@RequiredArgsConstructor
public class TemperatureLogController {

	private final TemperatureLogRepository repository;

	@GetMapping
	public Page<TemperatureLogOutput> search(@PathVariable String sensorId, @PageableDefault Pageable pageable) {
		var temperatureLogs = repository.findAllBySensorId(new SensorId(sensorId), pageable);

		return temperatureLogs.map(temperatureLog -> TemperatureLogOutput.builder()
																		 .id(temperatureLog.getId().getValue())
																		 .value(temperatureLog.getValue())
																		 .registeredAt(temperatureLog.getRegisteredAt())
																		 .sensorId(temperatureLog.getSensorId().getValue())
																		 .build());
	}
}
