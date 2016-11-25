package java8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zsq on 16/11/24.
 */
public class StreamDemo {
    public static void main(String[] args) {
        List<Article> list = new ArrayList<>();
        list.add(new Article("Java编程思想", "zhangsan", addTags("Java", "Java base", "Java web")));
        list.add(new Article("Java Web", "zhangsan", addTags("Java", "Java web", "JavaEE")));
        list.add(new Article("C++编程思想", "lisi", addTags("C++", "C++ primer", "C++ base")));
        list.add(new Article("Spark编程思想", "spark", addTags("scala", "spark", "big data")));

        System.out.println(Article.getFirstJavaArticle(list).getTitle()); //Java编程思想
        System.out.println(Article.getFirstJavaArticleByStream(list).get().getTitle()); //Java编程思想

        System.out.println("=====获取所有java文章=======");

        Article.getAllJavaArticles(list).forEach(article -> System.out.println(article.getTitle())); //Java编程思想  Java Web
        Article.getAllJavaArticlesByStream(list).stream().forEach(article -> System.out.println(article.getTitle()));

        System.out.println("=======按作者分组===========");

        List<String> keys = Article.groupByAuthor(list).entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
        keys.forEach(System.out::println);
        //List<Article> values = Article.groupByAuthor(list).entrySet().stream().map(x -> x.getValue()).findAny().get();
        //values.forEach(article -> System.out.println(article.getAuthor()));

        System.out.println("=======过滤重复的标签========");
        Article.getDistinctTags(list).forEach(System.out::println);
        Article.getDistinctTagsByStream(list).forEach(System.out::println);

    }

    /**
     * 可变长参数
     * @param tags
     * @return
     */
    public static List<String> addTags(String... tags) {
        List<String> list = new ArrayList<>();
        for (String tag : tags) {
            list.add(tag);
        }
        return list;
    }


}

class Article {

    private String title;
    private String author;
    private List<String> tags;

    public Article(String title, String author, List<String> tags) {
        this.title = title;
        this.author = author;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * 查询包含java的文章
     * 把第一次出现的返回
     * @param articles
     * @return
     */
    public static Article getFirstJavaArticle(List<Article> articles) {

        for (Article article : articles) {
            if (article.getTags().contains("Java")) {
                return article;
            }
        }
        return null;
    }

    /**
     * findFirst()
     * @param articles
     * @return
     */
    public static Optional<Article> getFirstJavaArticleByStream(List<Article> articles) {
        return articles.stream().filter(article -> article.getTags().contains("Java")).findFirst();

    }

    /**
     * 查询所有包含java的文章
     * @param articles
     * @return
     */
    public static List<Article> getAllJavaArticles(List<Article> articles) {
        List<Article> result = new ArrayList<>();
        for (Article article : articles) {
            if (article.getTags().contains("Java")) {
                result.add(article);
            }
        }
        return result;
    }

    /**
     * Collectors.toList()
     * @param articles
     * @return
     */
    public static List<Article> getAllJavaArticlesByStream(List<Article> articles) {
        return articles.stream().filter(article -> article.getTags().contains("Java")).collect(Collectors.toList());
    }

    /**
     * 按作者分组
     * @param articles
     * @return
     */
    public static Map<String, List<Article>> groupByAuthor(List<Article> articles) {
        Map<String, List<Article>> result = new HashMap<>();
        for (Article article : articles) {
            if (result.containsKey(article.getAuthor())) {
                result.get(article.getAuthor()).add(article);
            } else {
                List<Article> list = new ArrayList<>();
                list.add(article);
                result.put(article.getAuthor(), list);
            }
        }
        return result;
    }

    /**
     * 按作者分组  groupingBy
     * Collectors.groupingBy(article -> article.getAuthor())
     * Collectors.groupingBy(Article::getAuthor)
     * 以上两种都可以
     * @param articles
     * @return
     */
    public static Map<String, List<Article>> groupByAuthorByStream(List<Article> articles) {
        //return articles.stream().collect(Collectors.groupingBy(article -> article.getAuthor()));
        return articles.stream().collect(Collectors.groupingBy(Article::getAuthor));
    }

    /**
     * 获取文章中所有不同的标签
     * @param articles
     * @return
     */
    public static Set<String> getDistinctTags(List<Article> articles) {
        Set<String> result = new HashSet<>();
        for (Article article : articles) {
            result.addAll(article.getTags());
        }
        return result;
    }

    /**
     * flatMap()
     * Collectors.toSet()
     * flatmap 把标签列表转为一个返回流，然后使用 collect 去创建一个集合作为返回值。
     * @param articles
     * @return
     */
    public static Set<String> getDistinctTagsByStream(List<Article> articles) {
        return articles.stream().flatMap(article -> article.getTags().stream()).collect(Collectors.toSet());
    }
}
