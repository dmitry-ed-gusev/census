package org.dgusev.census.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dgusev.census.auth.domain.entity.AuthUser;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
        //Book book = new Book(1L, "Book Name", "Mkyong", new BigDecimal("9.99"));
        //when(mockRepository.findById(1L)).thenReturn(Optional.of(book));

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
        JSONAssert.assertEquals(expected, response.getBody(), false);
        // Then
        verify(mockAuthUserRepository, times(1)).findAll();
    }

    /*
    @Test
    public void find_bookId_OK() throws JSONException {

        String expected = "{id:1,name:\"Book Name\",author:\"Mkyong\",price:9.99}";

        ResponseEntity<String> response = restTemplate.getForEntity("/books/1", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());

        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_bookIdNotFound_404() throws Exception {

        String expected = "{status:404,error:\"Not Found\",message:\"Book id not found : 5\",path:\"/books/5\"}";

        ResponseEntity<String> response = restTemplate.getForEntity("/books/5", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    public void save_book_OK() throws Exception {

        Book newBook = new Book(1L, "Spring Boot Guide", "mkyong", new BigDecimal("2.99"));
        when(mockRepository.save(any(Book.class))).thenReturn(newBook);

        String expected = om.writeValueAsString(newBook);

        ResponseEntity<String> response = restTemplate.postForEntity("/books", newBook, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).save(any(Book.class));

    }

    @Test
    public void update_book_OK() throws Exception {

        Book updateBook = new Book(1L, "ABC", "mkyong", new BigDecimal("19.99"));
        when(mockRepository.save(any(Book.class))).thenReturn(updateBook);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateBook), headers);

        ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(om.writeValueAsString(updateBook), response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Book.class));

    }

    @Test
    public void patch_bookAuthor_OK() {

        when(mockRepository.save(any(Book.class))).thenReturn(new Book());
        String patchInJson = "{\"author\":\"ultraman\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(patchInJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Book.class));

    }

    @Test
    public void patch_bookPrice_405() throws JSONException {

        String expected = "{status:405,error:\"Method Not Allowed\",message:\"Field [price] update is not allow.\"}";

        String patchInJson = "{\"price\":\"99.99\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(patchInJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void delete_book_OK() {

        doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/books/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

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
