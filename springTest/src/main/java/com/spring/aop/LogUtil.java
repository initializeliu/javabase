package com.spring.aop;

public class LogUtil {
    public static void beforDoMethod(String methodName){
        System.out.println("[" + methodName + "] 方法执行之前");
    }
    public static void afterDoMethod(String methodName){
        System.out.println("[" + methodName + "] 方法执行之后");
    }
    public static void exceptionDoMethod(Exception message){
        System.out.println("方法抛出异常 [" +message.getCause() + "] ");
    }
    public static void finalDoMethod(String methodName){
        System.out.println("[" + methodName + "] 方法最终完成");
    }
}
