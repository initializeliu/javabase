package com.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {

//        Calculator calculator = new MyCalculator();

        //自己手动在调用方法前添加日志
//        LogUtil.beforDoMethod("add");
//        int result = calculator.add(3, 6);
//        LogUtil.afterDoMethod("add");
//        System.out.println(result);
//        LogUtil.finalDoMethod("add");

        //使用代理对象
//        calculator = ProxyCalculator.getCalculatorProxy(calculator);
//        System.out.println(calculator.add(4, 9));
//        System.out.println(calculator.div(3, 0));


        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("aop-application.xml");

        Calculator calculator = applicationContext.getBean(Calculator.class);
        System.out.println(calculator.add(3, 9));




    }
}
