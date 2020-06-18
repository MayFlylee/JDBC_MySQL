package DAO;

import JDBC.bean.Customer;

import java.sql.Connection;
import java.util.List;

//此接口封装了对Customer的表的常用操作
public interface CustomerDAO {
    void insert(Connection conn, Customer customer);

    void deleteById(Connection connection, int id);

    //针对内存中cust对象，去修改数据便
    void updateById(Connection conn,Customer customer);

    void getCustomer(Connection conn, int id);

    List<Customer> getAll(Connection conn);

    Long getCount(Connection conn);





}
