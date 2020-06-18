package JDBC;


import JDBC.bean.Customer;
import JDBC.util.JDBCUtil;
import jdk.nashorn.internal.scripts.JD;

import java.io.*;
import java.sql.*;

//存操作BLob类型数据
public class BlobTest {

    //插入blob类型的数据
    public void testInsert() throws IOException {
        Connection conn=null;
        PreparedStatement ps = null;
        FileInputStream is = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "insert into student_1 values(?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, "4");
            ps.setObject(2, "王三三");
            ps.setObject(3, "男");
            ps.setObject(4, "1998");
            ps.setObject(5, "计算机系");
            ps.setObject(6, "越秀区");
            is = new FileInputStream(new File(this.getClass().getResource("/image/p1.jpg").getPath()));
            ps.setBlob(7, is);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeResource(ps, conn);
            is.close();
        }
    }

    //查询Blob类型的字段
    public void testFindBlob() {
        Connection conn=null;
        PreparedStatement ps = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        InputStream is=null;
        String sql = "select * from student_1";
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String sex = rs.getString("sex");
                Date birth = rs.getDate("birth");
                String department = rs.getString("department");
                String address = rs.getString("address");
                Customer cus = new Customer(id, name, sex, birth, department, address);

                System.out.println(cus);

                //将Blob类型的数据下载，并以二进制的形式保存
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();

                fos = new FileOutputStream("src/main/resources/test.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeResource(ps, conn, rs);
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------");
    }

    public static void main(String [] args) {
        BlobTest bt = new BlobTest();
        bt.testFindBlob();
    }
}
