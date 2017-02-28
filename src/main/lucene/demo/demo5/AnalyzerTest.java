package demo.demo5;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by zsq on 2017/2/23.
 * 分词器  SimpleAnalyzer、CJKAnalyzer、IKAnalyzer
 */
public class AnalyzerTest {

    @Test
    public void test() throws IOException {
        String txt = "我是中国人";
        //Analyzer analyzer1 = new StandardAnalyzer();// 标准分词器    我  是  中  国  人
        //Analyzer analyzer2 = new SimpleAnalyzer();   // 简单分词器   我是中国人
        //Analyzer analyzer3 = new CJKAnalyzer();     // 二元分词器  我是  是中  中国  国人

        //调用IKAnalyzer 后 可能 lucene 版本 和  IKIKAnalyzer 版本不一致 报错 java.lang.AbstractMethodError: org.apache.lucene.analysis.Analyzer.createComponents(Ljava/lang/String;)Lorg/apache/lucene/analysis/Analyzer$TokenStreamComponents;
        //Analyzer analyzer4 = new IKAnalyzer(true);        //语意分词器

        Analyzer analyzer5 = new SmartChineseAnalyzer();
        TokenStream tokenStream = analyzer5.tokenStream("", new StringReader(txt));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);  //与以上语句相同
        tokenStream.reset();
        while (tokenStream.incrementToken()) {// 遍历得到token
            //以下输出方式结果一样
            System.out.print(new String(charTermAttribute.buffer(), 0, charTermAttribute.length()) + "  ");
            System.out.print(charTermAttribute.toString() + "|");
        }
    }
}