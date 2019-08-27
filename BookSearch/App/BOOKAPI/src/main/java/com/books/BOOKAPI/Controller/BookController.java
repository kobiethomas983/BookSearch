package com.books.BOOKAPI.Controller;

import com.books.BOOKAPI.Model.Book;
import com.books.BOOKAPI.Model.GoogleBooks;
import com.books.BOOKAPI.Service.BookService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

//JPA for model
//this allows the two services to communicate with each other reactjs and spring backend
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })

@RestController
@Slf4j
@RequestMapping("/api/v1/googlebooks")
public class BookController {

    private final String googleBooksApiUrl = "https://www.googleapis.com/books/v1/";


    @Autowired
    private BookService booksService;

    /**
     * Function to fetch all google books using the google book api's
     * The response from the google book api is directly mapped to
     * the Java Object GoogleBooks
     *
     * No Jackson or other Json libraries are needed to map the response
     * to the java object
     * @param name
     * @return List of google books
     */
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<GoogleBooks> getBook(@PathVariable("name") String name) {
        name = name.replaceAll(" ", "%20");
        String url = googleBooksApiUrl + "volumes?q=" + name + "&maxResults=1";

        // Get the rest template
        RestTemplate restTemplate = new RestTemplate();

        // Call the api to get books grab data from api call
        ResponseEntity<GoogleBooks> responseEntity = restTemplate
                .getForEntity(url, GoogleBooks.class );

        GoogleBooks book = GoogleBooks.builder().build();
        book = responseEntity.getBody();
        log.info("google books api response is {}", book);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * Function to parse the google book api response using the
     * jackson stream parser.
     * TODO: To be complated later
     * @param name
     * @return
     * @throws IOException
     */
    @GetMapping("/stream/{name}")
    public ResponseEntity<String> getBookStream(@PathVariable("name") String name) throws IOException {
        name = name.replaceAll(" ", "%20");
        String url = googleBooksApiUrl + "/volumes?q=" + name + "&maxResults=1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);

        StringBuilder sb = new StringBuilder();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(responseEntity.getBody());
        while (jsonParser.nextToken() != null) {
            JsonToken token = jsonParser.getCurrentToken();
            if (token.equals(JsonToken.FIELD_NAME)) {
                String fieldName = jsonParser.getCurrentName();

                jsonParser.nextToken();

                if (fieldName.equals("items")) {
                    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        String nameField = jsonParser.getCurrentName();
                        sb.append("current field name is " + nameField + " === ");
                        jsonParser.nextToken();
                    }
                }
            }
        }
        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }

    /**
     * Function to call the google book api's to get the books using
     * the title name passed by the user. In this function jackson's
     * JsonNode is used to parse the json output and to map it to
     * corresponding java object Book.
     *
     * This function is useful is we dont want to map the entire json
     * response to java object and want to extract only required fields
     * @param name
     * @return
     */
    @GetMapping("/bookname/{name}")
    public ResponseEntity getGoogleBooks(@PathVariable("name") String name) {
        List<Book> books;
        books = booksService.getBooks(name);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/bookname/showbook/{isbn}")
    public ResponseEntity getById(@PathVariable("isbn") String isbn){
        Book retrievedById = booksService.findById(isbn);

        return ResponseEntity.ok(retrievedById);
    }
}
