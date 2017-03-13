package demo.luceneZoie;

import java.util.List;

/**
 * Created by zsq on 2017/3/13.
 */
public interface PersonDao {

    /**
     * 新增
     * @return
     */
    public boolean save(PersonZoie person);

    /**
     * 更新
     * @param person
     * @return
     */
    public boolean update(PersonZoie person);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    public boolean delete(Long id);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public PersonZoie findById(Long id);

    /**
     * 查询所有
     * @return
     */
    public List<PersonZoie> findAll();

    /**
     * 查询3秒之前的数据，用于测试
     * @return
     */
    public List<PersonZoie> findPersonBefore3S();

}
