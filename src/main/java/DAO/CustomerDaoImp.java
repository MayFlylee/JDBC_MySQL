package DAO;

import JDBC.bean.Customer;

import java.sql.Connection;
import java.util.List;

public class CustomerDaoImp extends BaseDao implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "";
        update(conn,sql,
                customer.getName(),
                customer.getAddress(),
                customer.getBirth(),
                customer.getDepartment(),
                customer.getId(),
                customer.getSex()
        );
    }

    @Override
    public void deleteById(Connection connection, int id) {

    }

    @Override
    public void updateById(Connection conn, Customer customer) {

    }

    @Override
    public void getCustomer(Connection conn, int id) {

    }

    @Override
    public List<Customer> getAll(Connection conn) {
        return null;
    }

    @Override
    public Long getCount(Connection conn) {
        return null;
    }
}
