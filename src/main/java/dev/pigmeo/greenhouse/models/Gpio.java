package dev.pigmeo.greenhouse.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "Gpio")
public class Gpio {
    @Id
    private String id;
    private Long pin;
    private GpioType type;
    private String value;
    private Instant usedAt;

    public Gpio use() {
        if (this.type == GpioType.BUTTON) {
            return new Gpio(null, this.pin, this.type, "PRESSED", Instant.now());
        } else {
            return new Gpio(null,
                    this.pin,
                    this.type,
                    this.value.equals("0") ? "1" : "0",
                    Instant.now());
        }
    }
}
