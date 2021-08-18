package test07;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.TimeUnit;

public class SynchronizedMain {

    private static volatile Boolean isFlash = false;
    private static final Object obj = new Object();

    public static void main(String[] args) {
        final Table table = new Table();
        Thread thread01 = new Thread(()->{
           while(true){
               int bean = table.getBeans();
               Thread.yield();
               System.out.println(Thread.currentThread().getName() + " : " + bean);
           }
        });
        Thread thread02 = new Thread(()->{
            while(true){
                int bean = table.getBeans();
                Thread.yield();
                System.out.println(Thread.currentThread().getName() + " : " + bean);
            }
        });
//        thread01.start();
//        thread02.start();


        final Shop shop = new Shop();
        Thread t1 = new Thread(shop::buy);
        Thread t2 = new Thread(shop::buy);

//        t1.start();
//        t2.start();

        Thread download = new Thread(() -> {
            System.out.println("开始下载图片。。。。");
            for(int i = 0; i <= 100; i++) {
                System.out.println("down: " + i + "%");
                try {
                    TimeUnit.MICROSECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("图片加载完毕。。。。");
            isFlash = true;
            synchronized (obj){
                obj.notify();
                /**
                 * 若多个线程在同一个对象上调用wait方法进入阻塞状态后，那么当该对象的notify方法被调用时，
                 * 会随机解除一个线程的wait阻塞，这个不可控。若希望一次性将所有线程的wait阻塞解除，
                 * 可以调用notifyAll方法。
                 */
//                obj.notifyAll();
            }
        });

        Thread show = new Thread(() -> {
            System.out.println("开始显式图片。。。");
            synchronized (obj) {
                try {
                    /*
                    Object类中定义了两个方法wait()和notify()。
                    它们也可以实现协调线程之间同步工作的方法。
                    当一个线程调用了某个对象的wait方法时，这个线程就进入阻塞状态，
                    直到这个对象的notify方法被调用，这个线程才会解除wait阻塞，继续向下执行代码。
                     */
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!isFlash){
                throw new RuntimeException("图片加载失败。。。");
            }
            System.out.println("图片显式成功！");
        });

        download.start();
        show.start();




    }
    static class Table{
        private volatile int beans = 20;

        //如果不加synchronized关键字，可能beans为负数了，线程还在执行，成了死循环。
        synchronized int getBeans(){
            if (beans == 0){
                throw new RuntimeException("没有豆子....");
            }
            Thread.yield();
            return beans--;
        }
    }

    static class Shop{
        void buy(){
            try{
                Thread t = Thread.currentThread();
                System.out.println("正在挑选衣服");
                Thread.sleep(1000);
                synchronized (this){
                    System.out.println("正在试衣服");
                    Thread.sleep(1000);
                }
                System.out.println("结账离开");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
