package test02;

public class IntegerMain {

    public static void main(String[] args) {
        Integer i1 = 100;
        Integer i2 = 100;
        System.out.println(i1 == i2);
        Integer i3 = 128;
        Integer i4 = 128;
        System.out.println(i3 == i4);
        /**
         * 在Interger类中，存在⼀个静态内部类IntegerCache，
         * 该类中存在⼀个Integer cache[]，
         * 并且存在⼀ 个static块，会在加载类的时候执⾏，会将-128⾄127这些数字提前⽣成Integer对象，
         * 并缓存在cache数组中，
         * 当我们在定义Integer数字时，会调⽤Integer的valueOf⽅法，
         * valueOf⽅法会判断所定义的数字是否在-128⾄127之间，
         * 如果存在则直接从cache数组中获取Integer对象，
         * 如果超过，则⽣成⼀个新的 Integer对象。
         */

    }
}
