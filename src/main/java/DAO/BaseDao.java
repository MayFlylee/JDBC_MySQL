package DAO;

import JDBC.util.JDBCUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

//DAO,Data access object

//此父类封装了针对数据表的通用操作
public abstract class BaseDao {
    //通用增删改, 针对一条sql语句
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
            JDBCUtil.closeResource(ps,null);
        }
    }

    //批量插入增删改
   /* public void batchUpdate(Connection conn,String sql,int amount,Object object) {
        PreparedStatement ps = null;
        try {
            //conn = JDBCUtil.getConnection();

            //设置不允许自动提交，等执行完所有操作后再进行提交，提高效率
            conn.setAutoCommit(false);

           // String sql = "insert into student_2 value(?)";
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 1; i <= amount; i++) {
                ps.setObject(i, object);
                //攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            //统一提交数据
            conn.commit();
            long end=System.currentTimeMillis();
            System.out.println("Time=" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn);
        }
    }*/

    //通用的查询方法，查询一条数据
    public static <T> T findOneList(Connection conn,Class<T> clazz, String sql, Object... args) {
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

    //通用的查询方法，查询多条数据
    public static <T> List<T> findMoreList(Connection conn,Class<T> clazz, String sql, Object... args) {
        //Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            //conn = getConnection();
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
            JDBCUtil.closeResource(ps, null, rs);
        }
        return null;
    }

    //用于查询特殊值的方法
    public static <T> T getValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs=ps.executeQuery();
            if (rs.next()) {
                return (T) rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeResource(ps, null, rs);
        }
        return null;
    }
}
