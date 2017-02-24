




```
package java1234.demo5;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/22.
 */
public class Indexer {

    private Integer ids[] = {1,2,3};
    private String citys[] = {"青岛","南京","上海"};
    private String descs[] = {
            "青岛是一个美丽的城市。",
            "南京是一个有文化的城市。南京是一个文化的城市南京，简称宁，是江苏省会，地处中国东部地区，长江下游，濒江近海。全市下辖11个区，总面积6597平方公里，2013年建成区面积752.83平方公里，常住人口818.78万，其中城镇人口659.1万人。[1-4] “江南佳丽地，金陵帝王州”，南京拥有着6000多年文明史、近2600年建城史和近500年的建都史，是中国四大古都之一，有“六朝古都”、“十朝都会”之称，是中华文明的重要发祥地，历史上曾数次庇佑华夏之正朔，长期是中国南方的政治、经济、文化中心，拥有厚重的文化底蕴和丰富的历史遗存。[5-7] 南京是国家重要的科教中心，自古以来就是一座崇文重教的城市，有“天下文枢”、“东南第一学”的美誉。截至2013年，南京有高等院校75所，其中211高校8所，仅次于北京上海；国家重点实验室25所、国家重点学科169个、两院院士83人，均居中国第三。[8-10] 。",
            "上海是一个繁华的城市。"
    };

    private Directory dir;

    /**
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter()throws Exception{
        //Analyzer analyzer = new StandardAnalyzer(); 有中文 就不能用标准分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    /**
     *
     * @param indexDir
     * @throws Exception
     */
    private void index(String indexDir)throws Exception{
        dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriter writer = getWriter();
        Document doc;
        for(int i = 0;i < ids.length; i++){
            doc = new Document();
            doc.add(new TextField("id", ids[i].toString(), Field.Store.YES));
            doc.add(new StringField("city", citys[i], Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.YES));
            doc.add(new NumericDocValuesField("id", ids[i]));  //添加要排序的字段
            writer.addDocument(doc);
        }
        writer.close();
    }


    public static void main(String[] args) throws Exception {
        new Indexer().index("/Users/zsq/lucene/lucene6");
    }
}
```




```
package java1234.lucenepagedemo;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/23.
 * lucene 的 分页查询有两种 1. 再查询方式  2. 利用searchAfter() 主要用于大量数据查询分页
 *
 */
public class LucenePageTest {


    /**
     * 再查询方法: 先查询所有数据, 然后去分页数据.
     * 如果数据量大, 容易内存溢出
     * @param q  搜索的关键字
     * @param pageIndex  第几页
     * @param pageSize   每页显示多少条
     * @throws IOException
     * @throws ParseException
     */
    public static void searchPage(String q, int pageIndex, int pageSize) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene6"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器

        QueryParser queryParser = new QueryParser("desc", analyzer);
        Query query = queryParser.parse(q);
        TopDocs hits = indexSearcher.search(query, 500); //返回前500条 数据已在内存中
        ScoreDoc[] scoreDocs = hits.scoreDocs;
        int start = (pageIndex - 1) * pageSize;
        int end = pageIndex * pageSize;
        int total = hits.totalHits;
        if (end > total) {
            end = total;
        }
        Document doc;
        for (int i = start; i < end; i++) {
            doc = indexSearcher.doc(scoreDocs[i].doc);
            System.out.println(doc.get("desc"));
        }
        indexReader.close();
    }


    /**
     *
     * @param q   搜索关键字
     * @param pageIndex 第几页
     * @param pageSize  每页显示多少条
     * @throws IOException
     * @throws ParseException
     */
    public static void searchPageByAfter(String q, int pageIndex, int pageSize) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene6"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器

        QueryParser queryParser = new QueryParser("desc", analyzer);
        Query query = queryParser.parse(q);

        //获取上一页的最后一个元素
        ScoreDoc LastScoreDoc = getLastScoreDoc(pageIndex, pageSize, query, indexSearcher);

        /**
         * 如果有排序 getLastScoreDoc() 和 searchAfter() 中 都必须 加入sort
         */
        //Sort sort = new Sort();
        //sort.setSort(new SortField("id", SortField.Type.INT, true));
        //TopDocs hits = indexSearcher.searchAfter(LastScoreDoc, query, pageSize, sort);


        TopDocs hits = indexSearcher.searchAfter(LastScoreDoc, query, pageSize);
        Document doc;
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("desc"));
        }
        indexReader.close();
    }

    /**
     * 返回上一次的最后一个元素
     * @param pageIndex  第几页
     * @param pageSize   每页显示多少
     * @param query
     * @param indexSearcher
     * @return
     * @throws IOException
     */
    private static ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher indexSearcher) throws IOException {
        //如果当前页是第一页则返回null
        if (pageIndex == 1) {
            return null;
        }
        int num = (pageIndex - 1) * pageSize;  // 获取上一页的数量

        //Sort sort = new Sort();
        //sort.setSort(new SortField("id", SortField.Type.INT, true));
        //TopDocs hits = indexSearcher.search(query, num, sort);  //查询返回上一页的数据

        TopDocs hits = indexSearcher.search(query, num);  //查询返回上一页的数据
        int total = hits.totalHits;
        if (num > total) {
            num = total;
        }
        return hits.scoreDocs[num-1];
    }

    public static void main(String[] args) {
        try {
            LucenePageTest.searchPage("南京", 1, 2);
            System.out.println("--------- searchAfter() ----------");
            LucenePageTest.searchPageByAfter("城市", 1, 2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}

```