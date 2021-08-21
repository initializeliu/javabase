package com.spring.tx;

public class Book {
    private Integer bookId;
    private String name;
    private Integer price;

    public Book(){}
    public Book(Integer bookId, String name, Integer price) {
        this.bookId = bookId;
        this.name = name;
        this.price = price;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
