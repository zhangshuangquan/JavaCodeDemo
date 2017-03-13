package demo.luceneZoie;

import demo.utils.LuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by zsq on 2017/3/13.
 *
 * 对数据库 数据创建索引
 *
 */
public class CreateIndexTest {

    private PersonDao personDao;
    /**索引目录*/
    private String indexDir;

    public static void main(String[] args) throws IOException {
        String userIndexPath = "/Users/zsq/lucene/zoieIndex";
        PersonDao personDao = new PersonDaoImpl();
        //先读取数据库表中, 已有数据创建索引
        CreateIndexTest createIndexTest = new CreateIndexTest(personDao, userIndexPath);
        createIndexTest.index();
    }

    public CreateIndexTest(PersonDao personDao, String indexDir) {
        super();
        this.personDao = personDao;
        this.indexDir = indexDir;
    }

    public void index() throws IOException {
        List<PersonZoie> persons = personDao.findAll();
        if(null == persons || persons.size() == 0) {
            return;
        }

        /**
         * 打开 写索引类 IndexWriter
         */
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, indexWriterConfig);

        //遍历数据库数据, 并添加文档写入索引
        for(PersonZoie person : persons) {
            Document document = new Document();
            document.add(new StringField("id",person.getId().toString(),Field.Store.YES));
            document.add(new StringField("personName", person.getPersonName(), Field.Store.YES));
            document.add(new StringField("sex", person.getSex(), Field.Store.YES));
            document.add(new StringField("birth", String.valueOf(person.getBirth().getTime()), Field.Store.YES));
            document.add(new TextField("nativePlace", person.getNativePlace(), Field.Store.YES));
            document.add(new StringField("job", person.getJob(), Field.Store.YES));
            document.add(new StringField("salary", person.getSalary().toString(), Field.Store.YES));
            document.add(new StringField("hobby", person.getHobby(), Field.Store.YES));
            document.add(new StringField("deleteFlag", person.isDeleteFlag() + "", Field.Store.YES));

            //Zoie需要的UID[注意：这个域必须加，且必须是NumericDocValuesField类型，至于UID的域值是什么没关系，只要保证它是唯一的即可]
            document.add(new NumericDocValuesField("_ID", person.getId()));
            LuceneUtils.addIndex(writer, document);
        }
        writer.close();
        dir.close();
    }
}
