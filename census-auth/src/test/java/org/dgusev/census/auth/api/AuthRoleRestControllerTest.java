package org.dgusev.census.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test for AuthRoleRestController (with MockMvc and mocked Repositories).
 * This is a unit test because of mocked Repositories - testing just one layer (web).
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthRoleRestControllerTest {

    private static final String           STR_DATETIME_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat DATETIME_WITH_TIMEZONE     = new SimpleDateFormat(STR_DATETIME_WITH_TIMEZONE);

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthUserRepository mockAuthUserRepository;
    @MockBean
    private AuthRoleRepository mockAuthRoleRepository;

    @Before
    public void beforeEach() {}

    @BeforeClass
    public static void beforeAll() {
        DATETIME_WITH_TIMEZONE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    public void testFindAllAuthUsers() throws Exception {
        // Given
        Date date = new Date();
        String strDate = DATETIME_WITH_TIMEZONE.format(date);

        List<AuthUser> users = Arrays.asList(
                new AuthUser(1L, "Simple User #1", "Description #1",
                        "username1", "password1", true, date, null, null),
                new AuthUser(2L, "Simple User #2", "Description #2",
                        "username2", "password2", false, date, null, null)
        );
        // Given
        when(mockAuthUserRepository.findAll()).thenReturn(users);

        // When
        mockMvc.perform(get("/census/api/auth/users"))

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
                .andExpect(jsonPath("$[0].createdAt", is(strDate)))
                .andExpect(jsonPath("$[0].modifiedAt", is(nullValue()))) // check for null value of json element
                .andExpect(jsonPath("$[0].roles", is(nullValue())))
                // user #2
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Simple User #2")))
                .andExpect(jsonPath("$[1].description", is("Description #2")))
                .andExpect(jsonPath("$[1].username", is("username2")))
                .andExpect(jsonPath("$[1].password", is("password2")))
                .andExpect(jsonPath("$[1].active", is(false)))
                .andExpect(jsonPath("$[1].createdAt", is(strDate)))
                .andExpect(jsonPath("$[1].modifiedAt", is(nullValue())))
                .andExpect(jsonPath("$[1].roles", is(nullValue())));

        // Then
        verify(mockAuthUserRepository, times(1)).findAll();
    }

    @Test
    public void findAuthUserById() throws Exception {
        // Given
        Date date = new Date();
        String strDate = DATETIME_WITH_TIMEZONE.format(date);

        AuthUser authUser = new AuthUser(1L, "Simple User #1", "Description #1",
                "username1", "password1", true, date, null, null);
        when(mockAuthUserRepository.findById(1L)).thenReturn(Optional.of(authUser));

        // When
        mockMvc.perform(get("/census/api/auth/users/1"))
                // Then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Simple User #1")))
                .andExpect(jsonPath("$.description", is("Description #1")))
                .andExpect(jsonPath("$.username", is("username1")))
                .andExpect(jsonPath("$.password", is("password1")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.createdAt", is(strDate)))
                .andExpect(jsonPath("$.modifiedAt", is(nullValue())))
                .andExpect(jsonPath("$.roles", is(nullValue())));

        // Then
        verify(mockAuthUserRepository, times(1)).findById(1L);
    }

    /*
    @Test
    public void find_bookIdNotFound_404() throws Exception {
        mockMvc.perform(get("/books/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_book_OK() throws Exception {

        Book newBook = new Book(1L, "Spring Boot Guide", "mkyong", new BigDecimal("2.99"));
        when(mockRepository.save(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/books")
                .content(om.writeValueAsString(newBook))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Spring Boot Guide")))
                .andExpect(jsonPath("$.author", is("mkyong")))
                .andExpect(jsonPath("$.price", is(2.99)));

        verify(mockRepository, times(1)).save(any(Book.class));

    }

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

    @Test
    public void delete_book_OK() throws Exception {

        doNothing().when(mockRepository).deleteById(1L);

        mockMvc.perform(delete("/books/1"))
                //.andDo(print())
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).deleteById(1L);
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
