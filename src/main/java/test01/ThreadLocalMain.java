package test01;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal 局部线程变量，每个线程通过ThreadLocal的get和set方法来访问和修改线程自己独有的变量。
 * ThreadLocal的作用就是为每一个线程提供了一个独立的变量副本，每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
 */
public class ThreadLocalMain {

    private static Person person = new Person("llll", "008", 33);
    private static Person person2 = new Person("dddd", "009", 66);

    private static ThreadLocal<Person> threadLocal = new ThreadLocal<Person>(){
//        @Override
//        protected String initialValue() {
//            return "张三";
//        }


        @Override
        protected Person initialValue() {
//            return new Person("张三", "001", 22);
            return person;
        }
    };

    private static ThreadLocal<Person> threadLocal2 = new ThreadLocal<Person>(){
        @Override
        protected Person initialValue() {
//            return new Person("张三", "001", 22);
            return person2;
        }
    };

    private static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
//        threadLocal.set("mrbird");
//        threadLocal.set("my");
        //结果证明:后设置的值覆盖先设置的值
        //my
//        System.out.println(threadLocal.get());
//        threadLocal.remove();
        //结果证明：初始值无法被覆盖
        //张三
//        System.out.println(threadLocal.get());


//        Person person = new Person("maliu", "004", 25);

        Thread thread1 = new Thread(() -> {
//            threadLocal.set(new Person("李四", "002", 23));
            threadLocal.set(person);
            threadLocal2.set(person2);

            try {
                person.setAge(44);
                person2.setAge(77);
                TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
                System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get() + " == " + threadLocal2.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread1");

        Thread thread2 = new Thread(() -> {
//            threadLocal.set(new Person("王五", "003", 24));
            threadLocal.set(person);

            try {
                person.setAge(55);
                TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
                System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get() + " == " + threadLocal2.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread2");

        //结果证明了ThreadLocal在每个线程间是相互独立的，threadLocal在thread1、thread2和main线程间都有一份独立拷贝。
        /**
         * thread1 --> thread1 : Person{name='llll', Id='008', age=55}
         * thread2 --> thread2 : Person{name='llll', Id='008', age=55}
         * main main : Person{name='llll', Id='008', age=55}
         */

        //结果证明：ThreadLocalMap是ThreadLocal中的静态类，Thread中含有ThreadLocalMap实例，本质是一个Map<ThreadLocal, ThreadLocal对应在当前线程中的值>
        //ThreadLocalMap与Thread是多对多关系，不存在绑定关系。
        /**
         * thread2 --> thread2 : Person{name='llll', Id='008', age=55} == thread2 : Person{name='dddd', Id='009', age=77}
         * thread1 --> thread1 : Person{name='llll', Id='008', age=55} == thread1 : Person{name='dddd', Id='009', age=77}
         * main main : Person{name='llll', Id='008', age=55}
         */
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println(Thread.currentThread().getName() + " " + threadLocal.get());
    }

}
