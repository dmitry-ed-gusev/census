package org.dgusev.census.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main class for Census Auth application (microservice). */

@SpringBootApplication
public class CensusAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CensusAuthApplication.class, args);
    }

}
