package JDBC.util;

import JDBC.ConnectionTest;
import JDBC.bean.Customer;
import jdk.nashorn.internal.scripts.JD;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//操作数据库的工具类
public class JDBCUtil {

    /*
     * 1，连接数据库
     * 2，增数据
     * 3.删数据
     * 4，改数据
     * 5，查数据
     * */
    public static Connection getConnection()
            throws IOException, ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
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
        return conn;
    }


    //增删改关闭资源
    public static void closeResource(Statement ps, Connection conn) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //查询关闭资源
    public static void closeResource(Statement ps,
                                     Connection conn, ResultSet resultSet) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //通用的增删改方法
    public static void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn);
        }
    }

    //对Customer通用的查询方法,只能针对Customer类
    /*
     * 1，获取连接对象
     * 2，通过getMetaData()获取元数据
     * 3，ResultSetMetaData对象存储结果集的元数据
     * 4，通过反射把元素赋值给Cutomer对象*/
    public static Customer find(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行查询
            rs = ps.executeQuery();
            //通过ResultSetMetaData对象存储结果集的组元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Customer cust = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //给cust对象指定的ColumnName属性，腹直column：通过反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn, rs);
        }
        return null;
    }

    /**
     * 改进Customer的查询方法,只能针对Customer类
     * 获取别名代替列，解决类属性名和数据表名不同的问题
     */
    public static Customer find1(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行查询
            rs = ps.executeQuery();
            //通过ResultSetMetaData对象存储结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Customer cust = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    //String columnName = rsmd.getColumnName(i + 1);
                    //获取别名代替列，解决类属性名和数据表名不同的问题
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给cust对象指定的ColumnName属性，腹直column：通过反射
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn, rs);
        }
        return null;
    }

    //对任何类通用的查询方法
    //针对不同表，返回一条记录
    public static <T> T find2(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行查询
            rs = ps.executeQuery();
            //通过ResultSetMetaData对象存储结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                //提供空参的构造器
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    //String columnName = rsmd.getColumnName(i + 1);
                    //获取别名代替列，解决类属性名和数据表名不同的问题
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给cust对象指定的ColumnName属性，腹直column：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn, rs);
        }
        return null;
    }

    //查询多条数据
    public static <T> List<T> find3(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs=ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cloumnCount = rsmd.getColumnCount();
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < cloumnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeResource(ps, conn, rs);
        }
        return null;
    }
}
