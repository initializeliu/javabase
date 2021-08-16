package test03;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 1. ⾸先，他们的底层数据结构不同，ArrayList底层是基于数组实现的，LinkedList底层是基于链表实 现的
 *      class ArrayList{
 *          transient Object[] elementData;
 *          private int size;
 *      }
 *      class LinkedList{
 *          transient int size = 0;
 *          transient Node<E> first;
 *          transient Node<E> last;
 *      }
 * 2. 由于底层数据结构不同，他们所适⽤的场景也不同，ArrayList更适合随机查找，LinkedList更适合 删除和添加，查询、添加、删除的时间复杂度不同
 * 3. 另外ArrayList和LinkedList都实现了List接⼝，但是LinkedList还额外实现了Deque接⼝，所以 LinkedList还可以当做队列来使⽤
 */
public class ArrayListAndLinkedList {

    public static void main(String[] args) {

        ArrayList arrayList = new ArrayList();
        LinkedList linkedList = new LinkedList();

        //增：两个效率都非常高，可惜线程不安全
        arrayList.add("zhangsan");
        arrayList.add(33);
        //linkedList使用的是尾添加
        linkedList.add("lisi");
        linkedList.add(44);

        //删
        //System.arraycopy(elementData, index+1, elementData, index, numMoved);
        //存在数组拷贝性能较低
        arrayList.remove("zhangsan");
        //删除性能很高
        linkedList.remove("lisi");

        //改
        //可以直接通过索引拿到元素
        arrayList.set(1, "wangwa");
        //需要从第一个节点一个一个找到第n个节点，修改值。
        linkedList.set(1, "zhaoliu");

        //查
        //直接通过索引拿到元素
        arrayList.get(1);
        //需要从第一个节点一个一个找到第n个节点,获取数据。
        linkedList.get(1);
        linkedList.getFirst();
        linkedList.getLast();



    }
}
