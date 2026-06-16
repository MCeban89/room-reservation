package org.example.roomreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class  RoomreservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomreservationApplication.class, args);
    }

}
