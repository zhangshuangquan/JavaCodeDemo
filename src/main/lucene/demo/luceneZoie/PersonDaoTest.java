package demo.luceneZoie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsq on 2017/3/13.
 *
 * lucene 增量索引 测试
 */
public class PersonDaoTest {


    private static final DateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args){
        try {
            addPerson();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加一个Person测试
     * @throws ParseException
     */
    public static void addPerson() throws ParseException {
        PersonDao personDao = new PersonDaoImpl();
        String personName = "张国荣";
        String sex = "1";
        String birthString = "1956-09-12";
        Date birth = dateFormate.parse(birthString);
        String nativePlace = "中国香港九龙";
        String job = "歌手";
        Integer salary = 16000;
        String hobby = "演员&音乐";
        boolean deleteFlag = false;
        PersonZoie person = new PersonZoie(personName, sex, birth, nativePlace, job, salary, hobby, deleteFlag);
        boolean success = personDao.save(person);
        System.out.println(success ? "Person save successful." : "Person save fauilure.");
    }

}
