package java1234.demo2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/21.
 * 操作文档  文档域加权
 */
public class OperateDocument2 {

    private String ids[]={"1","2","3","4"};
    private String authors[]={"Jack","Marry","John","Json"};
    private String positions[]={"accounting","technician","salesperson","boss"};
    private String titles[]={"Java is a good language.","Java is a cross platform language","Java powerful","You should learn java"};
    private String contents[]={
            "If possible, use the same JRE major version at both index and search time.",
            "When upgrading to a different JRE major version, consider re-indexing. ",
            "Different JRE major versions may implement different versions of Unicode,",
            "For example: with Java 1.4, `LetterTokenizer` will split around the character U+02C6,"
    };

    private Directory dir;


    /**
     * 创建索引
     * @throws IOException
     */
    @Test
    public void index() throws IOException {
        dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene3"));
        IndexWriter indexWriter = getWriter();
        Document doc;
        TextField field;
        for (int i = 0; i < ids.length; i++) {
            doc = new Document();
            doc.add(new StringField("id", ids[i], Field.Store.YES));
            doc.add(new StringField("author", authors[i], Field.Store.YES));
            doc.add(new StringField("position", positions[i], Field.Store.YES));
            field = new TextField("title", titles[i], Field.Store.YES);
            if ("boss".equals(positions[i])) {
                //void setBoost(float boost) 设置这个文件的任何字段命中因素(即权重)
                field.setBoost(1.5f);
            }
            doc.add(field);
            doc.add(new TextField("content", contents[i], Field.Store.NO));
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }

    /**
     * 获取 indexWriter 写索引的对象
     * @return
     * @throws IOException
     */
    private IndexWriter getWriter() throws IOException {
        Analyzer analyzer = new StandardAnalyzer(); //标准分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir, config);
    }

    @Test
    public void search() throws IOException {
        dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene3"));
        // DirectoryReader.open(dir) 方法返回的是 DirectoryReader类, 该类IndexReader 的子类
        //即 DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        String searchField = "title";
        String q = "language";
        //Term 这个类是搜索的最低单位。它是在索引过程中类似Field。
        Term term = new Term(searchField, q);
        Query query = new TermQuery(term);
        TopDocs hits = indexSearcher.search(query, 10); //返回前10条
        System.out.println("查询关键字是:"+q+" 命中个数:"+hits.totalHits);
        Document doc;
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("author"));
        }
        indexReader.close();
    }

}
