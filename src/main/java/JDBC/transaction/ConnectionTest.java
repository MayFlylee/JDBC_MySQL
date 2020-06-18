package JDBC.transaction;

import JDBC.bean.Customer;
import JDBC.transaction.bean.User;
import JDBC.util.JDBCUtil;
import jdk.nashorn.internal.scripts.JD;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionTest {


    /*
     * 支持事务的JDBC更新程序
     * 支持回滚 * */
    public void testUpdateWithTx() throws Exception {
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            conn.setAutoCommit(false);

            String sql1 = "update student_balance set balance = ? where user_id=?";
            update(conn, sql1, 80, 1);
            //  System.out.println(10/0);
            String sql2 = "update student_balance set balance = ? where user_id=?";
            update(conn, sql2, 120, 2);
            conn.commit();
            System.out.println("转账成功");
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            System.out.println("服务器繁忙");
        } finally {
            JDBCUtil.closeResource(null, conn);
        }
    }


    /*
    * testTransactionSelect和testTransactionFind用于演示两个事务同时访问一个表
    * 演示事务个隔离级别设置
    * 最终目的：避免脏读
    * */
    public static void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        //获取当前数据库的隔离级别
        System.out.println(conn);
        //设置数据库的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交
        conn.setAutoCommit(false);
        String sql = "Select*from student_balance where user_id=?";
        User user = find2(conn, User.class, sql, 1);
        JDBCUtil.closeResource(null, conn);
        System.out.println(user);
    }

    public static void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        String sql = "Update student_balance set balance where user_id=?";
        update(conn, sql, 1);
        Thread.sleep(500);
        JDBCUtil.closeResource(null, conn);
    }

    //通用的支持事务的跟新方法
    public static void update(Connection conn, String sql, Object... args) {
        //Connection conn = null;
        PreparedStatement ps = null;
        try {
            //conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //不需要自动关闭conn连接，因为关闭后会自动提交事务
        }
    }

    //支持事务的通用的查询方法
    public static <T> T find2(Connection conn,Class<T> clazz, String sql, Object... args) {
//        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //conn = JDBCUtil.getConnection();
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
            JDBCUtil.closeResource(ps, null, rs);
        }
        return null;
    }



    public static void main(String[] args) throws Exception {
        ConnectionTest ct = new ConnectionTest();
        //ct.testUpdateWithTx();
        ct.testTransactionSelect();
    }
}
