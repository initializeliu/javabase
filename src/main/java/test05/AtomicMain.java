package test05;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 并发三大特性：
 * 1. 原子性
 *  指一个操作中cpu不可以在中途暂停然后再调度，不可以中断操作，要不全部执行。
 * 2. 可见性
 * 线程使用变量时，将主存中的变量值复制到线程相应缓存中，缓存中的变量发生改变，其他线程中对应变量无法收到变量改变的值。
 *
 * 3. 有序性
 *  虚拟机在进行代码编译时，对于那些改变顺序之后不会对最终结果造成影响的代码，虚拟机不一会回按照我们写的代码的顺序来执行，
 *  有可能将它们重排序，实际上，对于有些代码进行重排序后，虽然变量的值没有造成影响，但有可能出现线程安全问题。
 */
public class AtomicMain {
    public static void main(String[] args) {


        int i = 0;

        /**
         * 1. 将i从主存读到工作内存中的副本中
         * 2. +1的运算
         * 3. 将结果写入到工作内存中
         * 4. 将工作内存的值刷回主存（什么时候刷入由操作系统决定，不确定）
         */
        /**
         * 解决方案
         * 1，2，3步保证原子性
         * 4保证可见性（相关协议：总线Lock, MESI）
         */
        i++;

        /**
         * public class AtomicInteger extends Number implements java.io.Serializable {
         *      //保证可见性
         *      private volatile int value;
         *      public final boolean compareAndSet(int expect, int update) {
         *          return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
         *     }
         * }
         *
         * //调用底层c语言方法进行修改，保证原子性
         * public final class Unsafe {
         *      public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
         * }
         *
         */
        AtomicInteger integer = new AtomicInteger(99);
        AtomicBoolean bool = new AtomicBoolean();
        AtomicLong lg = new AtomicLong();


        //同时保证原子性和可见性
        //只有integer的value值是期待的值，才会进行修改。
        integer.compareAndSet(88, 10);
//        integer.getAndIncrement();
        System.out.println(integer);//99



    }

    //有序性验证
    int a = 0;
    boolean flag = false;

    public void write(){
        a = 2;              //1
        flag = true;        //2
    }

    public void multiply(){
        if(flag){           //3
            int ret = a * a;//4
        }
    }

    //write方法里的1和2做了重排序，线程1先对flag赋值为true,随后执行线程2，ret直接计算出结果，再到线程1，
    // 这时候a才赋值为2，很明显迟了一步。

    /**
     * 关键字： volatile, synchronized
     * volatile本身就包含了禁止指令重排
     *
     * volatile关键字⽤来修饰对象的属性，在并发 环境下可以保证这个属性的可⻅性，对于加了volatile关键字的属性，
     * 在对这个属性进⾏修改时，会直接将CPU⾼级缓存中的数据写回到主内存，对这个变量的读取也会直接从主内存中读取，
     * 从⽽保证了可⻅性，底层是通过操作系统的内存屏障来实现的，由于使⽤了内存屏障，所以会禁⽌指令重排，
     * 所以同时也就保证了有序性，在很多并发场景下，如果⽤好volatile关键字可以很好的提⾼执⾏效率。
     */

}
