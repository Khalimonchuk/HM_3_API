package com.lits.api.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.Expose;
import okhttp3.Response;

import java.io.IOException;

public class SearchEntry {


    private String isbn;
    private String name;
    private String description;
    private String author;
    private String publishDate;
    private String publisher;
    @Expose (deserialize = false)
    private  String objectID;

    @Expose (deserialize = false)
    private  String[] _highlightResult;

    public SearchEntry() {
        // Needed for JSON-to-Object parsing
    }

    public SearchEntry(String isbn, String name, String description, String author, String publishDate, String publisher, String objectID, String[] _highlightResult) {
        this.isbn = isbn;
        this.name = name;
        this.description = description;
        this.author = author;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this._highlightResult=_highlightResult;
        this.objectID=objectID;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

      public void set_highlightResult(String[] _highlightResult) {
      this._highlightResult = _highlightResult;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getObjectID() {
        return objectID;
    }

    public String[] get_highlightResult() {
        return _highlightResult;
    }
}
