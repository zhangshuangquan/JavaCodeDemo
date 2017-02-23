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
            LucenePageTest.searchPageByAfter("城市", 4, 2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
