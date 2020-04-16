package org.dgusev.census.auth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dgusev.census.auth.domain.entity.AuthRole;
import org.dgusev.census.auth.domain.entity.AuthUser;
import org.dgusev.census.auth.repository.AuthRoleRepository;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Unit test for AuthRestController (with MockMvc and mocked Repositories).
 * This is a unit test because of mocked Repositories - testing just one layer (web).
 */

// todo: https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/
// todo: https://mkyong.com/spring-boot/spring-rest-integration-test-example/

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthUserRestControllerTest {

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


    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc; // to perform HTTP REST requests

    @MockBean
    private AuthUserRepository mockAuthUserRepository; // mock for repository

    @Before
    public void beforeEach() {}

    @BeforeClass
    public static void beforeAll() {}

    @Test
    public void testFindAllAuthUsers_OK_200() throws Exception {
        // Given
        List<AuthUser> users = Arrays.asList(
                new AuthUser(1L, "Simple User #1", "Description #1",
                        "username1", "password1", true, CURRENT_DATE, null, null),
                new AuthUser(2L, "Simple User #2", "Description #2",
                        "username2", "password2", false, CURRENT_DATE, null, null)
        );
        // Given
        when(mockAuthUserRepository.findAll()).thenReturn(users);

        // When
        mockMvc.perform(get(AUTH_USERS_URL))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                // user #1
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Simple User #1")))
                .andExpect(jsonPath("$[0].description", is("Description #1")))
                .andExpect(jsonPath("$[0].username", is("username1")))
                .andExpect(jsonPath("$[0].password", is("password1")))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].createdAt", is(CURRENT_STR_DATE)))
                .andExpect(jsonPath("$[0].modifiedAt", is(nullValue()))) // check for null value of json element
                .andExpect(jsonPath("$[0].roles", is(nullValue())))
                // user #2
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
    public void testFindAuthUserById_OK_200() throws Exception {
        // Given
        AuthUser authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, CURRENT_DATE, null, null);
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
    public void testFindAuthUserById_NotFound_404() throws Exception {
        // When - Then
        mockMvc.perform(get(String.format(AUTH_USERS_WITH_ID, 111))).andExpect(status().isNotFound());
        // Then
        verify(mockAuthUserRepository, times(1)).findById(111L);
    }

    @Test
    public void testSaveNewAuthUser_OK_201() throws Exception {
        // Given
        AuthUser authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, CURRENT_DATE, null, null);
        when(mockAuthUserRepository.save(any(AuthUser.class))).thenReturn(authUser);

        // When
        mockMvc.perform(post(AUTH_USERS_URL)
                .content(om.writeValueAsString(authUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
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
    public void testSaveNewAuthUser_Fail_500() throws Exception {
        // Given
        AuthUser authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, CURRENT_DATE, null, null);
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
    public void testUpdateExistingUser_PUT_OK_200() {
//        mockMvc.perform(put("/heavyresource/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(
//                        new HeavyResource(1, "Tom", "Jackson", 12, "heaven street")))
//        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdateNonExistingUser_PUT_FAIL_404() {

    }

    @Test
    public void testUpdateExistingUser_PATCH_OK_200() {
        // option 1
//        mockMvc.perform(patch("/heavyrecource/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(
//                        new HeavyResourceAddressOnly(1, "5th avenue")))
//        ).andExpect(status().isOk());

        // option 2
//        HashMap<String, Object> updates = new HashMap<>();
//        updates.put("address", "5th avenue");
//
//        mockMvc.perform(patch("/heavyresource/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(updates))
//        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdateNonExistingUser_PATCH_FAIL_404() {

    }

    @Test
    public void testDeleteAuthUser_Existing_User_OK_204() throws Exception {
        // Given
        AuthUser authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, CURRENT_DATE, null, null);
        // return found user
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));
        // do nothing when call deleteById()
        doNothing().when(mockAuthUserRepository).deleteById(1L);

        // When
        mockMvc.perform(delete(String.format(AUTH_USERS_WITH_ID, 1)))
                //.andDo(print())
                // Then
                .andExpect(status().isNoContent());

        // Then
        verify(mockAuthUserRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAuthUser_Non_Existing_User_OK_204() throws Exception {
        // Given
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.empty()); // user not found
        // do nothing when call deleteById()
        doNothing().when(mockAuthUserRepository).deleteById(1L);

        // When
        mockMvc.perform(delete(String.format(AUTH_USERS_WITH_ID, 1)))
                // Then
                .andExpect(status().isNoContent());
        // Then
        verify(mockAuthUserRepository, times(0)).deleteById(1L);
    }

    /*
    @Test
    public void update_book_OK() throws Exception {

        Book updateBook = new Book(1L, "ABC", "mkyong", new BigDecimal("19.99"));
        when(mockRepository.save(any(Book.class))).thenReturn(updateBook);

        mockMvc.perform(put("/books/1")
                .content(om.writeValueAsString(updateBook))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ABC")))
                .andExpect(jsonPath("$.author", is("mkyong")))
                .andExpect(jsonPath("$.price", is(19.99)));


    }

    @Test
    public void patch_bookAuthor_OK() throws Exception {

        when(mockRepository.save(any(Book.class))).thenReturn(new Book());
        String patchInJson = "{\"author\":\"ultraman\"}";

        mockMvc.perform(patch("/books/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Book.class));

    }

    @Test
    public void patch_bookPrice_405() throws Exception {

        String patchInJson = "{\"price\":\"99.99\"}";

        mockMvc.perform(patch("/books/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Book.class));
    }


    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    */
}
