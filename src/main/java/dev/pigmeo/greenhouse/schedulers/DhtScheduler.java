package dev.pigmeo.greenhouse.schedulers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.repositories.DhtRepository;
import dev.pigmeo.greenhouse.services.EspService;

@Component
public class DhtScheduler {

    EspService espService;
    DhtRepository dhtRepository;

    DhtScheduler(EspService espService, DhtRepository dhtRepository) {
        this.espService = espService;
        this.dhtRepository = dhtRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(DhtScheduler.class);

    @Scheduled(fixedRate = 60000)
    public void reportDhtInfo() {
        Dht dht = espService.getDht();
        dht.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC));

        dhtRepository.save(dht);
        log.info("Saved DHT info T: {}, H: {}, HI: {} at {}",
                dht.getTemperature(),
                dht.getHumidity(),
                dht.getHeatIndex(),
                LocalDateTime.ofInstant(dht.getTimestamp(), ZoneOffset.UTC));
    }
}
