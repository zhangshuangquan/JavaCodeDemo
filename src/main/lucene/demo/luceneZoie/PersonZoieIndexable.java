package demo.luceneZoie;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import proj.zoie.api.indexing.AbstractZoieIndexable;

/**
 * Created by zsq on 2017/3/13.
 * JavaBean与Document的转换器
 */
public class PersonZoieIndexable extends AbstractZoieIndexable {


    private PersonZoie person;

    private Analyzer analyzer;

    public PersonZoieIndexable(PersonZoie person) {
        super();
        this.person = person;
    }

    public PersonZoieIndexable(PersonZoie person, Analyzer analyzer) {
        super();
        this.person = person;
        this.analyzer = analyzer;
    }

    public Document buildDocument() {
        System.out.println("Person --> Document begining.");
        Document document = new Document();
        document.add(new StringField("id", person.getId().toString(),Field.Store.YES));
        document.add(new StringField("personName", person.getPersonName(), Field.Store.YES));
        document.add(new StringField("sex", person.getSex(), Field.Store.YES));
        document.add(new StringField("birth", String.valueOf(person.getBirth().getTime()), Field.Store.YES));
        document.add(new TextField("nativePlace", person.getNativePlace(), Field.Store.YES));
        document.add(new StringField("job", person.getJob(), Field.Store.YES));
        document.add(new StringField("salary", person.getSalary().toString(), Field.Store.YES));
        document.add(new StringField("hobby", person.getHobby(), Field.Store.YES));
        document.add(new StringField("deleteFlag", person.isDeleteFlag() + "", Field.Store.YES));
        return document;
    }

    @Override
    public IndexingReq[] buildIndexingReqs() {
        return new IndexingReq[] {new IndexingReq(buildDocument(), analyzer)};
    }

    @Override
    public long getUID() {
        return person.getId();
    }

    @Override
    public boolean isDeleted() {
        return person.isDeleteFlag();
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }
}
