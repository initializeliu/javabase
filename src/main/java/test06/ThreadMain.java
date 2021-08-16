package test06;

import java.util.List;
import java.util.concurrent.*;


/**
 * 对守护线程的理解？
 * 守护线程：为所有非守护线程提供服务的线程；任何一个守护线程都是整个JVM中所有非守护线程的保姆；
 * 守护线程的生死无关重要，它却依赖整个进程而运行的；什么时候没有要执行的，程序就结束了，不需要管守护线程是否在执行，就把它中断了。
 * 注意：由于守护线程的终止是自身无法控制的，因此千万不要把IO，File等重要操作逻辑分配给它，因为它不靠谱。
 *
 * 守护线程的作用？
 * 例：GC垃圾回收线程就是一个守护线程，当我们的程序中不再有任何运行的Thread，程序就不会再产生垃圾，垃圾回收器也就无事可做，所以当垃圾回收线程是JVM上
 * 仅剩的线程时，垃圾回收线程会自动离开。它始终再低级别的状态中运行，用于实施监控和管理系统中的可回收资源。
 *
 * 应用场景：1，来为其他线程提供服务支持的情况；2，或者在任何情况下，程序结束时，这个线程必须正常且立刻关闭，就可以作为守护线程来使用。
 * 反之，如果一个正在执行某个操作的线程必须要正确的关闭掉否则就会出现不好的后果的话，那么这个线程就不能是守护线程，而是用户线程。
 * 通常都是写关键的事务，比如：数据库录入或者更新，这些操作都是不能中断的。
 *
 * thread.setDaemon(true)必须在thread.start()之前设置，否则会抛出一个illegalThreadStateException。你不能把正在运行的常规线程设置为守护线程。
 *
 * 在Daemon线程中产生的新线程也是Daemon的。
 *
 * 守护线程不能用与去访问固有资源，比如：读写操作或者计算逻辑，因为他会在任何时候甚至在一个操作中间发生中断。
 *
 * Java自带的多线程框架，比如：ExecutorService,会将守护线程转换为用户线程，所以如果要使用后台线程就不能用Java的线程池。
 *
 *
 */

/**
 * 线程安全的理解？
 *
 * 不是线程安全，应该是内存安全，堆是共享内存，可以被所有线程访问
 *
 * 当多个线程访问一个对象时，如果不用额外的同步控制或其他的协调操作，调用这个对象的行为都可以获得正确的结果，我们就说这个对象是线程安全的。
 *
 * 堆是进程和线程共有的空间，分全局堆和局部堆，全局堆就是所有没有分配的空间，局部堆就是用户分配的空间。堆在操作系统对进程
 * 初始化的时候分配，运行过程也可以向系统要额外的堆，但是用完了要还给操作系统，要不然就是内存泄漏。
 *
 * 在Java中，堆是Java虚拟机所管理的内存中最大的一块，是所有线程共享的一块内存区域，在虚拟机启动时创建，堆所在的内存区域的
 * 唯一目的就是存放对象实例，几乎所有的对象实例以及数组都在这里分配内存。
 *
 * 栈是每个线程独有的，保存期运行状态和局部自动变量的。栈在线程开始的时候初始化，每个线程的栈互相独立，因此，栈是线程安全的，操作系统在切换线程
 * 的时候会自动切换栈，栈空间不需要在高级语言里面显式的分配和释放。
 *
 * 目前主流操作系统都是多任务的，即多个进程同时运行。为保证安全，每个进程只能访问分配给自己的内存空间，而不能访问别的进程，这是有操作系统保障的。
 *
 * 在每个进程的内存空间中都会有一块特殊的公共区域，通常称为堆内存。进程内的所有线程都可以访问到该区域，这就是造成问题的潜在原因。
 *
 */

/**
 * sleep(), wait(), join(), yield()的区别
 *
 * 1，锁池
 * 所有需要竞争同步锁的线程都会放在锁池当中，比如当前对象的锁已经被其中一个线程得到，则其他线程需要在这个锁池进行等待，当前面的线程释放同步锁后
 * 池中的线程去竞争同步锁，当某个线程得到后进入就绪队列进行等待cpu资源分配。
 * 2，等待池
 * 当我们调用wait()方法后，线程会放到等待池当中，等待池的线程是不会去竞争同步锁。只有调用notify()或notifyAll()后等待池的线程才会开始去竞争锁，
 * notify()是随机从等待池中选出一个线程放到锁池，而notifyAll()是将等待池的所有线程放到锁池当中。
 *
 * 1，sleep是Thread类的静态本地方法，wait则是Object类的本地方法
 * 2，sleep方法不会释放lock，但是wait会释放，而且会加入到等待队列中。
 *      - sleep就是把cpu的执行资格和执行全释放出去，不再运行此线程，当定时时间结束在取回cpu资源，参与cpu的调度，获取到cpu资源后就可以继续运行了，
 *      而如果sleep时该线程有锁，那么sleep不会释放这个锁，而是把锁带着进入冻结状态，也就是说其他需要这个锁的线程根本不可能获取到这个锁，也就是说无法执行程序，
 *      如果在睡眠期间其他线程调用了这一个线程的interrupt方法，那么这个线程也会抛出interruptexception异常返回，这点和wait是一样的。
 *
 * 3，sleep方法不依赖于同步器synchronized, 但是wait需要依赖synchronized关键字。
 * 4，sleep不需要被唤醒（休眠之后推出阻塞），但是wait需要（不指定时间需要被别人中断）。
 * 5，sleep一般用于当前线程休眠，或者轮询暂停操作，wait则多用于线程之间的通信。
 * 6，sleep会让出CPU执行时间且强制上下文切换，而wait则不一定，wait后可能还有机会重新竞争到锁继续执行。
 *
 * yied()执行后线程直接进入就绪状态，马上释放cpu的执行权，但是依然保留了cpu的执行资格，所以有可能cpu下次进行线程调度还会让这个线程获取到执行权继续执行。
 *
 * join()执行后线程进入阻塞状态，例如在线程B中调用线程A的join(),那线程B会进入到阻塞队列，直到线程A结束中断线程。
 *
 */

/**
 * 线程的生命周期？线程有几种状态
 * 1，线程通常有五种状态：创建，就绪，运行，阻塞和死亡状态。
 * 2，阻塞状态的情况又分为三种：
 * （1）等待阻塞：运行的线程执行wait()，该线程会释放占用的所有资源，JVM会把该线程放入"等待池"中，进入这个状态后，是不能自动唤醒的，
 *      必须依靠其他线程调用notify或notifyAll()才能被唤醒，wait是object类的方法
 * （2）同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入到"锁池"中。
 * （3）其他阻塞：运行线程执行sleep或join()，或者发出I/O请求时，JVM会把该线程置为阻塞状态。当sleep状态超时，join等待线程终止或超时，或者I/O处理完毕时，
 *      线程重新转入就绪状态。sleep是Thread类的方法。
 *
 * 1，新建：新创建一个线程对象
 * 2，就绪：线程对象创建后，其他线程调用了该对象的start方法。该状态的线程位于可运行线程池中，变得可运行，等待获取cpu的使用权
 * 3，运行：就绪状态的线程获取cpu，执行程序代码。
 * 4，阻塞：是线程因为某种原因放弃cpu的使用权，暂时停止运行，直到线程进入就绪状态，才有机会转到运行状态。
 * 5，死亡：线程执行完了或者因异常退出run方法，该线程结束生命周期。
 */

/**
 * 为什么要使用线程池？解释线程池参数？
 * 1.降低资源消耗，提高线程利用率，降低创建和销毁线程的消耗。
 * 2.提高响应速度；任务来了，直接有线程可用可执行，而不是先创建线程，再执行。
 * 3.提高线程的可管理性；线程是稀缺资源，使用线程池可以同一分配调优监控
 *      - corePoolSize:代表核心线程数，正常工作情况下创建的线程数，这些线程创建后并不会消除，而是一种常驻线程。
 *      - maxinumPoolSize:代表最大线程数，表示最大允许被创建的线程数，比如当前任务较多，将核心线程都用完了，还无法满足需求时，
 *              此时就会创建新的线程，但是线程池内线程总数不会超过最大线程数。
 *      - keepAliveTime, unit:表示超出核心线程之外的线程的空闲存活时间，也就是核心线程不会消除，
 *              但是超出核心线程数的部分线程如果空闲一定的时间则会被消除，可以通过setKeepAliveTime来设置空闲时间。
 *      - workQueue: 用来存方等待执行的任务，假设核心线程都已被使用，还有任务进来则全部放入到队列，直到整个队列被放满但任务
 *              还再继续则会开始创建新的线程。
 *      - ThreadFactory: 实际上是一个线程工厂，用来生产线程执行任务，可以使用默认的创建工厂，
 *              产生的线程都在同一个组内，拥有相同的优先级，且都不是守护线程，也可以选择自定以线程工厂，一般我们会根据业务来定制不同的线程工厂
 *      - Handler: 任务拒绝策略，有两种情况，第一种是当我们调用shutdown等方法关闭线程池后，这时候即使线程池内还有没执行完的
 *              任务正在执行，但是由于线程池已经关闭，我们再继续向线程池提交任务就会遭到拒绝。另一种情况是当达到了最大线程数，
 *              线程池已经没有能力继续处理新提交的任务时，这时也拒绝。
 *
 */

/**
 * 线程池的底层⼯作原理 线程池内部是通过队列+线程实现的，当我们利⽤线程池执⾏任务时：
 * 1. 如果此时线程池中的线程数量⼩于corePoolSize，即使线程池中的线程都处于空闲状态，也要创建 新的线程来处理被添加的任务。
 * 2. 如果此时线程池中的线程数量等于corePoolSize，但是缓冲队列workQueue未满，那么任务被放⼊ 缓冲队列。
 * 3. 如果此时线程池中的线程数量⼤于等于corePoolSize，缓冲队列workQueue满，并且线程池中的数量⼩于maximumPoolSize，建新的线程来处理被添加的任务。
 * 4. 如果此时线程池中的线程数量⼤于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等 于maximumPoolSize，那么通过 handler所指定的策略来处理此任务。
 * 5. 当线程池中的线程数量⼤于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被 终⽌。这样，线程池可以动态的调整池中的线程数
 */
public class ThreadMain {
    public static void main(String[] args) throws InterruptedException {


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//                1, 2, 10,
//                2, 4, 10,
                2,3, 10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), (ThreadFactory) Thread::new,
                //四种拒绝策略：
                //CallerRunsPolicy策略：由调用线程处理该任务：
//                new ThreadPoolExecutor.CallerRunsPolicy());
                //AbortPolicy策略：丢弃任务，并抛出RejectedExecutionException异常
//                new ThreadPoolExecutor.AbortPolicy());
                //DiscardOldestPolicy策略：丢弃最早被放入到线程队列的任务，将新提交的任务放入到线程队列末端：
//                new ThreadPoolExecutor.DiscardOldestPolicy());
                //DiscardPolicy策略：直接丢弃新的任务，不抛异常：
                new ThreadPoolExecutor.DiscardPolicy());

        System.out.println("线程池创建完毕");

        //ThreadPoolExecutor的execute和submit方法都可以向线程池提交任务，区别是，submit方法能够返回执行结果，返回值类型为Future

//        threadPoolExecutor.execute(() -> sleep(100));
//        threadPoolExecutor.execute(() -> sleep(5));
//        threadPoolExecutor.execute(() -> sleep(5));
//        threadPoolExecutor.execute(() -> sleep(5));
        //如果申请线程大于最大线程数，抛出：RejectedExecutionException，因为我们设置的拒绝策略为AbortPolicy，所以最后提交的那个任务直接被拒绝了



//        Future future = threadPoolExecutor.submit(() -> sleep(10));
//        try {
//            System.out.println(future);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        int activeCount = -1;
//        int queueSize = -1;
//        while (true) {
//            if (activeCount != threadPoolExecutor.getActiveCount()
//                    || queueSize != threadPoolExecutor.getQueue().size()) {
//                System.out.println("活跃线程个数 " + threadPoolExecutor.getActiveCount());
//                System.out.println("核心线程个数 " + threadPoolExecutor.getCorePoolSize());
//                System.out.println("队列线程个数 " + threadPoolExecutor.getQueue().size());
//                System.out.println("最大线程数 " + threadPoolExecutor.getMaximumPoolSize());
//                System.out.println("------------------------------------");
//                activeCount = threadPoolExecutor.getActiveCount();
//                queueSize = threadPoolExecutor.getQueue().size();
//            }
//        }

        /**
         * 当线程池中所有任务都处理完毕后，线程并不会自己关闭。我们可以通过调用shutdown和shutdownNow方法来关闭线程池。两者的区别在于：
         * shutdown方法将线程池置为shutdown状态，拒绝新的任务提交，但线程池并不会马上关闭，而是等待所有正在执行的和线程队列里的任务都执行完毕后，线程池才会被关闭。所以这个方法是平滑的关闭线程池。
         * shutdownNow方法将线程池置为stop状态，拒绝新的任务提交，中断正在执行的那些任务，并且清除线程队列里的任务并返回。所以这个方法是比较“暴力”的。
         */

//        threadPoolExecutor.execute(new LongTask());
//        threadPoolExecutor.execute(new ShortTask());
//        threadPoolExecutor.execute(new ShortTask());
//        threadPoolExecutor.execute(new LongTask());


        threadPoolExecutor.execute(new LongTask("任务1"));
        threadPoolExecutor.execute(new LongTask("任务2"));
        threadPoolExecutor.execute(new LongTask("任务3"));
        threadPoolExecutor.execute(new LongTask("任务4"));
        threadPoolExecutor.execute(new LongTask("任务5"));
        /**
         * CallerRunsPolicy策略
         * Thread-1执行LongTask-任务2完毕
         * Thread-0执行LongTask-任务1完毕
         * main执行LongTask-任务5完毕
         * Thread-2执行LongTask-任务4完毕
         * Thread-1执行LongTask-任务3完毕
         */
        /**
         * 可以看到最后提交的任务被执行了，而第3个任务是第一个被放到线程队列的任务，被丢弃了。
         * Thread-0执行LongTask-任务1完毕
         * Thread-1执行LongTask-任务2完毕
         * Thread-2执行LongTask-任务4完毕
         * Thread-0执行LongTask-任务5完毕
         */
        /**
         * DiscardPolicy策略：直接丢弃新的任务，不抛异常：
         * 第5个任务直接被拒绝丢弃了，而没有抛出任何异常。
         * Thread-0执行LongTask-任务1完毕
         * Thread-2执行LongTask-任务4完毕
         * Thread-1执行LongTask-任务2完毕
         * Thread-2执行LongTask-任务3完毕
         */


//        threadPoolExecutor.shutdown();
//        System.out.println("已经执行了线程池shutdown方法");

        //在执行shutdownNow方法后，线程池马上就被关闭了，正在执行中的任务被打断，并且返回了线程队列中等待被执行的任务。
//        List<Runnable> runnables = threadPoolExecutor.shutdownNow();
//        System.out.println(runnables);
//        System.out.println("已经执行了线程池shutdownNow方法");

        /**
         * shortTask执行过程中被打断sleep interrupted
         * LongTask执行过程中被打断sleep interrupted
         * shortTask执行过程中被打断sleep interrupted
         */
        //通过上面两个例子我们还可以看到shutdown和shutdownNow方法都不是阻塞的。常与shutdown搭配的方法有awaitTermination。
        //awaitTermination方法接收timeout和TimeUnit两个参数，用于设定超时时间及单位。
        // 当等待超过设定时间时，会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false。该方法是阻塞的：
        threadPoolExecutor.shutdown();
//        boolean isShutDown = threadPoolExecutor.awaitTermination(3, TimeUnit.SECONDS);
//        if(isShutDown){
//            System.out.println("线程池在3秒内成功关闭");
//        }else{
//            System.out.println("等了3秒还没关闭，不等了╰（‵□′）╯");
//        }

    }

    static class ShortTask implements Runnable{

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + "执行shortTask完毕");
            } catch (InterruptedException e) {
                System.err.println("shortTask执行过程中被打断" + e.getMessage());
            }
        }
    }

    static class LongTask implements Runnable{

        private String name;

        public LongTask(String name){
            this.name = name;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "执行LongTask-" + name + "完毕");
            } catch (InterruptedException e) {
                System.err.println("LongTask执行过程中被打断" + e.getMessage());
            }
        }
    }

    public static void sleep(Integer time){

        try {
            System.out.println(Thread.currentThread().getName() + "线程执行sleep方法");
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
