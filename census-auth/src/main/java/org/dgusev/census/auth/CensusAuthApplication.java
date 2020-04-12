package org.dgusev.census.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;

import static org.dgusev.census.auth.CensusAuthDefaults.PID_FILE_NAME;
import static org.dgusev.census.auth.CensusAuthDefaults.PORT_FILE_NAME;

/** Main class for Census Auth application (microservice). */

@SpringBootApplication
public class CensusAuthApplication {

    public static void main(String[] args) {

        // SpringApplication.run(CensusAuthApplication.class, args);

        // create Spring Application
        SpringApplication application = new SpringApplication(CensusAuthApplication.class);
        // PID to file writer (deleted on shutdown)
        application.addListeners(new ApplicationPidFileWriter(PID_FILE_NAME));
        // web port to file writer (deleted on shutdown)
        application.addListeners(new WebServerPortFileWriter(PORT_FILE_NAME));
        // run application
        application.run(args);

    }

}
