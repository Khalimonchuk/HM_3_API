package com.lits.api.test;

import com.google.gson.annotations.Expose;

public class Books {

    @Expose (deserialize = true)
       public String isbn;
    @Expose(deserialize = false)
       public String name;
    @Expose (deserialize = false)
       public String description;
    @Expose (deserialize = false)
       public String author;
    @Expose (deserialize = false)
       public String publishDate;
    @Expose (deserialize = false)
       public String publisher;
    @Expose (deserialize = false)
       public String img;

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

    public Books(String isbn, String name, String description, String author, String publishDate, String publisher) {
        this.isbn = isbn;
        this.name = name;
        this.description = description;
        this.author = author;
        this.publishDate = publishDate;
        this.publisher = publisher;
    }

    public Books(String isbn, String name, String description, String author, String publishDate, String publisher, String img) {
        this.isbn = isbn;
        this.name = name;
        this.description = description;
        this.author = author;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
