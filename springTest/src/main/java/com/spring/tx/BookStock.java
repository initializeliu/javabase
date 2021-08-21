package com.spring.tx;

public class BookStock {
    private Integer bookId;
    private Integer bookStock;

    public BookStock(){}
    public BookStock(Integer bookId, Integer bookStock) {
        this.bookId = bookId;
        this.bookStock = bookStock;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getBookStock() {
        return bookStock;
    }

    public void setBookStock(Integer bookStock) {
        this.bookStock = bookStock;
    }
}
