package org.dgusev.census.auth.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/***/

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AuthUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Test
    public void testFindByName() {
//        entityManager.persist(new City("Bratislava", 432000));
//        entityManager.persist(new City("Budapest", 1759000));
//        entityManager.persist(new City("Prague", 1280000));
//        entityManager.persist(new City("Warsaw", 1748000));
//
//        var cities = repository.findByName("Bratislava");
//        assertEquals(1, cities.size());
//
//        assertThat(cities).extracting(City::getName).containsOnly("Bratislava");
    }

}
