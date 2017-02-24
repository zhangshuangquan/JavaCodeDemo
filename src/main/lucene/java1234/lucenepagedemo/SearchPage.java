package java1234.lucenepagedemo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsq on 2017/2/23.
 */
public class SearchPage {

    public static void main(String[] args) {
        //参数定义
        String directoryPath = "/Users/zsq/lucene/index";   //索引存放目录
        String fieldName = "contents";
        String queryString = "lucene";
        int currentPage = 2;   //当前页
        int pageSize = 3;     //每页显示条数

        Page<Document> page;
        try {
            page = pageQuery(fieldName, queryString, directoryPath, currentPage, pageSize);
            showData(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据展示
     * @param page
     */
    public static void showData(Page<Document> page){
        if(page == null || page.getItems() == null || page.getItems().size() == 0) {
            System.out.println("no results found.");
            return;
        }
        for(Document doc : page.getItems()) {
            String fileName = doc.get("fileName");
            String fullPath = doc.get("fullPath");
            String content = doc.get("contents");
            System.out.println("fileName:" + fileName);
            System.out.println("fullPath:" + fullPath);
            System.out.println("content:" + content);
        }
    }

    /**
     * 索引分页查询
     * @param fieldName
     * @param queryString
     * @param directoryPath
     * @param currentPage
     * @param pageSize
     * @return
     * @throws ParseException
     */
    private static Page pageQuery(String fieldName, String queryString, String directoryPath, int currentPage, int pageSize) throws ParseException, IOException {
        QueryParser parser = new QueryParser(fieldName, new StandardAnalyzer());
        Query query = parser.parse(queryString);
        Page<Document> page = new Page<>(currentPage, pageSize);
        pageQuery(directoryPath, query, page);
        return page;
    }

    /**
     * lucene  分页查询
     * @param directoryPath
     * @param query
     * @param page
     * @throws IOException
     */
    private static void pageQuery(String directoryPath, Query query, Page<Document> page) throws IOException {
        IndexSearcher searcher = createIndexSearcher(directoryPath);
        int totalRecord = searchTotalRecord(searcher, query);
        //设置总记录数
        page.setTotalRecord(totalRecord);

        /**
         * 按 文件名 排序
         * true 为降序排列   false 为升序排列
         */
        Sort sort = new Sort();
        sort.setSort(new SortField("fileName", SortField.Type.STRING, false));

        System.out.println(page.getAfterDocId());

        ScoreDoc after = getLastScoreDoc(page.getCurrentPage(), page.getPageSize(), query, searcher, sort);

        /**
         * 1. 如果 getLastScoreDoc() 和 searchAfter() 方法中都没有排序sort  这是可以的.
         *
         * 2. 如果 获取上一页的最后一个Doc方法(即getLastScoreDoc())中有排序, 那么 searchAfter() 中必须加上排序,
         * 否则  searchAfter() 没有排序 会得到相同的(第一页的)数据.
         *
         * 3. 如果 getLastScoreDoc() 中没有排序 但是 searchAfter() 有排序 则会报错如下:
         * java.lang.IllegalArgumentException: after must be a FieldDoc; got doc=5 score=0.3940055 shardIndex=0
         *
         */
        TopDocs topDocs = searcher.searchAfter(after, query, page.getPageSize(), sort);

        List<Document> docList = new ArrayList<>();
        ScoreDoc[] docs = topDocs.scoreDocs;
        int index = 0;
        int docID;
        Document document;
        for (ScoreDoc scoreDoc : docs) {
            docID = scoreDoc.doc;
            document = searcher.doc(docID);
            if(index == docs.length - 1) {
                page.setAfterDoc(scoreDoc);
                page.setAfterDocId(docID);
            }
            docList.add(document);
            index++;
        }
        page.setItems(docList);
        searcher.getIndexReader().close();
    }

    /**
     * 获取符合条件的总记录数
     * @param searcher
     * @param query
     * @return
     * @throws IOException
     */
    private static int searchTotalRecord(IndexSearcher searcher, Query query) throws IOException {
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
        if(topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
            return 0;
        }
        //ScoreDoc[] docs = topDocs.scoreDocs;  //docs.length  或者  可以直接返回 topDocs.totalHits
        return topDocs.totalHits;
    }

    /**
     * 根据页码和分页大小获取上一次的最后一个ScoreDoc
     *  获取这个scoreDoc的时候，如果有排序，一定加上排序（sort） 否则会出现ScoreDoc 无法转换FieldDoc的错误
     */
    private static ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher, Sort sort)
            throws IOException {
        if (pageIndex == 1)
            return null; // 如果是第一页就返回空
        int num = pageSize * (pageIndex - 1);// 获取上一页的数量
        TopDocs tds = searcher.search(query, num, sort);
        return tds.scoreDocs[num - 1];
    }

    /**
     * 创建索引查询器
     * @param directoryPath 索引目录
     * @return
     * @throws IOException
     */
    private static IndexSearcher createIndexSearcher(String directoryPath) throws IOException {
        return new IndexSearcher(createIndexReader(directoryPath));
    }

    /**
     * 创建索引阅读器
     * @param directoryPath 索引目录
     * @return
     * @throws IOException
     */
    private static IndexReader createIndexReader(String directoryPath) throws IOException {
        return DirectoryReader.open(FSDirectory.open(Paths.get(directoryPath, new String[0])));
    }
}
