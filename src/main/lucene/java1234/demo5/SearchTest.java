package java1234.demo5;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/22.
 * 中文分词  和 高亮显示
 */
public class SearchTest {

    public static void search(String indexDir, String q) throws IOException, ParseException, InvalidTokenOffsetsException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器
        String searchField = "desc";
        //查询分析器
        QueryParser queryParser = new QueryParser(searchField, analyzer);
        Query query = queryParser.parse(q);
        //TopDocs hits = indexSearcher.search(query, 10);

        // lucene 分页
        TopScoreDocCollector results = TopScoreDocCollector.create(10);// 结果集
        indexSearcher.search(query, results);// 查询前100条
        TopDocs hits = results.topDocs(1, 2);// 从结果集中第1条开始取2条


        System.out.println("命中:"+hits.totalHits);

        //高亮显示
        QueryScorer queryScorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(formatter, queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        Document doc;
        TokenStream tokenStream;
        for(ScoreDoc scoreDoc : hits.scoreDocs){
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("city"));
            //System.out.println(doc.get("desc"));
            String desc = doc.get("desc");
            if(desc != null){
                tokenStream = analyzer.tokenStream("desc", new StringReader(desc));
                System.out.println(highlighter.getBestFragment(tokenStream, desc));
            }
        }
        indexReader.close();
    }

    public static void main(String[] args) {
        String indexDir = "/Users/zsq/lucene/lucene6";
        String q = "南京";
        try {
            search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
