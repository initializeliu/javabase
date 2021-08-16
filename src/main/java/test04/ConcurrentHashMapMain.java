package test04;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapMain {

    public static void main(String[] args) {

        /**
         * ConcurrentHashMap的扩容机制
         * 1.7版本
         * 1. 1.7版本的ConcurrentHashMap是基于Segment分段实现的
         * 2. 每个Segment相对于⼀个⼩型的HashMap
         * 3. 每个Segment内部会进⾏扩容，和HashMap的扩容逻辑类似
         * 4. 先⽣成新的数组，然后转移元素到新数组中
         * 5. 扩容的判断也是每个Segment内部单独判断的，判断是否超过阈值
         *
         * 1.8版本
         * 1. 1.8版本的ConcurrentHashMap不再基于Segment实现
         * 2. 当某个线程进⾏put时，如果发现ConcurrentHashMap正在进⾏扩容那么该线程⼀起进⾏扩容
         * 3. 如果某个线程put时，发现没有正在进⾏扩容，则将key-value添加到ConcurrentHashMap中，然后判断是否超过阈值，超过了则进⾏扩容
         * 4. ConcurrentHashMap是⽀持多个线程同时扩容的
         * 5. 扩容之前也先⽣成⼀个新的数组
         * 6. 在转移元素时，先将原数组分组，将每组分给不同的线程来进⾏元素的转移，每个线程负责⼀组或 多组的元素转移⼯作
         */

        ConcurrentHashMap map = new ConcurrentHashMap();

        map.put("name", "zhangsan");

        /**
         * jdk7:
         * 数据结构：ReentrantLock+Segment+HashEntry, 一个Segment中包含一个HashEntry数组，每个HashEntry又是一个链表结构
         * 元素查询：二次hash,第一次Hash定位到Segment,第二次Hash定位到元素所在的链表的头部
         * 锁：Segment分段锁，Segment继承了ReentrantLock,锁定操作的Segment,其他的Segment不受影响，并发度为segment个数，可以通过构造函数指定，数组扩容不会影响其他的Segment.
         * get方法无需加锁，volatile保证
         */
        /**
         * jdk8:
         * 数据结构：synchronized + CAS(乐观锁) + Node + 红黑树， Node的val和next都用volatile修饰，保证可见性。
         * 查找，替换，赋值操作都使用CAS
         * 锁：锁链表的head节点，不影响其它元素的读写，锁粒度更细，效率更高，扩容时，阻塞所有的读写操作，并发扩容
         * 读操作无锁：
         * Node的val和next使用volatile修饰，读写线程对该变量互相可见
         * 数组用volatile修饰，保证扩容时被读线程感知。
         *
         */


    }


}
