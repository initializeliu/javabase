package test07;


/**
 * 取材于：https://mrbird.cc/Java-Concurrent-Basis.html
 * 通过Thread和Runable创建线程
 *
 * 线程状态：
 * New：当我们创建一个线程时，该线程并没有纳入线程调度，其处于一个new状态。
 * Runnable：当调用线程的start方法后，该线程纳入线程调度的控制，其处于一个可运行状态，等待分配时间片段以并发运行。
 * Running：当该线程被分配到了时间片段后其被CPU运行，这是该线程处于running状态。
 * Blocked：当线程在运行过程中可能会出现阻塞现象，比如等待用户输入信息等。但阻塞状态不是百分百出现的，具体要看代码中是否有相关需求。
 * Dead：当线程的任务全部运行完毕，或在运行过程中抛出了一个未捕获的异常，那么线程结束，等待GC回收
 *
 *
 */

import java.util.concurrent.TimeUnit;

/**
 * 线程有几个不可控因素:
 * 1，cpu分配时间片给哪个线程我们说了不算。
 * 2，时间片长短也不可控。
 * 3，线程调度会尽可能均匀的将时间片分配给多个线程。
 */
public class ThreadAndRunable {

    private static volatile Boolean flag = false;

    public static void main(String[] args) {

        //1,Thread类创建
        Thread thread1 = new MyThread1();
        Thread thread2 = new MyThread2();
        /*
        第一种创建线程的方式存在两个不足：
        由于java是单继承的，这就导致我们若继承了Thread类就无法再继承其他类，这在写项目时会遇到很大问题；
        由于我们定义线程的同时重写run方法来定义线程要执行的任务，这就导致线程与任务有一个强耦合关系，线程的重用性变得非常局限。
         */

        /**
         * 启动线程要调用start方法，不能直接调用run方法。start方法会将当前线程纳入到线程调度中，使其具有并发运行的能力。
         * start方法很快会执行完毕。当start方法执行完毕后，当前线程的run方法会很快的被执行起来（只要获取到了cpu时间片）。
         * 但不能理解为调用start方法时run方法就执行了！
         */
        thread1.start();
        thread2.start();

        //2,实现Runable
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("M: are you ok?");
                }
            }
        });
        thread3.start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("N: of course");
            }
        }).start();

        Thread curThread = Thread.currentThread();      //获取当前线程：
        System.out.println(curThread.getId());          //当前线程ID
        System.out.println(curThread.getName());        //当前线程名字
        System.out.println(curThread.getPriority());    //当前线程优先级
        System.out.println(curThread.isAlive());        //当前线程是否还活着
        System.out.println(curThread.isDaemon());        //当前线程是否为守护线程
        System.out.println(curThread.isInterrupted());        //判断当前线程是否被中断

//        线程优先级分为10个等级，1最低，5默认，10最高。线程提供了3个常量：
//        MIN_PRIORITY：1 对应最低优先级；
//        MAX_PRIORITY： 10 对应最高优先级；
//        NORM_PRIORITY：5 默认优先级。


        /**
         * 线程阻塞：
         * Thread提供了一个静态方法: sleep，该方法会阻塞运行当前方法的线程指定毫秒。
         * 当超时后，线程会自动回到Runnable状态，等待再次分配时间片运行：
         */
        System.out.println("程序开始了");
        try{
            Thread.sleep(5000);
//            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("程序结束了");

        /**
         * 守护线程
         * 后台线程，又叫做守护线程，当一个进程中的所有前台线程都结束了，进程就会结束，
         * 无论进程中的其他后台线程是否还在运行，都要被强制中断：
         */
        Thread back = new Thread(() -> {
           while (true){
               System.out.println("我是守护线程");
               try{
                   TimeUnit.SECONDS.sleep(1);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        //设置守护线程一定要在线程开启前，否则设置无效
//        back.setDaemon(true);
//        back.start();

        //yield:该方法用于使当前线程主动让出当次CPU时间片回到Runnable状态，等待分配时间片。
//        Thread.yield();
        //join:允许当前线程在另一个线程上等待，直到另一个线程结束工作。通常是用来协调两个线程工作使用
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
            flag = true;
        });

        Thread show = new Thread(() -> {
            System.out.println("开始显式图片。。。");
            try{
                download.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!flag){
                throw new RuntimeException("图片加载失败。。。");
            }
            System.out.println("图片显式成功！");
        });

        download.start();
        show.start();












    }
    static class MyThread1 extends Thread{
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println("are you ok?");
            }
        }
    }
    static class MyThread2 extends Thread{
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println("of course");
            }
        }
    }
}
