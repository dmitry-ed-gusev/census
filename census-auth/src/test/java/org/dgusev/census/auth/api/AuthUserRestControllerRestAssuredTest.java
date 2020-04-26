package org.dgusev.census.auth.api;

import io.restassured.http.ContentType;
import org.dgusev.census.auth.domain.entity.AuthUser;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/***/

// todo: https://www.baeldung.com/rest-assured-tutorial

// todo: add verify() statements - to verify calls to mocked repository
// todo: add REST Assured tests for PUT/PATCH/DELETE (by analogy with RestTemplateTest)

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AuthUserRestControllerRestAssuredTest {

    // dates
    private static final String           STR_DATETIME_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat DATETIME_WITH_TIMEZONE     = new SimpleDateFormat(STR_DATETIME_WITH_TIMEZONE);
    private static final Date CURRENT_DATE               = new Date();
    // URLs
    private static final String           AUTH_USERS_URL             = "/census/api/auth/users";
    private static final String           AUTH_USERS_WITH_ID         = AUTH_USERS_URL + "/%s";

    @MockBean
    private AuthUserRepository mockAuthUserRepository;

    private AuthUser authUser = null;

    @Before
    public void beforeEach() {
        authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, CURRENT_DATE, null, null);
    }

    @Test
    public void testFindAllAuthUsers_GET_OK_200() throws Exception {
        // Given
        List<AuthUser> users = Arrays.asList(
                new AuthUser(1L, "Simple User #1", "Description #1",
                        "username1", "password1", true, CURRENT_DATE, null, null),
                new AuthUser(2L, "Simple User #2", "Description #2",
                        "username2", "password2", false, CURRENT_DATE, null, null)
        );
        when(mockAuthUserRepository.findAll()).thenReturn(users);
        // When-Then
        AuthUser[] actualUsers = get(AUTH_USERS_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("size()", is(2))
                .extract()
                .as(AuthUser[].class);
        assertEquals(users.get(0), actualUsers[0]);
        assertEquals(users.get(1), actualUsers[1]);
    }

    @Test
    public void testFindAuthUserById_GET_OK_200() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        // When-Then
        AuthUser actualUser = get(String.format(AUTH_USERS_WITH_ID, 1L))
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .extract()
                .as(AuthUser.class);
        assertEquals(authUser, actualUser);
    }

    @Test
    public void testFindAuthUserById_GET_NotFound_404() throws Exception {
        get(String.format(AUTH_USERS_WITH_ID, 111L))
                .then()
                .statusCode(404);
    }

    @Test
    public void testSaveNewAuthUser_POST_OK_201() throws Exception {
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenReturn(authUser);
        // When-Then
        AuthUser actualUser = given().contentType(ContentType.JSON)
                .body(authUser)
                .when()
                .post(AUTH_USERS_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(AuthUser.class);
        assertEquals(authUser, actualUser);
    }

    @Test
    public void testSaveNewAuthUser_POST_Fail_500() throws Exception {
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenThrow(DataIntegrityViolationException.class);
        // When-Then
        given()
                .contentType(ContentType.JSON)
                .body(authUser)
                .when()
                .post(AUTH_USERS_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(ContentType.JSON)
                .body("message", equalTo("Data Integrity Error!"));
    }

}
