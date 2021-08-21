package com.spring.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 获取库存数量
     * @return
     */
    public Integer getStock(Integer bookId){
        String sql = "select bookStock from book_stock where bookId = ?";
        List<Integer> stocks = jdbcTemplate.query(sql, new SingleColumnRowMapper(), bookId);
        return stocks.get(0);
    }
    /**
     * 获取图书价格
     */
    public Integer getPrice(Integer bookId){
        String sql = "select price from book where bookId = ?";
        List<Integer> prices = jdbcTemplate.query(sql, new SingleColumnRowMapper(), bookId);
        return prices.get(0);
    }

    /**
     * 减库存
     */
    public int updateStock(Integer bookId){
        String sql = "update book_stock set bookStock = bookStock - 1 where bookId = ?";
        return jdbcTemplate.update(sql, bookId);
    }

}
