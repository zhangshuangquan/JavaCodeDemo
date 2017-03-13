package demo.multiDir;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zsq on 2017/3/13.
 * 多线程创建索引 (用lucene6 通过多线程  创建多个目录多个索引时, 会出现把多个索引都写到同一个目录下, 导致测试失败 )
 */
public class MultiThreadIndexTest {

    /**
     * 创建了5个线程同时创建索引
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 5;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(threadCount);
        for(int i = 0; i < threadCount; i++) {
            Runnable runnable = new IndexCreator("/Users/zsq/lucene/dir_" + (i+1), "/Users/zsq/lucene/multiIndexDir_" + (i+1), threadCount,
                    countDownLatch1, countDownLatch2);
            //子线程交给线程池管理
            pool.execute(runnable);
        }

        countDownLatch1.countDown();
        System.out.println("开始创建索引");
        //等待所有线程都完成
        countDownLatch2.await();
        //线程全部完成工作
        System.out.println("所有线程都创建索引完毕");
        //释放线程池资源
        pool.shutdown();
    }
}
