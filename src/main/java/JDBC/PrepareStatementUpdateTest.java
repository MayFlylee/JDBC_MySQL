package JDBC;



import JDBC.bean.Customer;
import JDBC.util.JDBCUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class PrepareStatementUpdateTest {
    //插入数据
    /*
    1，获取驱动、连接对象
    2、预编译sql语句
    3、通过写入接口执行操作
    4，关闭连接，节省资源
    * */
    public void testInsert() {
        Connection conn =null;
        PreparedStatement ps = null;
        try{
            InputStream is = ClassLoader
                    .getSystemClassLoader()
                    .getResourceAsStream("jdbc.properties");
            Properties props = new Properties();
            props.load(is);
            String user = props.getProperty("user");
            String password = props.getProperty("passWord");
            String url = props.getProperty("url");
            String driverClass = props.getProperty("driverClass");

            Class.forName(driverClass);

            conn = DriverManager.getConnection(url, user, password);

            System.out.println(conn);

            //插入占位符号
            String sql = "insert into scroe values(?,?,?,?)";
            ps = conn.prepareStatement(sql);


            ps.setString(1, "1");
            ps.setString(2, "1");
            ps.setString(3, "姓名");
            ps.setString(4,"90");
            ps.execute();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
    //修改数据
    public void testUpdate() throws SQLException, IOException, ClassNotFoundException {
        /*
        1,获取连接对象
        2，预编译sql语句
        3，填充占位符
        4，执行
        5，资源关闭
        * */
        Connection conn=JDBCUtil.getConnection();

        String sql="update scroe set Grde=? where Id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "95");
        ps.setString(2, "1");
        ps.execute();
        JDBCUtil.closeResource(ps, conn);
        System.out.println("修改成功");
    }
    //使用通用的增删改方法
    public void testUpdateUtil() {
        String sql = "insert into scroe values(?,?,?,?)";
        JDBCUtil.update(sql, 2, 2, "张三", 60);
    }

    //查询customer数据表，查询的表属性数量固定
    public void testFind(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "select*from student";
            ps = conn.prepareStatement(sql);
            //执行返回结果集
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String sex = rs.getString(3);
                Date birth = rs.getDate(4);
                String Department = rs.getString(5);
                String Address = rs.getString(6);
                //方式一
                //System.out.println(id+","+name+","+ sex+","+birth+","+Department+","+Address);
                //方式二
                Customer customer1 = new Customer(id, name, sex, birth, Department, Address);
                System.out.println(customer1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeResource(ps, conn, rs);
        }
    }

    //customer类的通用方法查询，不限查询属性的数量
    public void testFind1(){
        String sql = "select * from student";
        Customer customer = JDBCUtil.find(sql);
        System.out.println(customer);
    }
    //customer类的通用方法查询改进版
    public void testFind2(){
        String sql = "select * from student";
        Customer customer = JDBCUtil.find1(sql);
        System.out.println(customer);
    }

    //任何类通用方法查询改进版
    public void testFind3(){
        String sql = "select * from student";
        Customer customer = JDBCUtil.find2(Customer.class,sql);
        System.out.println(customer);
    }

    //任何类通用方法查询,返回对个列
    @Test
    public void testFind4() {
        String sql = "select * from student where id > 0";
        List<Customer> list = JDBCUtil.find3(Customer.class, sql);
        list.forEach(System.out::println);
    }
}






















