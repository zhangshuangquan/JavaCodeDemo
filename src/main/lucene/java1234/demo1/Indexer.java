package java1234.demo1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/21.
 * lucene 的写索引
 */
public class Indexer {

    private IndexWriter indexWriter;  //写索引


    /**
     * 初始化索引
     * @param indexDir
     */
    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));  //获取要索引的位置(即索引存放目录)
        Analyzer analyzer = new StandardAnalyzer();  //创建标准分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);  //写索引的配置类
        indexWriter = new IndexWriter(dir, indexWriterConfig);  //创建写索引的对象
    }

    /**
     * 关闭 写索引
     * @throws IOException
     */
    public void close() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    /**
     * 遍历磁盘文件, 并开始进行写索引操作
     * @param dataDir
     * @return
     */
    public int index(String dataDir) throws IOException {
        File[] file = new File(dataDir).listFiles();
        for (File f : file) {
            indexFile(f);
        }
        return indexWriter.numDocs();
    }

    /**
     * 对单个文件进行 写索引操作
     * 并获取document 对象
     * @param f
     */
    private void indexFile(File f) throws IOException {
        System.out.println("文件的路径:"+f.getCanonicalPath());
        Document doc = getDocument(f);  //获取文档对象
        indexWriter.addDocument(doc);
    }

    /**
     * 获取document 文档对象
     * @param f
     * @return
     */
    private Document getDocument(File f) throws IOException {
        Document doc = new Document();
        //在文档对象中 写入 要索引的字段
        doc.add(new TextField("contents", new FileReader(f))); //文件内容
        doc.add(new TextField("fileName", f.getName(), Field.Store.YES)); //文件名
        doc.add(new TextField("fullPath", f.getCanonicalPath(), Field.Store.YES)); //文件全路径名
        return doc;
    }

    public static void main(String[] args) {
        String indexDir = "/Users/zsq/lucene";  //mac pro path
        String dataDir = "/Users/zsq/lucene/data";
        Indexer indexer = null;
        int numIndex = 0;
        long start = System.currentTimeMillis();
        try {
            //新建索引对象
            indexer = new Indexer(indexDir);
            //对文件进行索引,并返回索引文件个数
            numIndex = indexer.index(dataDir);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                indexer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("要写索引的文件数目是:"+numIndex+"索引的时间消耗:"+(end-start)+"ms");
    }
}
