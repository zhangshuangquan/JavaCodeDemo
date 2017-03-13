package demo.luceneZoie;

import org.apache.lucene.analysis.Analyzer;
import proj.zoie.api.indexing.AbstractZoieIndexableInterpreter;
import proj.zoie.api.indexing.ZoieIndexable;

/**
 * Created by zsq on 2017/3/13.
 * JavaBean (自定义Person)-->Document的数据转换器的生产者
 */
public class CustomPersonZoieIndexableInterpreter extends AbstractZoieIndexableInterpreter<PersonZoie> {

    private Analyzer analyzer;

    @Override
    public ZoieIndexable convertAndInterpret(PersonZoie person) {
        return new PersonZoieIndexable(person, analyzer);
    }

    public CustomPersonZoieIndexableInterpreter() {}

    public CustomPersonZoieIndexableInterpreter(Analyzer analyzer) {
        super();
        this.analyzer = analyzer;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }
}
