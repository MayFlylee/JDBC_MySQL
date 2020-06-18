package JDBC.DruidConnection;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
public class DruidTest {
    // 方式二获取Connection对象
    private  static DataSource source = null;
    static {
        FileInputStream is =null;
        Properties prop = new Properties();
        try {
            is=new FileInputStream(new File("./src/main/resources/Druid.properties"));
            prop.load(is);
            source=DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @org.junit.Test
    public void druidGetConnectionTest() throws SQLException {
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
