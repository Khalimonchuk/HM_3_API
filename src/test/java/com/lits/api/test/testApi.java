package com.lits.api.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lits.api.httpClient;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.print.Book;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import static com.lits.api.httpClient.convert;

public class testApi {

    private httpClient client = new httpClient();


    @Test (description =  "LOGIN USER correct")
    public void LoginToSystem() throws IOException {
//

        String url = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/auth/login";
        HashMap<String, String> body = new HashMap<>();
        body.put("email", "i.khalimonchuk@gmail.com");
        body.put("password", "1kh23456");


        Response newone = client.POST(url, Headers.of(), body);


        final Map<String, Map<String, String>> loginResponceData = convert (newone, Map.class);
        String accessToken =  loginResponceData.get("r").get("access_token");

        Assert.assertEquals(newone.code(), 200);
        Assert.assertNotNull(accessToken);


    }

    @Test (description="GET USER INFO BY USER ID")
    public void GetUserByCorrectId () {

        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();

        String id = "3LuOOmU7rndmblTUFxFI";
        String url2 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/users/"+id;

        Response new1 = client.GET(url2, Headers.of(), ac1);

        final Map<String, String> userData = convert (new1, Map.class);

        String id1 = userData.get("id");
        String fn = userData.get("firstName");
        String ln = userData.get("lastName");
        String email = userData.get("email");

        Assert.assertEquals(new1.code(), 200);
        Assert.assertEquals(id1, id);
        Assert.assertEquals(fn, "Ivn");
        Assert.assertEquals(ln, "Kh");
        Assert.assertEquals(email, "i.khalimonchuk@gmail.com");


    }

    @Test (description="GET USER INFO BY USER ID Without search criteria")
    public void GetUserWithoutIdInRequest () {

        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();

        String id = "";
        String url2 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/users/"+id;

        Response new1 = client.GET(url2, Headers.of(), ac1);

        final Map<String, String> userData = convert (new1, Map.class);

        String error = userData.get("err");

        Assert.assertEquals(new1.code(), 400);
        Assert.assertEquals(error, "please specify search attribute, e.g email");


    }


    @Test (description="GET USER INFO BY USER ID incorrect id")
    public void GetUserByNonCreatedId () {

        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();

        String id = "incorrect_______book";
        String url2 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/users/"+id;

        Response new1 = client.GET(url2, Headers.of(), ac1);

        final Map<String, String> userData = convert (new1, Map.class);

        String error = userData.get("msg");



        Assert.assertEquals(new1.code(), 200);
        Assert.assertEquals(error, "user with such id ["+id+"] does not exists");


    }




    @Test (description="GET USER INFO BY USER ID")
    public void GetBookByCorrectISBN () {

        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();

        String isbn = "44343431111";
        String url2 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books/"+isbn;

        Response new1 = client.GET(url2, Headers.of(), ac1);

        final Map<String, String> userData = convert (new1, Map.class);

        String isbnActual = userData.get("isbn");
        String name = userData.get("name");
        String description = userData.get("description");
        String author = userData.get("author");
        String publishDate = userData.get("publishDate");
        String publisher = userData.get("publisher");



        Assert.assertEquals(new1.code(), 200);
        //Assert.assertEquals(isbnActual, "4343431111");
        Assert.assertEquals(name, "The Martian");
        Assert.assertEquals(description, "The Martian is a 2011 science fiction novel written by Andy Weir. It was his debut novel under his own name. It was originally self-published in 2011; Crown Publishing purchased the rights and re-released it in 2014");
        Assert.assertEquals(author, "Andy Weir");
        Assert.assertEquals(publishDate, "2011");
        Assert.assertEquals(publisher, "self-published");



    }


    @Test (description="GET USER INFO BY incorrect USER ID")
    public void GetBookByINCorrectISBN () {

        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
        int a = (int) (100000000*Math.random());

        String isbn = "44343431111";
        String url2 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books/"+isbn+a;

        Response new1 = client.GET(url2, Headers.of(), ac1);

        final Map<String, String> userData = convert (new1, Map.class);

        Assert.assertEquals(new1.code(), 404);

        String error = userData.get("msg");
        Assert.assertEquals(error, "book with such isbn ["+isbn+a+"] does not exists");

    }



    @Test (description="GET USER INFO BY incorrect USER ID")
    public void GetBookByWithotISBN () {

        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
        int a = (int) (100000000*Math.random());

        String isbn = "44343431111";
        String url2 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books/";

        Response new1 = client.GET(url2, Headers.of(), ac1);

        final List<Books> userData = convert (new1, List.class);

        Assert.assertEquals(new1.code(), 200);


        Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Books[] b = g.fromJson(userData.toString().trim(), Books[].class);

    }




    @Test  (description = "Correct book creation")
    public void CreateAndGetBook () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "10000"+a;
        String set_name = "ivkh_bookname_"+a;
        String set_description = "ivkh_bookDesc_"+a;
        String set_author = "Author_ivkh_test_"+a;
        String set_publishDate = "200"+b;
        String set_publisher = "self-published";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);



        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);

        final Map<String, String> BookData = convert (new1, Map.class);

        String isbn = BookData.get("isbn");
        String name = BookData.get("name");
        String description = BookData.get("description");
        String author = BookData.get("author");
        String publishDate = BookData.get("publishDate");
        String publisher = BookData.get("publisher");

        Assert.assertEquals(new1.code(), 200);
        Assert.assertEquals(isbn, set_isbn);
        Assert.assertEquals(name, set_name);
        Assert.assertEquals(description, set_description);
        Assert.assertEquals(author, set_author);
        Assert.assertEquals(publishDate, set_publishDate);
        Assert.assertEquals(publisher, set_publisher);


//Search newly created book
        String url4 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books/"+set_isbn;

        Response new4 = client.GET(url4, Headers.of(), ac1);

        final Map<String, String> BookData1 = convert (new4, Map.class);
        String isbn1 = BookData1.get("isbn");
        String name1 = BookData1.get("name");
        String description1 = BookData1.get("description");
        String author1 = BookData1.get("author");
        String publishDate1 = BookData1.get("publishDate");
        String publisher1 = BookData1.get("publisher");

        Assert.assertEquals(new4.code(), 200);
        Assert.assertEquals(isbn1, set_isbn);
        Assert.assertEquals(name1, set_name);
        Assert.assertEquals(description1, set_description);
        Assert.assertEquals(author1, set_author);
        Assert.assertEquals(publishDate1, set_publishDate);
        Assert.assertEquals(publisher1, set_publisher);


    }



    @Test  (description = "book creation withoy ISBN")
    public void CreateBookWithEmptyISBN () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "";
        String set_name = "ivkh_bookname_"+a;
        String set_description = "ivkh_bookDesc_"+a;
        String set_author = "Author_ivkh_test_"+a;
        String set_publishDate = "200"+b;
        String set_publisher = "self-published";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }

    @Test  (description = "book creation withot Name")
    public void CreateBookWithEmptyName () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "10000"+a;
        //String set_name = "ivkh_bookname_"+a;
        String set_name = "";
        String set_description = "ivkh_bookDesc_"+a;
        String set_author = "Author_ivkh_test_"+a;
        String set_publishDate = "200"+b;
        String set_publisher = "self-published";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }


    @Test  (description = "book creation withot description")
    public void CreateBookWithEmptyDescription () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "10000"+a;
        String set_name = "ivkh_bookname_"+a;
        //String set_description = "ivkh_bookDesc_"+a;
        String set_description = "";
        String set_author = "Author_ivkh_test_"+a;
        String set_publishDate = "200"+b;
        String set_publisher = "self-published";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }


    @Test  (description = "book creation withot author")
    public void CreateBookWithEmptyAuthor () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "10000"+a;
        String set_name = "ivkh_bookname_"+a;
        String set_description = "ivkh_bookDesc_"+a;
        //String set_author = "Author_ivkh_test_"+a;
        String set_author = "";
        String set_publishDate = "200"+b;
        String set_publisher = "self-published";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }


    @Test  (description = "book creation withot author")
    public void CreateBookWithEmptyPublishDate () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "10000"+a;
        String set_name = "ivkh_bookname_"+a;
        String set_description = "ivkh_bookDesc_"+a;
        String set_author = "Author_ivkh_test_"+a;
        String set_publishDate = "";
        //String set_publishDate = "200"+b;
        String set_publisher = "self-published";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }


    @Test  (description = "book creation withot author")
    public void CreateBookWithEmptyPublisher () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

        int a = (int) (Math.random() * 1000000);
        int b = (int) (Math.random() * 10);

        String set_isbn = "10000"+a;
        String set_name = "ivkh_bookname_"+a;
        String set_description = "ivkh_bookDesc_"+a;
        String set_author = "Author_ivkh_test_"+a;
        String set_publishDate = "200"+b;
        //String set_publisher = "self-published";
        String set_publisher = "";


        HashMap<String, String> bodyBook = new HashMap<>();
        bodyBook.put("isbn", set_isbn);
        bodyBook.put("name", set_name);
        bodyBook.put("description", set_description);
        bodyBook.put("author", set_author);
        bodyBook.put("publishDate", set_publishDate);
        bodyBook.put("publisher", set_publisher);


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }


    @Test  (description = "book creation withot request body")
    public void CreateBookWithoutBodyRequest () {

//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

//Create BOOK
        String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";


        HashMap<String, String> bodyBook = new HashMap<>();


        Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);
        final Map<String, String> BookData = convert (new1, Map.class);
        System.out.println(new1.code());
        Assert.assertEquals(new1.code(), 422);

    }




    @Test (description = "Create book with the same ISBN")
    public void CreateBookWithSameISBN () {
//LOGIN
            Login l1 = new Login();
            String ac1 = "Bearer " + l1.getToken();
//END LOGIN

            //Create BOOK
            String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

            int a = (int) (Math.random() * 1000000);
            int b = (int) (Math.random() * 10);

            String set_isbn = "10000"+a;
            String set_name = "ivkh_bookname_"+a;
            String set_description = "ivkh_bookDesc_"+a;
            String set_author = "Author_ivkh_test_"+a;
            String set_publishDate = "200"+b;
            String set_publisher = "self-published";


            HashMap<String, String> bodyBook = new HashMap<>();
            bodyBook.put("isbn", set_isbn);
            bodyBook.put("name", set_name);
            bodyBook.put("description", set_description);
            bodyBook.put("author", set_author);
            bodyBook.put("publishDate", set_publishDate);
            bodyBook.put("publisher", set_publisher);



            Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);

            final Map<String, String> BookData = convert (new1, Map.class);

            String isbn = BookData.get("isbn");
            String name = BookData.get("name");
            String description = BookData.get("description");
            String author = BookData.get("author");
            String publishDate = BookData.get("publishDate");
            String publisher = BookData.get("publisher");

            Assert.assertEquals(new1.code(), 200);
            Assert.assertEquals(isbn, set_isbn);
            Assert.assertEquals(name, set_name);
            Assert.assertEquals(description, set_description);
            Assert.assertEquals(author, set_author);
            Assert.assertEquals(publishDate, set_publishDate);
            Assert.assertEquals(publisher, set_publisher);


            Response new2 = client.POST(url3, Headers.of(), bodyBook, ac1);

            final Map<String, String> sameBook = convert (new2, Map.class);
            String error = sameBook.get("err");

            Assert.assertEquals(new2.code(), 400);
            Assert.assertEquals(error, "book with such isbn already exists");


        }



        /// SEARCH BLOCK

        @Test (description = "Search by Name")
         public void SearchBookByName () {
//LOGIN
        Login l1 = new Login();
        String ac1 = "Bearer " + l1.getToken();
//END LOGIN

            //Create BOOK
            String url3 = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/books";

            int a = (int) (Math.random() * 1000000);
            int b = (int) (Math.random() * 10);

            String set_isbn = "10000"+a;
            String set_name = "ivkh_bookname_"+a;
            String set_description = "ivkh_bookDesc_"+a;
            String set_author = "Author_ivkh_test_"+a;
            String set_publishDate = "200"+b;
            String set_publisher = "self-published";


            HashMap<String, String> bodyBook = new HashMap<>();
            bodyBook.put("isbn", set_isbn);
            bodyBook.put("name", set_name);
            bodyBook.put("description", set_description);
            bodyBook.put("author", set_author);
            bodyBook.put("publishDate", set_publishDate);
            bodyBook.put("publisher", set_publisher);



            Response new1 = client.POST(url3, Headers.of(), bodyBook, ac1);

            final Map<String, String> BookData = convert (new1, Map.class);

            String isbn = BookData.get("isbn");
            String name = BookData.get("name");
            String description = BookData.get("description");
            String author = BookData.get("author");
            String publishDate = BookData.get("publishDate");
            String publisher = BookData.get("publisher");

            Assert.assertEquals(new1.code(), 200);
            Assert.assertEquals(isbn, set_isbn);
            Assert.assertEquals(name, set_name);
            Assert.assertEquals(description, set_description);
            Assert.assertEquals(author, set_author);
            Assert.assertEquals(publishDate, set_publishDate);
            Assert.assertEquals(publisher, set_publisher);


            // SEARCH BY NAME

            String searchname = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/v1/search?q=ivkh_bookname_909332";

            Response new40 = client.GET(searchname, Headers.of(), ac1);
            final Map<String, ArrayList> sm = convert(new40, Map.class);


          //  SearchEntry a1 = SearchEntry.converting(sm.get("hits").toString(), SearchEntry.class);
          //  a1.getAuthor();

            int size = sm.get("hits").get(0).toString().length();

           // sm.get("hits").get();
            System.out.println(sm.get("hits"));

          // Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
          // SearchEntry p = g.fromJson(sm.get("hits").get(0).toString(), SearchEntry.class);
          //  p.getIsbn();

            Assert.assertEquals(new40.code(), 200);
            Assert.assertTrue(size>100);




          //  Map <String, String> book2 = convert(sm.get("hits"), Map.class);


            //System.out.println(a4);













    }






}
