package test02;

import java.lang.reflect.Field;

/**
 * String 相关操作
 */
public class StringMain {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        String s1 = new String("abc");

        System.out.println(System.identityHashCode(s1)); //1554874502

        //添加n行代码s指向的内存地址不变的情况下，使结果输出为"abcd"
        Field field = s1.getClass().getDeclaredField("value");
        field.setAccessible(true);
        field.set(s1, "abcd".toCharArray());

        System.out.println(System.identityHashCode(s1)); //1554874502
        System.out.println("s = " + s1); //s = abcd


        String s2 = new String("abc");
        System.out.println(System.identityHashCode(s2)); //1639705018
        System.out.println(s2);//abc

        System.out.println(s1);//abcd

        String s3 = "ddd";

        String s4 = new String("ddd");

        //通过这种方式创建字符串，会先向字符串常量池中查找相应字符串，找到：直接将常量池中的String对象取出赋值给s5,
        //未找到：通过new String("ddd")创建字符串对象赋值给s5,然后放到常量池中。
        String s5 = "ddd";

        //通过这种方式创建对象，并不会放入到常量池中。
        String s6 = new String("ddd");

        System.out.println(System.identityHashCode(s3));//1627674070
        System.out.println(System.identityHashCode(s4));//1360875712
        System.out.println(System.identityHashCode(s5));//1627674070
        System.out.println(System.identityHashCode(s6));//1625635731
        System.out.println(s3.getClass());//class java.lang.String
        System.out.println(s4.getClass());//class java.lang.String



        String s7 = new String("lll");
        String s8 = "lll";
        System.out.println(s7 == s8); //false
        //String对象的intern⽅法，⾸先会检查字符串常量池中是否存在"lll"，
        // 如果存在则返回该字符串引⽤，
        // 如果不存在，则把"lll"添加到字符串常量池中，并返回该字符串常量的引⽤。
        String s9 = s7.intern();
        System.out.println(s7 == s9); //false
        System.out.println(s8 == s9); //true







    }

}
