Lucene常用类

    （1）IndexWriter索引过程的核心组件。这个类负责创建新索引或者打开已有索引，以及向索引中添加、删除或更新索引文档的信息。可以把IndexWriter看做这样一个对象：提供针对索引文件的写入操作，但不能用于读取或搜索索引。IndexWriter需要开辟一定空间来存储索引，该功能可以由Directory完成。

    （2）Diretory索引存放的位置，它是一个抽象类，它的子类负责具体制定索引的存储路径。lucene提供了两种索引存放的位置，一种是磁盘，一种是内存。一般情况将索引放在磁盘上；相应地lucene提供了FSDirectory和RAMDirectory两个类。

     （3）Analyzer分析器,主要用于分析搜索引擎遇到的各种文本，Analyzer的工作是一个复杂的过程：把一个字符串按某种规则划分成一个个词语，并去除其中的无效词语（停用词），这里说的无效词语如英文中的“of”、“the”，中文中的“的”、“地”等词语，这些词语在文章中大量出现，但是本身不包含什么关键信息，去掉有利于缩小索引文件、提高效率、提高命中率。分词的规则千变万化，但目的只有一个：按语义划分。这点在英文中比较容易实现，因为英文本身就是以单词为单位的，已经用空格分开；而中文则必须以某种方法将连成一片的句子划分成一个个词语。具体划分方法下面再详细介绍，这里只需了解分析器的概念即可。

   （4）Document文档 Document相当于一个要进行索引的单元，可以是文本文件、字符串或者数据库表的一条记录等等，一条记录经过索引之后，就是以一个Document的形式存储在索引文件，索引的文件都必须转化为Document对象才能进行索引。

    （5）Field一个Document可以包含多个信息域，比如一篇文章可以包含“标题”、“正文”等信息域，这些信息域就是通过Field在Document中存储的。
    Field有两个属性可选：存储和索引。通过存储属性你可以控制是否对这个Field进行存储；通过索引属性你可以控制是否对该Field进行索引。这看起来似乎有些废话，事实上对这两个属性的正确组合很重要，下面举例说明：还是以刚才的文章为例子，我们需要对标题和正文进行全文搜索，所以我们要把索引属性设置为true，同时我们希望能直接从搜索结果中提取文章标题，所以我们把标题域的存储属性设置为true，但是由于正文域太大了，我们为了缩小索引文件大小，将正文域的存储属性设置为false，当需要时再直接读取文件；我们只是希望能从搜索解果中提取最后修改时间，不需要对它进行搜索，所以我们把最后修改时间域的存储属性设置为true，索引属性设置为false。上面的三个域涵盖了两个属性的三种组合，还有一种全为false的没有用到，事实上Field不允许你那么设置，因为既不存储又不索引的域是没有意义的。

  （6）IndexSearcher  是lucene中最基本的检索工具，所有的检索都会用到IndexSearcher工具。

  （7）IndexReader 打开一个Directory读取索引类。

  （8）Query  查询,抽象类，必须通过一系列子类来表述检索的具体需求，lucene中支持模糊查询，语义查询，短语查询，组合查询等等,如有TermQuery,BooleanQuery,RangeQuery,WildcardQuery等一些类。

   （9）QueryParser 解析用户的查询字符串进行搜索,是一个解析用户输入的工具，可以通过扫描用户输入的字符串，生成Query对象。

  （10）TopDocs 根据关键字搜索整个索引库，然后对所有结果进行排序,取指定条目的结果。

  （11）TokenStream Token  分词器Analyzer通过对文本的分析来建立TokenStreams(分词数据流)。TokenStream是由一个个Token(分词组成的数据流)。所以说Analyzer就代表着一个从文本数据中抽取索引词（Term）的一种策略。

 （12）AttributeSource  TokenStream即是从Document的域（field）中或者查询条件中抽取一个个分词而组成的一个数据流。TokenSteam中是一个个的分词，而每个分词又是由一个个的属性（Attribute）组成。对于所有的分词来说，每个属性只有一个实例。这些属性都保存在AttributeSource中，而AttributeSource正是TokenStream的父类。



```
package java1234.demo5;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
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

    private Integer ids[] = {1,2,3};
    private String citys[] = {"青岛","南京","上海"};
    private String descs[] = {
            "青岛是一个美丽的城市。",
            "南京是一个有文化的城市。南京是一个文化的城市南京，简称宁，是江苏省会，地处中国东部地区，长江下游，濒江近海。全市下辖11个区，总面积6597平方公里，2013年建成区面积752.83平方公里，常住人口818.78万，其中城镇人口659.1万人。[1-4] “江南佳丽地，金陵帝王州”，南京拥有着6000多年文明史、近2600年建城史和近500年的建都史，是中国四大古都之一，有“六朝古都”、“十朝都会”之称，是中华文明的重要发祥地，历史上曾数次庇佑华夏之正朔，长期是中国南方的政治、经济、文化中心，拥有厚重的文化底蕴和丰富的历史遗存。[5-7] 南京是国家重要的科教中心，自古以来就是一座崇文重教的城市，有“天下文枢”、“东南第一学”的美誉。截至2013年，南京有高等院校75所，其中211高校8所，仅次于北京上海；国家重点实验室25所、国家重点学科169个、两院院士83人，均居中国第三。[8-10] 。",
            "上海是一个繁华的城市。"
    };

    private Directory dir;

    /**
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter()throws Exception{
        //Analyzer analyzer = new StandardAnalyzer(); 有中文 就不能用标准分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
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
        Document doc;
        for(int i = 0;i < ids.length; i++){
            doc = new Document();
            doc.add(new TextField("id", ids[i].toString(), Field.Store.YES));
            doc.add(new StringField("city", citys[i], Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.YES));
            doc.add(new NumericDocValuesField("id", ids[i]));  //添加要排序的字段
            writer.addDocument(doc);
        }
        writer.close();
    }


    public static void main(String[] args) throws Exception {
        new Indexer().index("/Users/zsq/lucene/lucene6");
    }
}
```




```
package java1234.lucenepagedemo;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/23.
 * lucene 的 分页查询有两种 1. 再查询方式  2. 利用searchAfter() 主要用于大量数据查询分页
 *
 */
public class LucenePageTest {


    /**
     * 再查询方法: 先查询所有数据, 然后去分页数据.
     * 如果数据量大, 容易内存溢出
     * @param q  搜索的关键字
     * @param pageIndex  第几页
     * @param pageSize   每页显示多少条
     * @throws IOException
     * @throws ParseException
     */
    public static void searchPage(String q, int pageIndex, int pageSize) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene6"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器

        QueryParser queryParser = new QueryParser("desc", analyzer);
        Query query = queryParser.parse(q);
        TopDocs hits = indexSearcher.search(query, 500); //返回前500条 数据已在内存中
        ScoreDoc[] scoreDocs = hits.scoreDocs;
        int start = (pageIndex - 1) * pageSize;
        int end = pageIndex * pageSize;
        int total = hits.totalHits;
        if (end > total) {
            end = total;
        }
        Document doc;
        for (int i = start; i < end; i++) {
            doc = indexSearcher.doc(scoreDocs[i].doc);
            System.out.println(doc.get("desc"));
        }
        indexReader.close();
    }


    /**
     *
     * @param q   搜索关键字
     * @param pageIndex 第几页
     * @param pageSize  每页显示多少条
     * @throws IOException
     * @throws ParseException
     */
    public static void searchPageByAfter(String q, int pageIndex, int pageSize) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get("/Users/zsq/lucene/lucene6"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(); // 中文分词器

        QueryParser queryParser = new QueryParser("desc", analyzer);
        Query query = queryParser.parse(q);

        //获取上一页的最后一个元素
        ScoreDoc LastScoreDoc = getLastScoreDoc(pageIndex, pageSize, query, indexSearcher);

        /**
         * 如果有排序 getLastScoreDoc() 和 searchAfter() 中 都必须 加入sort
         */
        //Sort sort = new Sort();
        //sort.setSort(new SortField("id", SortField.Type.INT, true));
        //TopDocs hits = indexSearcher.searchAfter(LastScoreDoc, query, pageSize, sort);


        TopDocs hits = indexSearcher.searchAfter(LastScoreDoc, query, pageSize);
        Document doc;
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("desc"));
        }
        indexReader.close();
    }

    /**
     * 返回上一次的最后一个元素
     * @param pageIndex  第几页
     * @param pageSize   每页显示多少
     * @param query
     * @param indexSearcher
     * @return
     * @throws IOException
     */
    private static ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher indexSearcher) throws IOException {
        //如果当前页是第一页则返回null
        if (pageIndex == 1) {
            return null;
        }
        int num = (pageIndex - 1) * pageSize;  // 获取上一页的数量

        //Sort sort = new Sort();
        //sort.setSort(new SortField("id", SortField.Type.INT, true));
        //TopDocs hits = indexSearcher.search(query, num, sort);  //查询返回上一页的数据

        TopDocs hits = indexSearcher.search(query, num);  //查询返回上一页的数据
        int total = hits.totalHits;
        if (num > total) {
            num = total;
        }
        return hits.scoreDocs[num-1];
    }

    public static void main(String[] args) {
        try {
            LucenePageTest.searchPage("南京", 1, 2);
            System.out.println("--------- searchAfter() ----------");
            LucenePageTest.searchPageByAfter("城市", 1, 2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}

```