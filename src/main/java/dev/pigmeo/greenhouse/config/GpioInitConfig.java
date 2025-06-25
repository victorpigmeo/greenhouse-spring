package dev.pigmeo.greenhouse.config;

import java.time.Instant;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import dev.pigmeo.greenhouse.models.Gpio;
import dev.pigmeo.greenhouse.models.GpioType;
import dev.pigmeo.greenhouse.repositories.GpioRepository;

@Component
public class GpioInitConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final GpioRepository gpioRepository;

    public GpioInitConfig(GpioRepository gpioRepository) {
        this.gpioRepository = gpioRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Optional<Gpio> pin13 = this.gpioRepository.findTopByPinOrderByUsedAtDesc(13L);
        if(pin13.isEmpty()){
            this.gpioRepository.save(new Gpio(null, 13L, GpioType.SWITCH, "0", Instant.now()));
        }

        Optional<Gpio> pin15 = this.gpioRepository.findTopByPinOrderByUsedAtDesc(15L);
        if(pin15.isEmpty()){
            this.gpioRepository.save(new Gpio(null, 15L, GpioType.SWITCH, "0", Instant.now()));
        }

        Optional<Gpio> pin12 = this.gpioRepository.findTopByPinOrderByUsedAtDesc(12L);
        if(pin12.isEmpty()){
            this.gpioRepository.save(new Gpio(null, 12L, GpioType.BUTTON, "STARTED", Instant.now()));
        }
        
    }
}
