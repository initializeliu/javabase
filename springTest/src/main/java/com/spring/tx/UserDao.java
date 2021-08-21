package com.spring.tx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int updateBalance(Integer userId, Integer price){
        String sql = "update user set balance = balance - ? where userId = ?";
        return jdbcTemplate.update(sql, price, userId);
    }
}
