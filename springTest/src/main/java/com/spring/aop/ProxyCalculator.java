package com.spring.aop;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyCalculator {

    public static Calculator getCalculatorProxy(Calculator calculator){

//        InvocationHandler handler = new InvocationHandler(){
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                return null;
//            }
//        };

        /**
         * ClassLoader loader,
         * Class<?>[] interfaces,
         * InvocationHandler h
         */
        return (Calculator)Proxy.newProxyInstance(calculator.getClass().getClassLoader(),
                calculator.getClass().getInterfaces(),
                (Object proxy, Method method, Object[] args) -> {
                    Object obj = null;
                    try{
                        LogUtil.beforDoMethod(method.getName());
                        obj = method.invoke(calculator, args);
                        LogUtil.afterDoMethod(method.getName());
                    }catch (Exception e){
                        LogUtil.exceptionDoMethod(e);
                    }finally {
                        LogUtil.finalDoMethod(method.getName());
                    }
                    return obj;
                });
    }

}
