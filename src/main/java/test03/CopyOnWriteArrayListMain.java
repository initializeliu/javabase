package test03;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 1. ⾸先CopyOnWriteArrayList内部也是⽤过数组来实现的，在向CopyOnWriteArrayList添加元素时，
 * 会复制⼀个新的数组，写操作在新数组上进⾏，读操作在原数组上进⾏
 * 2. 并且，写操作会加锁，防⽌出现并发写⼊丢失数据的问题
 * 3. 写操作结束之后会把原数组指向新数组
 * 4. CopyOnWriteArrayList允许在写操作时来读取数据，⼤⼤提⾼了读的性能，因此适合读多写少的应⽤场景，
 * 但是CopyOnWriteArrayList会⽐较占内存，同时可能读到的数据不是实时最新的数据，所以不适合实时性要求很⾼的场景
 */
public class CopyOnWriteArrayListMain {

    public static void main(String[] args) {

        CopyOnWriteArrayList list = new CopyOnWriteArrayList();

        //使用ReentrantLock
        list.add("zhangsan");
        list.remove(1);
        list.set(1, "lisi");

        //查询没有使用锁
        list.get(1);


    }
}
