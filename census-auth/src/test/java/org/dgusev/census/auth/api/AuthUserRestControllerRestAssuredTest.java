package org.dgusev.census.auth.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

/***/

// todo: https://www.baeldung.com/rest-assured-tutorial

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthUserRestControllerRestAssuredTest {

    @Test
    public void test() {

        get("/census/api/auth/users/1")
                .then()
                    .statusCode(200)
                    .assertThat()
                    .body("id", equalTo(1));

    }

}
