package JDBC.DBCPConnection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;


import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DNCPTest {

    public DNCPTest() throws FileNotFoundException {
    }

    public void testGetConnection() throws SQLException {
        BasicDataSource sources = new BasicDataSource();
        sources.setDriverClassName("com.mysql.jdbc.Driver");
        sources.setUrl("jdbc:mysql://localhost:3308/?serverTimeZone=GMT");
        sources.setUsername("root");
        sources.setPassword("123456");

        //初始化链接数
        sources.setInitialSize(10);
        sources.setMaxWaitMillis(10);

        Connection conn = sources.getConnection();
        System.out.println(conn);
    }

    //方式二，使用配置文件

    private static DataSource dataSource =null;

    static {
        Properties prop=null;
        FileInputStream is=null;
        try {
            //方式一
            //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("DNCP.properties");
            //方式二
            is = new FileInputStream(new File("./src/main/resources/DNCP.properties"));
            prop = new Properties();
            prop.load(is);
            dataSource = BasicDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void testGetConnection2() throws Exception {
        //prop.setProperty()
        Connection conn = dataSource.getConnection();
        System.out.println(conn);
    }

    public static void main(String[] args) throws Exception {
        DNCPTest dt = new DNCPTest();
        dt.testGetConnection2();
    }
}