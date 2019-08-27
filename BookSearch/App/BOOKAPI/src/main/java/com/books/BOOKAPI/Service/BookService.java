package com.books.BOOKAPI.Service;

import com.books.BOOKAPI.Model.Book;
import com.books.BOOKAPI.Model.GoogleBooks;
import com.books.BOOKAPI.Repository.BookRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class BookService {
    private final String googleBooksApiUrl = "https://www.googleapis.com/books/v1/";
    //Object Mappers are expensive so reinitialize if have to
    private static ObjectMapper mapper = new ObjectMapper();
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks(String name) {
        name = name.replaceAll(" ", "%20");
        String url = googleBooksApiUrl + "volumes?q=" + name + "&maxResults=5";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);



        // Dont fail for not mapped values
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        // Fail if primitive values are null
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        //totalitems arraylist
        GoogleBooks book = GoogleBooks.builder().build();
        ArrayList<Book> books = new ArrayList<>();
        try {
            //JsonNode jsonNode = mapper.readValue(responseEntity.getBody(), JsonNode.class);
            JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
            JsonNode items = jsonNode.get("items");
            if (items != null && items.isArray()) {
                for (JsonNode item : items) {
                    books.add(processItems(item));
                }
            }

        } catch (IOException e) {
            e.getMessage();
        }

        return books;
    }


    //Process data that you want to minpulate
    private Book processItems(JsonNode items) {

        String id = items.get("id").asText();
        StringBuilder authorsSB = new StringBuilder();
        StringBuilder categoriesSb = new StringBuilder();
        StringBuilder isbnsSb = new StringBuilder();
        JsonNode volumeInfo = items.get("volumeInfo");
        String title = volumeInfo.get("title").asText();

        JsonNode authors = volumeInfo.get("authors");
        if (authors != null) {
            if (authors.isArray()) {
                for (JsonNode author : authors) {
                    authorsSB.append(author.asText() + ",");
                }
            }

        }
        String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").textValue() : "";
        String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").textValue() : "";
        String description = volumeInfo.has("description") ? volumeInfo.get("description").textValue() : "";
        int pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").asInt() : 0;
        int averageRating = volumeInfo.has("averageRating") ? volumeInfo.get("averageRating").asInt() : 0;
        int ratingsCount = volumeInfo.has("ratingsCount") ? volumeInfo.get("ratingsCount").asInt() : 0;
        String language = volumeInfo.has("language") ? volumeInfo.get("language").textValue() : "";

        JsonNode isbnArray = volumeInfo.get("industryIdentifiers");
        if(isbnArray != null && isbnArray.isArray()){
            for(JsonNode isbnItem : isbnArray){
                isbnsSb.append(isbnItem.get("identifier").asText() + ",");
            }
        }

        JsonNode categories = volumeInfo.get("categories");
        if (categories != null && categories.isArray()) {
            for (JsonNode category : categories) {
                categoriesSb.append(category.asText() + ",");
            }
        }

        JsonNode imageLinks = volumeInfo.get("imageLinks");
        String thumbNail = imageLinks.has("thumbnail") ? imageLinks.get("thumbnail").textValue() : "";

        if (description.length() > 1000) {
            description = description.substring(0, 999);
        }
        Book book = Book.builder()
                .id(id)
                .title(title)
                .authors(authorsSB.toString())
                .publisher(publisher)
                .publishedDate(publishedDate)
                .description(description)
                .pageCount(pageCount)
                .categories(categoriesSb.toString())
                .averageRating(averageRating)
                .ratingsCount(ratingsCount)
                .language(language)
                .thumbnail(thumbNail)
                .isbn(isbnsSb.toString()).build();
        bookRepository.save(book);
        return book;

    }

    public Book findById(String isbn){
        Book foundBook = new Book();
        String url = googleBooksApiUrl + "volumes?q=isbn:"+ isbn;

        RestTemplate restCall = new RestTemplate();

        ResponseEntity<String> responseEntity = restCall.getForEntity(url,String.class);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        //fail if primitive values are null
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        ArrayList<Book> books = new ArrayList<>();
        try{
            JsonNode json = mapper.readTree(responseEntity.getBody());
            JsonNode items = json.get("items");

            if(items != null && items.isArray()){
                for(JsonNode item : items){
                    books.add(processItems(item));
                }
            }

        }catch(IOException e){
            e.getMessage();
            e.printStackTrace();
        }

        foundBook = books.get(0);
        return foundBook;
    }
}
