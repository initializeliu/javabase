package com.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


/**
 * 步骤：
 * 0.  <context:component-scan base-package="com.spring.aop"/>
 *     <aop:aspectj-autoproxy/>
 *     注意添加aop命名空间
 * 1，创建切面类：@Aspect
 * 2，编写切面方法
 * 3，五种：@Before，@AfterReturning， @AfterThrowing，@After，@Around
 * 4, 配置文件和注解选一种方式即可
 */



@Component
//@Aspect
public class AspectCalculator {

//    try{
//        @Before
//        doMethod();
//        @AfterReturning
//    }catch(Exception e){
//        @AfterThrowing
//    }finally{
//        @After
//    }

    /**
     * 可以通过JoinPoint获取目标方法详细信息
     */
//    @Before("execution (public int com.spring.aop.MyCalculator.*(int, int))")
//    @Before("comAspect()")
    public void beforeAspect(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
//        signature.getName();
//        signature.getClass();
        System.out.println("===============>");
    }

//    @AfterReturning(value = "execution (public int com.spring.aop.MyCalculator.*(int, int))", returning = "aaa")
//    public void afterReturnAspect(String aaa){
    public void afterReturnAspect(){
//        aaa.toLowerCase(Locale.ROOT);
        System.out.println(">>>>>>>>>>>");
    }

//    @AfterThrowing(value = "execution (* *.*(..))", throwing = "ex")
    public void execptionAspect(JoinPoint joinPoint, Exception ex){
        System.out.println("方法[" + joinPoint.getSignature().getName() + "]发生异常");
        System.out.println("0000000000000");
    }

//    @After("execution (public int com.spring.aop.Calculator.*(int, int))")
    public void afterAspect(){
        System.out.println("---------------->");
    }

    /**
     * 环绕通知就是一个动态代理,
     * ProceedingJoinPoint功能很强
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @Around(value = "execution (* *.*(..))")
    public int aroundAspect(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        System.out.println("前置环绕通知");
        //就是通过反射调用方法执行，method.invoke(args)
        int result = -1;
        try{
            //@Before
            result = (int) joinPoint.proceed(args);
            //@AfterReturning
        }catch (Exception e){
            //@AfterThrowing
        }finally {
            //@After()
        }

        System.out.println("后置环绕通知");
        return result;
    }

    //抽取公共匹配规则
//    @Pointcut(value = "execution (public int com.spring.aop.Calculator.*(int, int))")
    public void comAspect(){}

}
