package java1234.demo3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/22.
 * lucene 查询
 */
public class SearchTest {

    private Directory dir;  //存放索引的目录

    private IndexReader indexReader;  //打开索引类

    private IndexSearcher indexSearcher;  //lucene 的查询

    @Before
    public void setUp() throws IOException {
        dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene4"));
        indexReader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(indexReader);
    }

    @After
    public void tearDown() throws IOException {
        indexReader.close();
    }

    /**
     * TermQuery
     * 是 lucene 支持的最为基本的一个查询类，它的构造函数只接受一个参数，那就是一个 Term 对象
     * @throws IOException
     */
    @Test
    public void testTermQuery() throws IOException {
        String searchField = "contents";
        String q = "particular";
        Term term = new Term(searchField, q);
        //Query是 TermQuery 的父类
        Query query = new TermQuery(term);
        TopDocs hits = indexSearcher.search(query, 10); //返回前10条
        System.out.println("查询到的结果是:"+hits.totalHits+"条");
        Document doc;
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("文件路径名:"+doc.get("fullPath"));
        }
    }

    /**
     * QueryParser 是查询分析器
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public void testQueryParser() throws ParseException, IOException {
        Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
        String searchField = "contents";
        String q = "abc~";
        //查询分析器
        QueryParser queryParser = new QueryParser(searchField, analyzer);
        Query query = queryParser.parse(q);
        TopDocs hits = indexSearcher.search(query, 100);
        System.out.println("查询结果是:"+hits.totalHits);
        Document doc;
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("fullPath"));
        }
    }


}
