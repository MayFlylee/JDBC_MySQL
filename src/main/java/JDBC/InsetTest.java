package JDBC;

import JDBC.util.JDBCUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

//批量插入功能
/*
* 使用PrepareStatment实现高效批量插入
* 方式一：
*
* 方式二：
*
* 方式三：
* */
public class InsetTest {
    //方式二，使用preparestatment
    //缺点，频繁的IO操作，速度慢，良好的代码要避免多次
    public void batchInsetTest1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            String sql = "insert into student_2 value(?)";
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 2000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end=System.currentTimeMillis();
            System.out.println("Time=" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn);
        }
    }

    /*
    * 方式三，使用Batch，批量插入。
    * 要求：jar在5.7以上，
    * 1,addBatch(),executeBatch(),clearBatch();
    * 2,mysql服务器默认是关闭批量处理，需要通过一个参数让，mysql开启支持
    *       ?rewriteBatchedStatements=true,写在配置文件后面
    * 3.需要跟新架包，mysql-connector 1.37以上
    * */

    public void batchInsetTest2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            String sql = "insert into student_2 value(?)";
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 2000; i++) {
                ps.setObject(1, "name_" + i);
                //攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            long end=System.currentTimeMillis();
            System.out.println("Time=" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(ps, conn);
        }
    }


    //方式四，继续优化
    public void batchInsetTest3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnection();

            //设置不允许自动提交，等执行完所有操作后再进行提交，提高效率
            conn.setAutoCommit(false);

            String sql = "insert into student_2 value(?)";
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 2000000; i++) {
                ps.setObject(1, "name_" + i);
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
    }

    public static void main(String[] args) {
        InsetTest it = new InsetTest();
        //it.batchInsetTest1();
        it.batchInsetTest3();
    }
}

