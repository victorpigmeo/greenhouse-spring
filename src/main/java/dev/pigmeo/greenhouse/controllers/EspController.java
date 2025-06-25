package dev.pigmeo.greenhouse.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pigmeo.greenhouse.dto.DhtResponse;
import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.models.Gpio;
import dev.pigmeo.greenhouse.repositories.DhtRepository;
import dev.pigmeo.greenhouse.services.EspService;


@RestController
@RequestMapping("/api")
public class EspController {

    private EspService espService;
    private DhtRepository dhtRepository;

    public EspController(EspService espService, DhtRepository dhtRepository) {
        this.espService = espService;
        this.dhtRepository = dhtRepository;
    }

    @GetMapping("/dht")
    public List<DhtResponse> getDhtReadsByTimestamp(@RequestParam("from") Long from, @RequestParam("to") Long to) {
        List<Dht> dhtReads = dhtRepository.findByTimestampBetween(
                Instant.ofEpochMilli(from),
                Instant.ofEpochMilli(to));

        return dhtReads.stream().map((dhtRead) -> {
            return new DhtResponse(
                    dhtRead.getId(),
                    dhtRead.getTemperature(),
                    dhtRead.getHumidity(),
                    dhtRead.getHeatIndex(),
                    LocalDateTime.ofInstant(dhtRead.getTimestamp(), ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }).toList();
    }

    @GetMapping("/dht/now")
    public DhtResponse getTemperature() {
        Dht dhtRead = espService.getDht();
        return new DhtResponse(
                    dhtRead.getId(),
                    dhtRead.getTemperature(),
                    dhtRead.getHumidity(),
                    dhtRead.getHeatIndex(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @PutMapping("/gpio/{pin}")
    public Gpio setGpio(@PathVariable Long pin) {
        return this.espService.setGpio(pin);
    }

}
