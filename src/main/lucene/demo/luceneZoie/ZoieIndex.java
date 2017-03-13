package demo.luceneZoie;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import proj.zoie.api.DataConsumer;
import proj.zoie.api.ZoieException;
import proj.zoie.api.indexing.ZoieIndexableInterpreter;
import proj.zoie.impl.indexing.DefaultIndexReaderDecorator;
import proj.zoie.impl.indexing.ZoieConfig;
import proj.zoie.impl.indexing.ZoieSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsq on 2017/3/13.
 *
 * Zoie 增量索引测试
 *
 */
public class ZoieIndex {

    /**最大增量索引数量*/
    public static final long MAX_INCREMENT_INDEX_NUMBER = Long.MAX_VALUE;

    /**索引目录*/
    public String userIndexPath;

    public ZoieSystem zoieSystem;

    /**队列中放入多少项才触发索引*/
    private  int zoieBatchSize;

    /**等待多长时间才触发索引*/
    private int zoieBatchDelay;

    /**分词器*/
    private Analyzer analyzer;

    private PersonDao personDao;

    public ZoieIndex() {
        super();
    }

    public ZoieIndex(String userIndexPath, Analyzer analyzer,
                     int zoieBatchSize, int zoieBatchDelay) {
        super();
        this.userIndexPath = userIndexPath;
        this.analyzer = analyzer;
        this.zoieBatchSize = zoieBatchSize;
        this.zoieBatchDelay = zoieBatchDelay;
    }

    public ZoieIndex(String userIndexPath, Analyzer analyzer, PersonDao personDao, int zoieBatchSize, int zoieBatchDelay) {
        super();
        this.userIndexPath = userIndexPath;
        this.analyzer = analyzer;
        this.personDao = personDao;
        this.zoieBatchSize = zoieBatchSize;
        this.zoieBatchDelay = zoieBatchDelay;
    }

    public void init() throws ZoieException {
        //如果索引目录不存在则新建
        File idxDir = new File(userIndexPath);
        if(!idxDir.exists()){
            idxDir.mkdir();
        }

        //分词器设置为SmartChineseAnalyzer分词器
        analyzer = new SmartChineseAnalyzer();

        //数据转换器[JavaBea-->Document]
        ZoieIndexableInterpreter interpreter = new CustomPersonZoieIndexableInterpreter(analyzer);
        //Lucene的IndexReader装饰者，包装成zoie的IndexReader
        DefaultIndexReaderDecorator readerDecorator = new DefaultIndexReaderDecorator();

        //Zoie初始化相关配置
        ZoieConfig zoieConfig = new ZoieConfig();
        zoieConfig.setBatchDelay(zoieBatchDelay);
        zoieConfig.setBatchSize(zoieBatchSize);
        //设置分词器
        zoieConfig.setAnalyzer(analyzer);

        //设置相似性评分器   lucene6.0 没有该写法
        //zoieConfig.setSimilarity(new DefaultSimilarity());

        // 开启NRT索引
        zoieConfig.setRtIndexing(true);
        zoieSystem = new ZoieSystem(idxDir, interpreter, readerDecorator, zoieConfig);
        zoieSystem.start();
        zoieSystem.getAdminMBean().flushToDiskIndex();
    }

    /**
     * 更新索引数据
     * @throws ZoieException
     */
    public void updateIndexData() throws ZoieException {
        //先从数据库查出新增加的数据
        List<PersonZoie> persons = personDao.findPersonBefore3S();
        if(persons == null || persons.size() == 0) {
            System.out.println("No increment data right now.please wait a while.");
            return;
        }
        List<DataConsumer.DataEvent<PersonZoie>> dataEventList = new ArrayList<DataConsumer.DataEvent<PersonZoie>>();
        for(PersonZoie person : persons) {
            dataEventList.add(new DataConsumer.DataEvent<PersonZoie>(person, "1.0", person.isDeleteFlag()));
        }
        //消费数据
        zoieSystem.consume(dataEventList);
    }

    public void destroy(){
        // 将内存索引刷新到磁盘索引中
        zoieSystem.shutdown();
        System.out.println(".........将内存索引刷新到磁盘索引中.........");
    }

    public String getUserIndexPath() {
        return userIndexPath;
    }

    public void setUserIndexPath(String userIndexPath) {
        this.userIndexPath = userIndexPath;
    }

    public int getZoieBatchSize() {
        return zoieBatchSize;
    }

    public void setZoieBatchSize(int zoieBatchSize) {
        this.zoieBatchSize = zoieBatchSize;
    }

    public int getZoieBatchDelay() {
        return zoieBatchDelay;
    }

    public void setZoieBatchDelay(int zoieBatchDelay) {
        this.zoieBatchDelay = zoieBatchDelay;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }
}
