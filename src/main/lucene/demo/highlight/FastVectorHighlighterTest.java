package demo.highlight;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Encoder;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.vectorhighlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zsq on 2017/3/17.
 *
 * 快速 高亮 测试
 */
public class FastVectorHighlighterTest {

    public static void main(String[] args) throws Exception {
         //testSimpleHighlightTest();
         //testPhraseHighlightLongTextTest();
         testFormater();
    }

    /**
     * 快速高亮器第一个简单测试
     *
     * @throws IOException
     */
    public static void testSimpleHighlightTest() throws IOException {
        Directory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
                new StandardAnalyzer()));

        Document doc = new Document();

        FieldType type = new FieldType(TextField.TYPE_STORED);
        type.setStoreTermVectorOffsets(true);
        type.setStoreTermVectorPositions(true);
        type.setStoreTermVectors(true);
        type.freeze();

        Field field = new Field("field",  "This is a test where foo is highlighed and should be highlighted",  type);
        doc.add(field);
        writer.addDocument(doc);

        FastVectorHighlighter highlighter = new FastVectorHighlighter();
        IndexReader reader = DirectoryReader.open(writer);
        int docId = 0;
        FieldQuery fieldQuery = highlighter.getFieldQuery(new TermQuery(
                new Term("field", "foo")), reader);

        /**
         * 测试高亮段显示字符最大长度的影响
         */
        String[] bestFragments = highlighter.getBestFragments(fieldQuery,
                reader, docId, "field", 54, 1);
        System.out.println(bestFragments[0]);

        bestFragments = highlighter.getBestFragments(fieldQuery, reader, docId,
                "field", 52, 1);
        System.out.println(bestFragments[0]);

        bestFragments = highlighter.getBestFragments(fieldQuery, reader, docId,
                "field", 30, 1);
        System.out.println(bestFragments[0]);
        reader.close();
        writer.close();
        dir.close();
    }

    public static void testPhraseHighlightLongTextTest() throws IOException {
        Directory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
                new StandardAnalyzer()));

        Document doc = new Document();
        FieldType type = new FieldType(TextField.TYPE_STORED);
        type.setStoreTermVectorOffsets(true);
        type.setStoreTermVectorPositions(true);
        type.setStoreTermVectors(true);
        type.freeze();

        Field text = new Field("text",
                "Netscape was the general name for a series of web browsers originally produced by Netscape Communications Corporation, now a subsidiary of AOL The original browser was once the dominant browser in terms of usage share, but as a result of the first browser war it lost virtually all of its share to Internet Explorer Netscape was discontinued and support for all Netscape browsers and client products was terminated on March 1, 2008 Netscape Navigator was the name of Netscape\u0027s web browser from versions 1.0 through 4.8 The first beta release versions of the browser were released in 1994 and known as Mosaic and then Mosaic Netscape until a legal challenge from the National Center for Supercomputing Applications (makers of NCSA Mosaic, which many of Netscape\u0027s founders used to develop), led to the name change to Netscape Navigator The company\u0027s name also changed from Mosaic Communications Corporation to Netscape Communications Corporation The browser was easily the most advanced...",
                type);
        doc.add(text);
        writer.addDocument(doc);

        FastVectorHighlighter highlighter = new FastVectorHighlighter();
        IndexReader reader = DirectoryReader.open(writer);
        int docId = 0;
        String field = "text";

        {
            // 构造PhraseQuery时添加的两个Term之间是没有间隙，是连在一起的，且两者在原文中也是连在一起的，
            // 所以高亮时也是当作一个整体进行高亮的,这是普通高亮器实现不了的
            PhraseQuery query = new PhraseQuery(0, field, "internet", "explorer");

            FieldQuery fieldQuery = highlighter.getFieldQuery(query, reader);
            String[] bestFragments = highlighter.getBestFragments(fieldQuery,
                    reader, docId, field, 128, 1);
            System.out.println(bestFragments.length);
            System.out.println(bestFragments[0]);
        }
        reader.close();
        writer.close();
        dir.close();
    }

    public static void testFormater() throws IOException, ParseException {
        Directory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
                new StandardAnalyzer()));
        Document doc = new Document();
        FieldType type = new FieldType(TextField.TYPE_STORED);
        type.setStoreTermVectorOffsets(true);
        type.setStoreTermVectorPositions(true);
        type.setStoreTermVectors(true);
        type.freeze();
        Field field = new Field("field", "This is a test where foo is highlighed&<underline> and should be \"highlighted\".",
                type);

        doc.add(field);
        writer.addDocument(doc);


        //自定义高亮标签，默认为<B></B>
        String[] preTags = new String[] { "<font color=\"#0000FF\">","<strong>" };
        String[] postTags = new String[] { "</font>","</strong>" };


        FragListBuilder fragListBuilder = new SimpleFragListBuilder();
        FragmentsBuilder fragmentsBuilder = new ScoreOrderFragmentsBuilder(preTags,postTags);
        //创建快速高亮器
        FastVectorHighlighter highlighter = new FastVectorHighlighter(true,true,fragListBuilder,fragmentsBuilder);

        // 特殊字符编码器
        Encoder encoder = new SimpleHTMLEncoder();

        IndexReader reader = DirectoryReader.open(writer);

		/*PhraseQuery query = new PhraseQuery();
		query.add(new Term("field", "test"));
		query.add(new Term("field", "foo"));
		query.setSlop(2);*/
        QueryParser queryParser = new QueryParser("field",new StandardAnalyzer());
        Query query = queryParser.parse("test foo");
        System.out.println(query.toString());
        FieldQuery fieldQuery = highlighter.getFieldQuery(query, reader);
        int docId = 0;
        // matchedFields对哪些域进行高亮，添加多个域即可以对多个域进行高亮
        Set<String> matchedFields = new HashSet<String>();
        matchedFields.add("field");
        String[] bestFragments = highlighter.getBestFragments(fieldQuery,
                reader, docId, "field", matchedFields, 100, 1, fragListBuilder,
                fragmentsBuilder, preTags, postTags, encoder);
        System.out.println(bestFragments[0]);

        reader.close();
        writer.close();
        dir.close();
    }

    private static Query clause(String field, String... terms) {
        return clause(field, 1, terms);
    }

    private static Query clause(String field, float boost, String... terms) {
        Query q;
        if (terms.length == 1) {
            q = new TermQuery(new Term(field, terms[0]));
        } else {
            PhraseQuery pq = new PhraseQuery(0, field, terms);
            q = pq;
        }
        return q;
    }
}
