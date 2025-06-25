package dev.pigmeo.greenhouse.diplomat;

import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.models.Gpio;

public interface EspHttpClient {
    Dht getDht();
    Gpio useGpio(Gpio gpio);    
}
