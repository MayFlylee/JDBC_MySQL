package JDBC.DBUtils;

//封装了针对于JDBC数据库的增删改查操作

import JDBC.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtilsTest {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        QueryRunner qr = new QueryRunner();
        Connection conn = JDBCUtil.getConnection();
    }
}
