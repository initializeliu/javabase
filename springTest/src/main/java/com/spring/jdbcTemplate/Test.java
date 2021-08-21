package com.spring.jdbcTemplate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("jdbc-application.xml");

//        DataSource dataSource = (DataSource) ac.getBean("datasource");
//        Connection jdbcCon = dataSource.getConnection();
//        System.out.println(jdbcCon);

//        JdbcTemplate jdbcTemplate = ac.getBean(JdbcTemplate.class);
//
//        jdbcTemplate.execute("insert into emp(name, age) values ('zhangsan', 33),('lisi', 33),('wangwu', 32),('wangwu', 35),('zhaoliu', 37)");

//        jdbcTemplate.execute("update emp set name='aaa' where id = 1");

//        jdbcTemplate.query("", new RowCallbackHandler() {
//            @Override
//            public void processRow(ResultSet rs) throws SQLException {
//
//            }
//        });

//        jdbcTemplate.query("select id, name, age from person where id = 3", new BeanPropertyRowMapper(Person.class));


        NamedParameterJdbcTemplate namedJdbcTemplate = ac.getBean(NamedParameterJdbcTemplate.class);

        String sql = "INSERT INTO emp(name, age) VALUES(:name, :age)";
        Emp emp = new Emp("aaa",11);
        namedJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(emp));


    }
    static class Emp{
        private int id;
        private String name;
        private int age;

        public Emp(){}
        public Emp(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Emp(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
