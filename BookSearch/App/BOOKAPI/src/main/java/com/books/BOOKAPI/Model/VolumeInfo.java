package com.books.BOOKAPI.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VolumeInfo {
    private String title;
    private String[] authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int averageRating;
    private String language;
    private int pageCount;
    private String printType;
    private int ratingsCount;
    private ImageLinks imageLinks;
}
