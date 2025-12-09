package dev.pigmeo.greenhouse.services;

import java.util.List;

import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.models.Gpio;

public interface EspService {
    public Dht getDht();

    public List<Gpio> getAllGpio();

    public Gpio newGpio(Gpio newGpio);

    public Gpio setGpio(Long pin);
}
