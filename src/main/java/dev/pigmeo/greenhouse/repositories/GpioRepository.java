package dev.pigmeo.greenhouse.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.pigmeo.greenhouse.models.Gpio;

public interface GpioRepository extends MongoRepository<Gpio, String>{
    Optional<Gpio> findTopByPinOrderByUsedAtDesc(Long pin);
}
