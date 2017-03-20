package demo.demo1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.nio.file.Paths;

/**
 * Created by zsq on 2017/2/21.
 * lucene 的写索引
 */
public class IndexerType {

    private IndexWriter indexWriter;  //写索引

    /**
     * 初始化索引
     * @param indexDir
     */
    public IndexerType(String indexDir) throws IOException {
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
    public int index(String dataDir) throws Exception {
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
    private void indexFile(File f) throws Exception {
        System.out.println("文件的路径:"+f.getCanonicalPath());
        Document doc = getDocument(f);  //获取文档对象
        indexWriter.addDocument(doc);
    }

    /**
     * 获取document 文档对象
     * @param f
     * @return
     */
    private Document getDocument(File f) throws Exception {
        Document doc = new Document();
        /*FileReader fileReader = new FileReader(f);
        int ch = 0;  //读取 fileReader 中的字符
        while ((ch = fileReader.read()) != -1) {
            System.out.println((char)ch);
        }
        fileReader.close();*/

        // 根据不同格式的文档 进行转换 为 字符, 再添加到文档中
        String contents = parseFileType(f.getCanonicalPath(), f.getName());

        //在文档对象中 写入 要索引的字段
        doc.add(new TextField("contents", contents, Field.Store.YES)); //文件内容
        doc.add(new TextField("fileName", f.getName(), Field.Store.YES)); //文件名
        doc.add(new TextField("fullPath", f.getCanonicalPath(), Field.Store.YES)); //文件全路径名
        doc.add(new SortedDocValuesField("fileName", new BytesRef(f.getName())));  //添加要排序的字段 字符型
        return doc;
    }

    /**
     * 判断 文件 格式 转为 相应的文件
     * @param canonicalPath 文件路径
     * @param fileName 文件名
     * @return
     * @throws Exception
     */
    public static String parseFileType(String canonicalPath, String fileName) throws Exception {
        System.out.println(canonicalPath+"-----"+fileName);
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        System.out.println(suffix);
        if ("doc".equals(suffix) || "docx".equals(suffix)) {
            return readWord(canonicalPath);
        } else if ("pdf".equals(suffix)) {
            return readPDF(canonicalPath);
        } else if ("html".equals(suffix) || "htm".equals(suffix)){
            return readHtml(canonicalPath);
        } else if ("txt".equals(suffix)) {
            return readTXT(canonicalPath);
        } else {
            return "";
        }
    }

    /**
     * poi  操作  把 word 文档转换 成 字符
     * @param path
     * @return
     */
    public static String readWord(String path) {
        StringBuffer content = new StringBuffer("");// 文档内容
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(path));
            XWPFParagraph[] paragraphs = doc.getParagraphs();
            XWPFParagraph xwpfParagraph;
            for (int i = 0; i < paragraphs.length; i++) {// 遍历段落读取数据
                xwpfParagraph = paragraphs[i];
                content.append(xwpfParagraph.getText());
            }
        } catch (Exception e) {

        }
        return content.toString().trim();
    }

    /**
     * PDFBox 把 pdf 转为 字符
     * @param path
     * @return
     * @throws Exception
     */
    public static String readPDF(String path) throws Exception {
        StringBuffer content = new StringBuffer("");// 文档内容
        FileInputStream fis = new FileInputStream(path);
        PDDocument doc = PDDocument.load(fis);
        PDFTextStripper ts = new PDFTextStripper();
        content.append(ts.getText(doc));
        fis.close();
        return content.toString().trim();
    }

    /**
     * 读取html 文档
     * @param path
     * @return
     */
    public static String readHtml(String path) {
        StringBuffer content = new StringBuffer("");
        File file = new File(path);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            // 读取页面
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    fis,"utf-8"));//这里的字符编码要注意，要对上html头文件的一致，否则会出乱码
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String contentString = content.toString();
        return contentString;
    }

    /**
     * 读取 txt 文档
     * @param path
     * @return
     */
    public static String readTXT(String path) {
        StringBuffer content = new StringBuffer("");// 文档内容
        try {
            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            String s1;

            while ((s1 = br.readLine()) != null) {
                content.append(s1 + "\r");
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString().trim();
    }

    public static void main(String[] args) {
        String indexDir = "/Users/zsq/lucene/wordIndex";  //索引文件存放的路径
        String dataDir = "/Users/zsq/lucene/word";    //要索引的 数据 文件路径
        IndexerType indexer = null;
        int numIndex = 0;
        long start = System.currentTimeMillis();
        try {
            //新建索引对象
            indexer = new IndexerType(indexDir);
            //对文件进行索引,并返回索引文件个数
            numIndex = indexer.index(dataDir);

        } catch (Exception e) {
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
