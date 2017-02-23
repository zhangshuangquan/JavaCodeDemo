package java1234.demo4;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/22.
 * lucene 查询方式
 *
 */
public class SearchTest {

    private Directory dir;

    private IndexReader indexReader;

    private IndexSearcher indexSearcher;

    @Before
    public void setUp() throws IOException {
        dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene5"));
        indexReader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(indexReader);
    }

    @After
    public void tearDown() throws IOException {
        indexReader.close();
    }

    /**
     * TermRangeQuery 是在使用的范围内的文本的词条都被搜索。
     * TermRangeQuery(String field, String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper)
     构造一个查询选择的所有条款大于/等于比lowerTerm，但小于/等于比upperTerm。

     TermRangeQuery(String field, String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper, Collator collator)
     构造一个查询选择的所有条款大于/等于比lowerTerm，但小于/等于比upperTerm。
     * @throws IOException
     */
    @Test
    public void testTermRangeQuery() throws IOException {
        TermRangeQuery termRangeQuery = new TermRangeQuery("desc", new BytesRef("b".getBytes()), new BytesRef("c".getBytes()), true, true);
        TopDocs hits = indexSearcher.search(termRangeQuery, 10);
        Document doc;
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }

   /* //数字范围搜索
   @Test
    public void testNumericRangeQuery()throws Exception{
        NumericRangeQuery<Integer> query=NumericRangeQuery.newIntRange("id", 1, 2, true, true);
        TopDocs hits=indexSearcher.search(query, 10);
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }*/


    /**
     * 前缀搜索
     * PrefixQuery用于匹配其索引开始以指定的字符串的文档。
     * PrefixQuery(Term prefix)
     构造的开始前缀词的查询。
     * @throws IOException
     */
    @Test
    public void testPrefixQuery() throws IOException {
        PrefixQuery prefixQuery = new PrefixQuery(new Term("city", "a"));
        TopDocs hits = indexSearcher.search(prefixQuery, 10);
        Document doc;
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }

    /**
     * BooleanQuery用于搜索的是使用AND，OR或NOT运算符多个查询结果的文件。
     * @throws IOException
     */
    @Test
    public void testBooleanQuery() throws IOException {
       // NumericRangeQuery<Integer> query1=NumericRangeQuery.newIntRange("id", 1, 2, true, true);
        PrefixQuery query2 = new PrefixQuery(new Term("city","b"));
        BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
        //booleanQuery.add(query1,BooleanClause.Occur.MUST);
        booleanQuery.add(query2,BooleanClause.Occur.MUST);
        TopDocs hits = indexSearcher.search(booleanQuery.build(), 10);
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }

    // 关键词查询
    @Test
    public void testTermQuery() throws IOException {
        TermQuery query = new TermQuery(new Term("city", "changhai"));
        TopDocs hits = indexSearcher.search(query, 10);
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }

    // 通配符查询
    // ? 表示一个任意字符
    // * 表示0或多个任意字符
    @Test
    public void testWildcardQuery() throws IOException {
        // 对应的查询字符串为：title:lu*n?
        // WildcardQuery query = new WildcardQuery(new Term("title", "lu*n?"));

        // 对应的查询字符串为：content:互?网
        WildcardQuery query = new WildcardQuery(new Term("content", "互?网"));
        TopDocs hits = indexSearcher.search(query, 10);
        searchAndShowResult(hits);
    }

    // 查询所有
    @Test
    public void testMatchAllDocsQuery() throws IOException {
        // 对应的查询字符串为：*:*
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        TopDocs hits = indexSearcher.search(query, 10);
        searchAndShowResult(hits);

    }

    // 模糊查询
    @Test
    public void testFuzzyQuery() throws IOException {
        // 对应的查询字符串为：title:lucenX~0.9
        // 第二个参数是最小相似度，表示有多少正确的就显示出来，比如0.9表示有90%正确的字符就会显示出来。
        FuzzyQuery query = new FuzzyQuery(new Term("title", "lucenX"), (int) 0.8F);
        TopDocs hits = indexSearcher.search(query, 10);
        searchAndShowResult(hits);
    }

    // 范围查询
    @Test
    public void testNumericRangeQuery2() {
        // 对应的查询字符串为：id:[5 TO 15]
        // NumericRangeQuery query = NumericRangeQuery.newIntRange("id", 5, 15, true, true);

        // 对应的查询字符串为：id:{5 TO 15}
        // NumericRangeQuery query = NumericRangeQuery.newIntRange("id", 5, 15, false, false);

        // 对应的查询字符串为：id:[5 TO 15}
        //NumericRangeQuery query = NumericRangeQuery.newIntRange("id", 5, 15, true, false);

        //TopDocs hits = indexSearcher.search(query, 10);
        //searchAndShowResult(hits);
    }

    // 布尔查询
    @Test
    public void testBooleanQuery2() {
        //BooleanQuery booleanQuery = new BooleanQuery();
        // booleanQuery.add(query, Occur.MUST); // 必须满足
        // booleanQuery.add(query, Occur.SHOULD); // 多个SHOULD一起用表示OR的关系
        // booleanQuery.add(query, Occur.MUST_NOT); // 非

        //Query query1 = new TermQuery(new Term("title", "lucene"));
        //Query query2 = NumericRangeQuery.newIntRange("id", 5, 15, false, true);

        // // 对应的查询字符串为：+title:lucene +id:{5 TO 15]
        // // 对应的查询字符串为（大写的AND）：title:lucene AND id:{5 TO 15]
        // booleanQuery.add(query1, Occur.MUST);
        // booleanQuery.add(query2, Occur.MUST);

        // 对应的查询字符串为：title:lucene id:{5 TO 15]
        // 对应的查询字符串为：title:lucene OR id:{5 TO 15]
        // booleanQuery.add(query1, Occur.SHOULD);
        // booleanQuery.add(query2, Occur.SHOULD);

        // 对应的查询字符串为：+title:lucene -id:{5 TO 15]
        // 对应的查询字符串为：title:lucene (NOT id:{5 TO 15] )
       //booleanQuery.add(query1, Occur.MUST);
        //booleanQuery.add(query2, Occur.MUST_NOT);

    }

    private void searchAndShowResult(TopDocs hits) throws IOException {
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }
}
