package java1234.demo3;

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
 * Created by zsq on 2017/2/22.
 */
public class Indexer {

    private IndexWriter indexWriter;

    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(dir, config);
    }

    public void close() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    public int index(String dataDir) throws IOException {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            indexFile(file);
        }
        return indexWriter.numDocs();
    }

    /**
     * 获取文档,并把文档写入索引
     * @param file
     * @throws IOException
     */
    private void indexFile(File file) throws IOException {
        System.out.println("文件的路径:"+file.getCanonicalPath());
        Document doc = getDocument(file);  //获取文档
        indexWriter.addDocument(doc);  //把文档写到索引中
    }

    /**
     * 生成文档
     * @param file
     * @return
     */
    private Document getDocument(File file) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(file)));
        doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return doc;
    }

    public static void main(String[] args) {
        String indexDir = "/Users/zsq/lucene/lucene4";
        String dataDir = "/Users/zsq/lucene/lucene4/data";
        Indexer indexer = null;
        int numIndexed = 0;
        long start = System.currentTimeMillis();
        try {
             indexer = new Indexer(indexDir);
             numIndexed = indexer.index(dataDir);
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
        System.out.println("要写索引的文件数目是:"+numIndexed+" 索引的时间消耗:"+(end-start)+"ms");
    }

}
