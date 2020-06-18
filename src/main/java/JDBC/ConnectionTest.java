package JDBC;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    private String username = "root";
    private String password = "123456";
    String url = "jdbc:mysql://localhost:3308/book?serverTimezone=GMT";

    //方式一：调用第三方接口
    public void test1() throws SQLException {

        Driver driver = new com.mysql.jdbc.Driver();
        Properties info = new Properties();
        info.getProperty("username", username);
        info.getProperty("password", password);

        Connection con = driver.connect(url, info);
        System.out.println(con);
    }

    //方式二，利用反射,使得程序具有更好可以移植性，不好含第三方接口
    public void test2() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        Properties info = new Properties();
        info.getProperty("username", username);
        info.getProperty("password", password);
        Connection con = driver.connect(url, info);
        System.out.println(con);
    }

    //方式三，通过DriverManager替换Driver
    public void test3() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");

        Driver driver = (Driver) clazz.newInstance();
        DriverManager.registerDriver(driver);

        Connection conn = DriverManager.getConnection(url, username, password);
        System.out.println(conn);
    }

    //方式四：优化方式三
    public void test4() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
//        Driver driver = (Driver) clazz.newInstance();
//        DriverManager.registerDriver(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        System.out.println(conn);
    }

    //方式五（final版本）：通过读取配置文件的方式
    public void test5() throws Exception {
        //方式一
        //InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        // 方式二
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties props = new Properties();
        props.load(is);
        String userName = props.getProperty("userName");
        String passWord = props.getProperty("passWord");
        String url = props.getProperty("url");
        String driverClass = props.getProperty("driverClass");

        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(url, userName, passWord);
        System.out.println(conn);
    }




    public static void main(String[] args) throws Exception {
        ConnectionTest c1=new ConnectionTest();
       /* c1.test1();
        c1.test2();
        */
      //  c1.test3();
      //  c1.test4();
        c1.test5();
    }
}
