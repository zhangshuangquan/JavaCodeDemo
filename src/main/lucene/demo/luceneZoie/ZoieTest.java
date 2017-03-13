package demo.luceneZoie;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;

import java.util.Timer;

/**
 * Created by zsq on 2017/3/13.
 * 增量索引测试
 */
public class ZoieTest {

    public static void main(String[] args) throws Exception {

        String userIndexPath = "/Users/zsq/lucene/zoieIndex";

        //处理中文的分词
        Analyzer analyzer = new SmartChineseAnalyzer();

        // personDao bean
        PersonDao personDao = new PersonDaoImpl();

        int zoieBatchSize = 10;
        int zoieBatchDelay = 1000;

        //先读取数据库表中已有数据, 并对其创建索引
        CreateIndexTest createIndexTest = new CreateIndexTest(personDao, userIndexPath);
        createIndexTest.index();

        //再往数据库表中插入一条数据, 模拟数据动态变化
        PersonDaoTest.addPerson();

        //创建 zoie增量索引类
        ZoieIndex zoindex = new ZoieIndex(userIndexPath, analyzer, personDao,
                zoieBatchSize, zoieBatchDelay);

        //创建定时任务
        Timer timer = new Timer("", false);
        timer.scheduleAtFixedRate(new ZoieIndexTimerTask(zoindex), 10L, 3000L);

        //睡眠2分钟
        //Thread.sleep(2*60*1000L);

        //2分钟后定时器取消
        //timer.cancel();
        System.out.println("Timer cancled.");

        /**把索引flush到硬盘*/
        zoindex.destroy();
        System.out.println("finished.");
    }

}
