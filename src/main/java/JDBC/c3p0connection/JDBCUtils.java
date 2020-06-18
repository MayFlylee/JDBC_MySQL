package JDBC.c3p0connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtils {
    //使用C3P0的连接池技术重新编写工具类（dao）

    //只需要一个连接池，连接池内包含多个连接对象 、
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public Connection getConnection() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }
}
