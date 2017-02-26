package demo.demo4;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/22.
 */
public class Indexer {

        private Integer ids[]={1,2,3};
        private String citys[]={"aingdao","banjing","shanghai"};
        private String descs[]={
                "Qingdao is b beautiful city.",
                "Nanjing is c city of culture.",
                "Shanghai is d bustling city."
        };

        private Directory dir;

    /**
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter()throws Exception{
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    /**
     *
     * @param indexDir
     * @throws Exception
     */
    private void index(String indexDir)throws Exception{
        dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriter writer = getWriter();
        for(int i = 0;i < ids.length; i++){
            Document doc = new Document();
            doc.add(new NumericDocValuesField("id", ids[i]));
            doc.add(new StringField("city",citys[i],Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();
    }


    public static void main(String[] args) throws Exception {
        new Indexer().index("/Users/zsq/lucene/lucene5");
    }

}
