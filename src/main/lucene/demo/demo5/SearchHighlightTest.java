package demo.demo5;

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
public class SearchHighlightTest {

    public static void search(String indexDir, String q) throws IOException, ParseException, InvalidTokenOffsetsException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器
        String searchField = "desc";

        //查询分析器
        QueryParser queryParser = new QueryParser(searchField, analyzer);
        Query query = queryParser.parse(q);

        /*
         * 排序  true 为降序排列   false 为升序排列
         *
         * 需要在添加文档的时候, 把要排序的字段添加进去, 不然报错.
         * doc.add(new NumericDocValuesField("id", ids[i]));
         * NumericDocValuesField 为专用的排序字段 数字型
         *
         * 会报错如下:
         * java.lang.IllegalStateException: unexpected docvalues type NONE for field 'id' (expected=NUMERIC). Re-index with correct docvalues type.
         *
         *
         * 按字符串排序 SortedDocValuesField
          * doc.add(new SortedDocValuesField("fileName", new BytesRef(f.getName()))); 添加排序字段
          * Sort sort = new Sort();
          * sort.setSort(new SortField("fileName", SortField.Type.STRING, false));
         */
        Sort sort = new Sort();
        sort.setSort(new SortField("id", SortField.Type.INT, true));

        TopDocs hits = indexSearcher.search(query, 10, sort);

        // lucene 分页  查不到匹配数据  建议使用 searchAfter()
        //TopScoreDocCollector results = TopScoreDocCollector.create(10);// 结果集
        //indexSearcher.search(query, results);// 查询前100条
        //TopDocs hits = results.topDocs(1, 2);// 从结果集中第1条开始取2条


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
        String q = "城市";
        try {
            search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
