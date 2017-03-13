package demo.luceneZoie;

import demo.db.DBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by zsq on 2017/3/13.
 */
public class PersonDaoImpl implements PersonDao {

    private QueryRunner queryRunner = DBUtils.getQueryRunner();
    /**
     * 新增
     * @return
     */
    @Override
    public boolean save(PersonZoie person) {
        int result = 0;
        try {
            result = queryRunner.update("insert into person_zoie(personName,sex,birth,nativePlace,job,salary,hobby,deleteFlag,updatedTime) " +
                    "values(?,?,?,?,?,?,?,?,?)" , new Object[] {
                    person.getPersonName(),
                    person.getSex(),
                    person.getBirth(),
                    person.getNativePlace(),
                    person.getJob(),
                    person.getSalary(),
                    person.getHobby(),
                    person.isDeleteFlag(),
                    new Date()
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * 根据ID更新
     * @param person
     * @return
     */
    @Override
    public boolean update(PersonZoie person) {
        int result = 0;
        try {
            result = queryRunner.update(
                    "update person_zoie set personName = ?, sex = ?, birth = ?, " +
                            "nativePlace = ?, job = ?, salary = ?, hobby = ?,deleteFlag = ?, " +
                            "updatedTime = ? where id = ?"
                    , new Object[] {
                            person.getPersonName(),
                            person.getSex(),
                            person.getBirth(),
                            person.getNativePlace(),
                            person.getJob(),
                            person.getSalary(),
                            person.getHobby(),
                            person.isDeleteFlag(),
                            new Date(),
                            person.getId()
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    public boolean delete(Long id) {
        int result = 0;
        try {
            result = queryRunner.update("delete from person_zoie where id = ?", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public PersonZoie findById(Long id) {
        PersonZoie person = null;
        try {
            person = queryRunner.query("select * from person_zoie where id = ?", new BeanHandler<PersonZoie>(PersonZoie.class),id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    /**
     * 查询所有
     * @return
     */
    public List<PersonZoie> findAll() {
        List<PersonZoie> persons = null;
        try {
            persons = queryRunner.query("select * from person_zoie", new BeanListHandler<PersonZoie>(PersonZoie.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    /**
     * 查询3秒之前的数据，用于测试
     * @return
     */
    public List<PersonZoie> findPersonBefore3S() {
        List<PersonZoie> persons = null;
        try {
            persons = queryRunner.query("select * from person_zoie where updatedTime >= DATE_SUB(NOW(),INTERVAL 3 SECOND)", new BeanListHandler<PersonZoie>(PersonZoie.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }
}
