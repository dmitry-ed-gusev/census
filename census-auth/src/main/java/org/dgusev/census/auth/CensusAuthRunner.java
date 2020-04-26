package org.dgusev.census.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/***/

@Slf4j
@Component
public class CensusAuthRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        LOG.debug("CMD Line Runner is working...");
    }

}
