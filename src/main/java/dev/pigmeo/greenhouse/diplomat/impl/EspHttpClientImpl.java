package dev.pigmeo.greenhouse.diplomat.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import dev.pigmeo.greenhouse.config.EspServerConfig;
import dev.pigmeo.greenhouse.diplomat.EspHttpClient;
import dev.pigmeo.greenhouse.dto.EspGpioResponse;
import dev.pigmeo.greenhouse.exception.EspServerException;
import dev.pigmeo.greenhouse.models.Dht;
import dev.pigmeo.greenhouse.models.Gpio;
import dev.pigmeo.greenhouse.models.GpioType;

@Component
public class EspHttpClientImpl implements EspHttpClient {

    WebClient webClient;
    EspServerConfig espServerConfig;

    public EspHttpClientImpl(WebClient webClient, EspServerConfig espServerConfig) {
        this.webClient = webClient;
        this.espServerConfig = espServerConfig;
    }

    @Override
    public Dht getDht(){
        return webClient.get()
                .uri(espServerConfig.getEndpointByKey("dht"))
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(Dht.class);
                    } else {
                        System.out.println(response.bodyToMono(String.class));
                        throw new EspServerException("Error reaching the ESP", response.statusCode());
                    }
                }).block();
    }

    @Override
    public Gpio useGpio(Gpio gpio){
        //TODO Refactor ESP code to remove action
        String action = gpio.getType().equals(GpioType.BUTTON) ? "button" : "set";
        Long value = gpio.getValue().matches("-?\\d+(\\.\\d+)?") ? Long.parseLong(gpio.getValue()) : 0L;
        String uri = String.format(espServerConfig.getEndpointByKey("gpio"), action, gpio.getPin(), value);

        webClient.get()
            .uri(uri)
            .exchangeToMono(response -> {
                if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(EspGpioResponse.class);
                    } else {
                        throw new EspServerException("Error reaching the ESP", response.statusCode());
                    }
            }).block();

        return gpio;
    }

}
