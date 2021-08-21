package com.spring.autowired;

import com.spring.autowired.Appconfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {


    public static void main(String[] args) {


        //初始化Spring容器
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);


        //关闭循环依赖
//        ac.setAllowCircularReferences(false);

//        InfoService infoService = ac.getBean(InfoService.class);
//        UserService infoService = ac.getBean(UserService.class);

//        System.out.println("infoService = " + infoService);


    }


}
