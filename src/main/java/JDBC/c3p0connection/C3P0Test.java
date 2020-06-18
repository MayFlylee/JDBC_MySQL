package JDBC.c3p0connection;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Test {
    public void testGetConnection() throws Exception {
        //获取数据库连接池对象
        //方式一
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3308/book?serverTimezone=GMT&rewriteBatchedStatements=true" );
        cpds.setUser("root");
        cpds.setPassword("123456");

        cpds.setInitialPoolSize(10);
        Connection conn = cpds.getConnection();
        System.out.println(conn);
        //销毁连接池
        //DataSources.destroy(cpds);
    }

    //获取数据库连接池对象
    //方式二：使用配置文件
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cpds=new ComboPooledDataSource("helloc3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

    public static void main(String[] args) throws Exception {
        C3P0Test ct = new C3P0Test();
        ct.testGetConnection1();

    }
}
