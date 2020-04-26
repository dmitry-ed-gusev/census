package org.dgusev.census.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dgusev.census.auth.domain.entity.AuthUser;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Unit test for AuthRestController (with MockMvc and mocked Repositories).
 * This is a unit test because of mocked Repositories - testing just one layer (web).
 */

// todo: https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/
// todo: https://mkyong.com/spring-boot/spring-rest-integration-test-example/
// todo: https://reflectoring.io/spring-boot-web-controller-test/

// this annotation is suitable for unit test for REST controller (only)
@WebMvcTest (controllers = AuthUserRestController.class)

// this two annotations (together) are suitable for loading full application context
//@SpringBootTest
//@AutoConfigureMockMvc

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthUserRestControllerMockMvcTest {

    // dates
    private static final String           STR_DATETIME_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat DATETIME_WITH_TIMEZONE     = new SimpleDateFormat(STR_DATETIME_WITH_TIMEZONE);

    static { // static initialization block for datetime with timezone
        DATETIME_WITH_TIMEZONE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private static final Date             CURRENT_DATE               = new Date();
    private static final String           CURRENT_STR_DATE           = DATETIME_WITH_TIMEZONE.format(CURRENT_DATE);

    // URLs
    private static final String           AUTH_USERS_URL             = "/census/api/auth/users";
    private static final String           AUTH_USERS_WITH_ID         = AUTH_USERS_URL + "/%s";

    // Jackson JSON Object mapper
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc; // to perform HTTP REST requests

    @MockBean
    private AuthUserRepository mockAuthUserRepository; // mock for repository

    private AuthUser authUser = null; // user for tests, init before each test

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
        // When
        mockMvc.perform(get(AUTH_USERS_URL))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                // Then - expected user #1
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Simple User #1")))
                .andExpect(jsonPath("$[0].description", is("Description #1")))
                .andExpect(jsonPath("$[0].username", is("username1")))
                .andExpect(jsonPath("$[0].password", is("password1")))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].createdAt", is(CURRENT_STR_DATE)))
                .andExpect(jsonPath("$[0].modifiedAt", is(nullValue()))) // check for null value of json element
                .andExpect(jsonPath("$[0].roles", is(nullValue())))
                // Then - expected user #2
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Simple User #2")))
                .andExpect(jsonPath("$[1].description", is("Description #2")))
                .andExpect(jsonPath("$[1].username", is("username2")))
                .andExpect(jsonPath("$[1].password", is("password2")))
                .andExpect(jsonPath("$[1].active", is(false)))
                .andExpect(jsonPath("$[1].createdAt", is(CURRENT_STR_DATE)))
                .andExpect(jsonPath("$[1].modifiedAt", is(nullValue())))
                .andExpect(jsonPath("$[1].roles", is(nullValue())));
        // Then
        verify(mockAuthUserRepository, times(1)).findAll();
    }

    @Test
    public void testFindAuthUserById_GET_OK_200() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        // When
        mockMvc.perform(get(String.format(AUTH_USERS_WITH_ID, 1)))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Simple User #1")))
                .andExpect(jsonPath("$.description", is("Description #1")))
                .andExpect(jsonPath("$.username", is("username1")))
                .andExpect(jsonPath("$.password", is("password1")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.createdAt", is(CURRENT_STR_DATE)))
                .andExpect(jsonPath("$.modifiedAt", is(nullValue())))
                .andExpect(jsonPath("$.roles", is(nullValue())));
        // Then
        verify(mockAuthUserRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAuthUserById_GET_NotFound_404() throws Exception {
        // When - Then
        mockMvc.perform(get(String.format(AUTH_USERS_WITH_ID, 111)))
                .andExpect(status().isNotFound());
        // Then
        verify(mockAuthUserRepository, times(1)).findById(111L);
    }

    @Test
    public void testSaveNewAuthUser_POST_OK_201() throws Exception {
        // Given
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenReturn(authUser);
        // When
        mockMvc.perform(post(AUTH_USERS_URL)
                .content(om.writeValueAsString(authUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Simple User #1")))
                .andExpect(jsonPath("$.description", is("Description #1")))
                .andExpect(jsonPath("$.username", is("username1")))
                .andExpect(jsonPath("$.password", is("password1")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.createdAt", is(CURRENT_STR_DATE)))
                .andExpect(jsonPath("$.modifiedAt", is(nullValue())))
                .andExpect(jsonPath("$.roles", is(nullValue())));
        // Then
        verify(mockAuthUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testSaveNewAuthUser_POST_Fail_500() throws Exception {
        // Given
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenThrow(DataIntegrityViolationException.class);
        // When
        mockMvc.perform(post(AUTH_USERS_URL)
                .content(om.writeValueAsString(authUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
                //.andExpect(jsonPath("$.message", is("Data Integrity Error!")));
        // Then
        verify(mockAuthUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testUpdateExistingUser_PUT_OK_200() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser)); // found user
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenReturn(authUser); // call to save
        // When
        mockMvc.perform(put(String.format(AUTH_USERS_WITH_ID, 1L))
                .content(om.writeValueAsString(authUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
                // todo: add test for returned content (updated user)
        // Then
        verify(mockAuthUserRepository, times(1)).findById(1L);
        verify(mockAuthUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testUpdateNonExistingUser_PUT_FAIL_404() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.empty()); // user not found
        // When
        mockMvc.perform(put(String.format(AUTH_USERS_WITH_ID, 1L))
                .content(om.writeValueAsString(authUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
                // todo: add test for returned content (updated user)
        // Then
        verify(mockAuthUserRepository, times(1)).findById(1L);
        verify(mockAuthUserRepository, times(0)).save(any(AuthUser.class));
    }

    @Test
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

        // When
        mockMvc.perform(patch(String.format(AUTH_USERS_WITH_ID, 1L))
                .content(om.writeValueAsString(update))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk());

        // Then
        verify(mockAuthUserRepository, times(1)).findById(1L);
        // invoke save() with updated user
        verify(mockAuthUserRepository, times(1)).save(eq(updatedAuthUser));
    }

    @Test
    public void testUpdateNonExistingUser_PATCH_FAIL_404() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.empty()); // user not found
        // When
        mockMvc.perform(patch(String.format(AUTH_USERS_WITH_ID, 1L))
                .content(om.writeValueAsString(new HashMap<>()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
        // Then
        verify(mockAuthUserRepository, times(0)).save(any(AuthUser.class));
    }

    @Test
    public void testDeleteAuthUser_Existing_User_DELETE_OK_204() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        // todo: do we need this? -> doNothing().when(mockAuthUserRepository).deleteById(1L);
        // When
        mockMvc.perform(delete(String.format(AUTH_USERS_WITH_ID, 1)))
                //.andDo(print())
                // Then
                .andExpect(status().isNoContent());
        // Then
        verify(mockAuthUserRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAuthUser_Non_Existing_User_DELETE_OK_204() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.empty()); // user not found
        // todo: do we need this? -> doNothing().when(mockAuthUserRepository).deleteById(1L);
        // When
        mockMvc.perform(delete(String.format(AUTH_USERS_WITH_ID, 1)))
                // Then
                .andExpect(status().isNoContent());
        // Then
        verify(mockAuthUserRepository, times(0)).deleteById(1L);
    }

}
