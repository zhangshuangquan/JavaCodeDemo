package demo.demo1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/21.
 * lucene 的查询
 */
public class Searcher {


    public static void main(String[] args) {
        String indexDir = "/Users/zsq/lucene/index";
        String q = "lucene";
        try {
            search(indexDir, q);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关键字查询
     * @param indexDir
     * @param q
     */
    private static void search(String indexDir, String q) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));  //获取索引存放的位置
        IndexReader indexReader = DirectoryReader.open(dir);  // 读索引对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader); //索引查询对象
        Analyzer analyzer = new StandardAnalyzer(); //建立标准分词器
        QueryParser queryParser = new QueryParser("contents", analyzer); //查询分析器
        Query query = queryParser.parse(q);  //对关键字的解析
        long start = System.currentTimeMillis();

        //排序
        Sort sort = new Sort();
        sort.setSort(new SortField("fileName", SortField.Type.STRING, false));
        TopDocs hits = indexSearcher.search(query, 10, sort);

        //TopDocs hits = indexSearcher.search(query, 10);  //查询到的结果,返回前10
        long end = System.currentTimeMillis();
        System.out.println("查询关键字:"+q+" 消耗时间:"+(end-start)+" ms "+"命中个数:"+hits.totalHits);
        Document doc;
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
             doc = indexSearcher.doc(scoreDoc.doc);
             System.out.println(doc.get("fullPath"));
        }
        indexReader.close();
    }
}
