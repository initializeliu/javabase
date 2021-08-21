package com.spring.tx;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionManager;

public class Test {
    public static void main(String[] args) {

        ApplicationContext ac = new ClassPathXmlApplicationContext("tx-application.xml");

//        BookService bookService = ac.getBean(BookService.class);
//        bookService.buyBook(1, 2);

        MulService mulService = ac.getBean(MulService.class);
        mulService.mulTx();

    }
}
