package demo.demo2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/21.
 * lucene 的文档操作 (添加, 删除, 修改 文档)
 *
 * 文档 Document相当于一个要进行索引的单元，可以是文本文件、字符串或者数据库表的一条记录等等.
 * 一条记录经过索引之后，就是以一个Document的形式存储在索引文件，
 * 索引的文件都必须转化为Document对象才能进行索引。
 */
public class OperateDocument {

    private String ids[]={"1","2","3"};
    private String cities[]={"qingdao","nanjing","shanghai"};
    private String desc[]={
            "Qingdao is a beautiful city.",
            "Nanjing is a city of culture.",
            "Shanghai is a bustling city."
    };

    private Directory dir;  //目录类


    /**
     * 添加 文档操作
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene2"));
        IndexWriter indexWriter = getWriter();
        Document doc;
        for (int i = 0; i < ids.length; i++) {
            doc = new Document();
            //在文档中添加字段 并存储
            doc.add(new TextField("id", ids[i], Field.Store.YES));
            doc.add(new TextField("city", cities[i], Field.Store.YES));
            doc.add(new TextField("desc", desc[i], Field.Store.NO)); //不存储
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }

    /**
     * 获取 indexWriter 写索引对象
     * @return
     * @throws IOException
     */
    public IndexWriter getWriter() throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    /**
     * 测试 写索引
     * @throws IOException
     */
    @Test
    public void testIndexWriter() throws IOException {
        IndexWriter indexWriter = getWriter();
        System.out.println("文件的个数:"+indexWriter.numDocs());
    }

    /**
     * 测试 返回读索引对象
     * @throws IOException
     */
    @Test
    public void testIndexReader() throws IOException {
        //打开索引目录 并返回 读索引对象
        IndexReader indexReader = DirectoryReader.open(dir);
        //返回文档的此索引总数，包括文档尚未刷新（仍然在RAM缓冲器），不计算缺失。
        System.out.println(indexReader.maxDoc());  //最大 文档 字段数 3
        //返回文档的此索引总数，包括文档尚未刷新（仍然在RAM缓冲器），并包括缺失
        System.out.println(indexReader.numDocs());  //文档 字段数  3
    }

    @Test
    public void testDeleteBeforeMerge() throws IOException {
        IndexWriter indexWriter = getWriter();
        System.out.println(indexWriter.numDocs());
        indexWriter.deleteDocuments(new Term("id", "1"));
        //提交所有挂起的更改（添加和删除文件，段合并，添加索引等）
        indexWriter.commit();
        System.out.println("writer.maxDoc()的个数:"+indexWriter.maxDoc());  // 3 不计算缺失的索引
        System.out.println("writer.numDocs()的个数:"+indexWriter.numDocs()); // 2 计算缺失的索引
        indexWriter.close();
    }

    /**
     * 测试 强制合并已删除的文档
     * @throws IOException
     */
    @Test
    public void testDeleteAfterMerge() throws IOException {
        IndexWriter indexWriter = getWriter();
        System.out.println(indexWriter.numDocs());
        indexWriter.deleteDocuments(new Term("id", "1"));
        indexWriter.forceMergeDeletes();  // 强制合并已删除的文档（清空回收站）
        indexWriter.commit();
        System.out.println("writer.maxDoc()的个数:"+indexWriter.maxDoc());  // 2  强制合并后返回索引总数
        System.out.println("writer.numDocs()的个数:"+indexWriter.numDocs()); // 2
        indexWriter.close();
    }

    /**
     * 更新 文档
     * @throws IOException
     */
    @Test
    public void testUpdate() throws IOException {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        // Field 子类 包括: StringField TextField DoubleField IntField 等等
        doc.add(new StringField("id", "1", Field.Store.YES));
        doc.add(new StringField("city","qingdao",Field.Store.YES));
        doc.add(new TextField("desc", "dsss is a city.", Field.Store.NO));
        //Term 这个类是搜索的最低单位。它是在索引过程中类似Field。
        writer.updateDocument(new Term("id","1"), doc);
        writer.close();
    }

}
