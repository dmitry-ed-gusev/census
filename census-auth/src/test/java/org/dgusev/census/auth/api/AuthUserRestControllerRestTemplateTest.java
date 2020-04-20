package org.dgusev.census.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dgusev.census.auth.domain.entity.AuthUser;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/***/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class AuthUserRestControllerRestTemplateTest {

    // dates
    private static final String           STR_DATETIME_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat DATETIME_WITH_TIMEZONE     = new SimpleDateFormat(STR_DATETIME_WITH_TIMEZONE);
    private static final Date             CURRENT_DATE               = new Date();
    private static final String           CURRENT_STR_DATE           = DATETIME_WITH_TIMEZONE.format(CURRENT_DATE);
    // URLs
    private static final String           AUTH_USERS_URL             = "/census/api/auth/users";
    private static final String           AUTH_USERS_WITH_ID         = AUTH_USERS_URL + "/%s";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // static initialization block for datetime with timezone + some other initializations
    static {
        // setup timezone
        DATETIME_WITH_TIMEZONE.setTimeZone(TimeZone.getTimeZone("GMT"));
        // setup proper date mapping for Jackson ObjectMapper
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.setDateFormat(DATETIME_WITH_TIMEZONE);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private AuthUserRepository mockAuthUserRepository;

    private AuthUser authUser = null;

    @Before
    public void beforeEach() {
        authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, CURRENT_DATE, null, null);
    }

    @BeforeClass
    public static void beforeAll() {}

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
        String expected = MAPPER.writeValueAsString(users);
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(AUTH_USERS_URL, String.class);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockAuthUserRepository, times(1)).findAll();
    }

    @Test
    public void testFindAuthUserById_GET_OK_200() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        String expected = MAPPER.writeValueAsString(authUser);
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(AUTH_USERS_WITH_ID, 1L), String.class);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockAuthUserRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAuthUserById_GET_NotFound_404() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(AUTH_USERS_WITH_ID, 111L), String.class);
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(mockAuthUserRepository, times(1)).findById(111L);
    }

    @Test
    public void testSaveNewAuthUser_POST_OK_201() throws Exception {
        // Given
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenReturn(authUser);
        String expected = MAPPER.writeValueAsString(authUser);
        // When
        ResponseEntity<String> response = restTemplate.postForEntity(AUTH_USERS_URL, authUser, String.class);
        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockAuthUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testSaveNewAuthUser_POST_Fail_500() throws Exception {
        // Given
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenThrow(DataIntegrityViolationException.class);
        // When
        ResponseEntity<String> response = restTemplate.postForEntity(AUTH_USERS_URL, authUser, String.class);
        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertTrue(response.getBody() != null && response.getBody().contains("Data Integrity Error!"));
        verify(mockAuthUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testUpdateExistingUser_PUT_OK_200() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser)); // found user
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenReturn(authUser); // call to save
        String expected = MAPPER.writeValueAsString(authUser);
        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(expected, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format(AUTH_USERS_WITH_ID, 1L),
                HttpMethod.PUT, entity, String.class);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockAuthUserRepository, times(1)).findById(1L);
        verify(mockAuthUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testUpdateNonExistingUser_PUT_FAIL_404() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.empty()); // user not found
        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(MAPPER.writeValueAsString(authUser), headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format(AUTH_USERS_WITH_ID, 1L),
                HttpMethod.PUT, entity, String.class);
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(mockAuthUserRepository, times(1)).findById(1L);
        verify(mockAuthUserRepository, times(0)).save(any(AuthUser.class));
    }

    @Test
    @Ignore (value = "Standard JDK HTTP doesn't support PATCH HTTP method!")
    // todo: https://stackoverflow.com/questions/29447382/resttemplate-patch-request
    public void testUpdateExistingUser_PATCH_OK_200() throws Exception {
        // Given
        Map<String, String> update = new HashMap<String, String>() {{
            put("name",        "New Name");
            put("description", "New Description");
            put("username",    "new_username");
            put("password",    "new_password");
            put("active",      "true");
        }};
        // updated user, that should be passed to save() method of the repository (save to DB)
        AuthUser updatedAuthUser = new AuthUser(1L, update.get("name"), update.get("description"),
                update.get("username"), update.get("password"), "true".equals(update.get("active")), CURRENT_DATE, null, null);
        // setup mock for findById() call - return existing user
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        String updateData = MAPPER.writeValueAsString(update);

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(updateData, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format(AUTH_USERS_WITH_ID, 1L),
                HttpMethod.PATCH, entity, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        verify(mockAuthUserRepository, times(1)).findById(1L);
        // invoke save() with updated user
        verify(mockAuthUserRepository, times(1)).save(eq(updatedAuthUser));
    }

    @Test
    public void testDeleteAuthUser_Existing_User_DELETE_OK_204() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        doNothing().when(mockAuthUserRepository).deleteById(1L);
        // When
        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(String.format(AUTH_USERS_WITH_ID, 1L),
                HttpMethod.DELETE, entity, String.class);
        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mockAuthUserRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAuthUser_Non_Existing_User_DELETE_OK_204() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.empty()); // user not found
        doNothing().when(mockAuthUserRepository).deleteById(1L);
        // When
        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(String.format(AUTH_USERS_WITH_ID, 1L),
                HttpMethod.DELETE, entity, String.class);
        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mockAuthUserRepository, times(0)).deleteById(1L);
    }

}
