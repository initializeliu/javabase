package test06;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 线程池工厂方法
 * 除了使用ThreadPoolExecutor的构造方法创建线程池外，我们也可以使用Executors提供的工厂方法来创建不同类型的线程池
 * 可通过构造方法具体了解
 */
public class ExecutorServiceMain {
    public static void main(String[] args) throws Exception {

        /**
         * 可以看到，通过newFixedThreadPool创建的是一个固定大小的线程池，大小由nThreads参数指定，它具有如下几个特点:
         * 因为corePoolSize和maximumPoolSize的值都为nThreads，所以线程池中线程数量永远等于nThreads，不可能新建除了核心线程数的线程来处理任务，即keepAliveTime实际上在这里是无效的。
         * LinkedBlockingQueue是一个无界队列（最大长度为Integer.MAX_VALUE），所以这个线程池理论是可以无限的接收新的任务，这就是为什么上面没有指定拒绝策略的原因。
         */
        ExecutorService executorService1 = Executors.newFixedThreadPool(5);


        /**
         * 这是一个理论上无限大小的线程池：
         * 核心线程数为0，SynchronousQueue队列是没有长度的队列，所以当有新的任务提交，如果有空闲的还未超时的（最大空闲时间60秒）线程则执行该任务，否则新增一个线程来处理该任务。
         * 因为线程数量没有限制，理论上可以接收无限个新任务，所以这里也没有指定拒绝策略。
         */
        ExecutorService executorService2 = Executors.newCachedThreadPool();



        /**
         * 核心线程数和最大线程数都为
         * 1，每次只能有一个线程处理任务。
         * 2,LinkedBlockingQueue队列可以接收无限个新任务。
         */
        ExecutorService executorService3 = Executors.newSingleThreadExecutor();


        /**
         * 所以newScheduledThreadPool理论是也是可以接收无限个任务，DelayedWorkQueue也是一个无界队列。
         * 使用newScheduledThreadPool创建的线程池除了可以处理普通的Runnable任务外，它还具有调度的功能：
         */
        ScheduledExecutorService executorService4 = Executors.newScheduledThreadPool(5);

        //1.延迟指定时间后执行：
        //延迟5秒执行
//        executorService4.schedule(() -> System.out.println("hello"), 5, TimeUnit.SECONDS);

        //2.按指定的速率执行：
        //// 延迟1秒执行，然后每5秒执行一次
//        executorService4.scheduleAtFixedRate(() -> System.out.println("hello"), 1, 5, TimeUnit.SECONDS);

        //3.按指定的时延执行：
//        executorService4.scheduleWithFixedDelay(() -> System.out.println("hello"), 1, 5, TimeUnit.SECONDS);
        /**
         * scheduleAtFixedRate和scheduleWithFixedDelay没啥区别，实际它们还是有区别的：
         * scheduleAtFixedRate按照固定速率执行任务，比如每5秒执行一个任务，即使上一个任务没有结束，5秒后也会开始处理新的任务；
         * scheduleWithFixedDelay按照固定的时延处理任务，比如每延迟5秒执行一个任务，无论上一个任务处理了1秒，1分钟还是1小时，下一个任务总是在上一个任务执行完毕后5秒钟后开始执行。
         */

        /**
         * 因为这几个线程池理论是都可以接收无限个任务，所以这就有内存溢出的风险。实际上只要我们掌握了ThreadPoolExecutor构造函数7个参数的含义，我们就可以根据不同的业务来创建出符合需求的线程池。一般线程池的创建可以参考如下规则：
         * IO密集型任务：IO密集型任务线程并不是一直在执行任务，应该配置尽可能多的线程，线程池线程数量推荐设置为2 * CPU核心数；对于IO密集型任务，网络上也有另一种线程池数量计算公式：CPU核心数/(1 - 阻塞系数)，阻塞系数取值0.8~0.9，至于这两种公式使用哪一个，可以根据实际环境测试比较得出；
         * 计算密集型任务：此类型需要CPU的大量运算，所以尽可能的去压榨CPU资源，线程池线程数量推荐设置为CPU核心数 + 1。
         */

        //CPU核心数可以使用Runtime获得：
        //Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//                1, 2, 3, TimeUnit.SECONDS,
//                2, 2, 3, TimeUnit.SECONDS,
                2, 5, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), (ThreadFactory) Thread::new,
                new ThreadPoolExecutor.AbortPolicy()
        );

//        threadPoolExecutor.execute(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                System.out.println("任务执行完毕");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

        //线程池核心线程即使是空闲状态也不会被销毁，除非使用allowCoreThreadTimeOut设置了允许核心线程超时
//        threadPoolExecutor.allowCoreThreadTimeOut(true);
        //值得注意的是，如果一个线程池调用了allowCoreThreadTimeOut(true)方法，那么它的keepAliveTime不能为0
        //5秒后任务执行完毕，核心线程处于空闲的状态。因为通过allowCoreThreadTimeOut方法设置了允许核心线程超时，
        // 所以3秒后（keepAliveTime设置为3秒），核心线程被销毁。核心线程被销毁后，线程池也就没有作用了，于是就自动关闭了。

//        Runnable r = () -> System.out.println("是否被移除");
//        threadPoolExecutor.execute(r);
//        threadPoolExecutor.remove(r);
        //可看到任务并没有被执行，已经被删除，因为唯一一个核心线程已经在执行任务了，所以后提交的这个任务被放到了线程队列里，然后通过remove方法删除。


        //默认情况下，只有当往线程池里提交了任务后，线程池才会启动核心线程处理任务。
        // 我们可以通过调用prestartCoreThread方法，让核心线程即使没有任务提交，也处于等待执行任务的活跃状态：
//        System.out.println("活跃线程数: " + threadPoolExecutor.getActiveCount());
//        threadPoolExecutor.prestartCoreThread();
//        System.out.println("活跃线程数: " + threadPoolExecutor.getActiveCount());
//        threadPoolExecutor.prestartCoreThread();
//        System.out.println("活跃线程数: " + threadPoolExecutor.getActiveCount());
//        threadPoolExecutor.prestartCoreThread();
//        System.out.println("活跃线程数: " + threadPoolExecutor.getActiveCount());

        //该方法返回boolean类型值，如果所以核心线程都启动了，返回false，反之返回true。
        //还有一个和它类似的prestartAllCoreThreads方法，它的作用是一次性启动所有核心线程，让其处于活跃地等待执行任务的状态。
        //ThreadPoolExecutor的invokeAny方法用于随机执行任务集合中的某个任务，并返回执行结果，该方法是同步方法：

        List<Callable<Integer>> tasks = IntStream.range(0,4).boxed().map(i -> (Callable<Integer>)() ->{
           TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
            return i;
        }).collect(Collectors.toList());

        // 随机执行结果
//        Integer result = threadPoolExecutor.invokeAny(tasks);
//        System.out.println("-------------------");
//        System.out.println(result);

        //ThreadPoolExecutor的invokeAll则是执行任务集合中的所有任务，返回Future集合：
        List<Future<Integer>> futureList = threadPoolExecutor.invokeAll(tasks);
        futureList.stream().map(f->{
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        }).forEach(System.out::println);


        threadPoolExecutor.shutdown();

//        System.out.println("线程池为shutdown状态：" + threadPoolExecutor.isShutdown());
//        System.out.println("线程池正在关闭：" + threadPoolExecutor.isTerminating());
//        System.out.println("线程池已经关闭：" + threadPoolExecutor.isTerminated());
//        threadPoolExecutor.awaitTermination(6, TimeUnit.SECONDS);
//        System.out.println("线程池已经关闭" + threadPoolExecutor.isTerminated());


    }
}
