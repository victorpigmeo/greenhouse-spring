package dev.pigmeo.greenhouse.services;

import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.models.Gpio;

public interface EspService {
    public Dht getDht();
    public Gpio setGpio(Long pin);
}
