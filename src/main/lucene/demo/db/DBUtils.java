package demo.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
/**
 * Created by zsq on 2017/3/13.
 */
public class DBUtils {

    private static DataSource dataSource;

    public static QueryRunner getQueryRunner(){
        if(DBUtils.dataSource == null){
            BasicDataSource dbcpDataSource = new BasicDataSource();
            dbcpDataSource.setUrl("jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf8");
            dbcpDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dbcpDataSource.setUsername("root");
            dbcpDataSource.setPassword("6633");
            dbcpDataSource.setDefaultAutoCommit(true);
            dbcpDataSource.setMaxActive(100);
            dbcpDataSource.setMaxIdle(30);
            dbcpDataSource.setMaxWait(500);
            DBUtils.dataSource = dbcpDataSource;
        }
        return new QueryRunner(DBUtils.dataSource);
    }
}
