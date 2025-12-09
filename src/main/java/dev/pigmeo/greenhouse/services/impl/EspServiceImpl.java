package dev.pigmeo.greenhouse.services.impl;

import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.pigmeo.greenhouse.diplomat.EspHttpClient;
import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.models.Gpio;
import dev.pigmeo.greenhouse.repositories.GpioRepository;
import dev.pigmeo.greenhouse.services.EspService;

@Service
public class EspServiceImpl implements EspService {

    private EspHttpClient espHttpClient;
    private GpioRepository gpioRepository;

    public EspServiceImpl(EspHttpClient espHttpClient, GpioRepository gpioRepository) {
        this.espHttpClient = espHttpClient;
        this.gpioRepository = gpioRepository;
    }

    @Override
    public Dht getDht() {
        return this.espHttpClient.getDht();
    }

    public List<Gpio> getAllGpio(){
        return this.gpioRepository.findAll();
    }

    public Gpio newGpio(Gpio newGpio){
        return this.gpioRepository.save(newGpio);
    }

    public Gpio setGpio(Long pin){
        //TODO Implement GPIO CRUD
        Optional<Gpio> gpio = this.gpioRepository.findTopByPinOrderByUsedAtDesc(pin);

        if(gpio.isPresent()){
            Gpio newValue = gpio.get().use();
            
            this.espHttpClient.useGpio(newValue);
            this.gpioRepository.save(newValue);

            return newValue;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    
}
