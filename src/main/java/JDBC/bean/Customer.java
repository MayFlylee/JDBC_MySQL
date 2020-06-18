package JDBC.bean;

import java.util.Date;

public class Customer {
    private int id = 0;
    private String name = null;
    private String sex = null;
    private Date birth = null;
    private String department = null;
    private String address = null;

    public Customer() {
        super();
    }

    public Customer(int id, String name, String sex, Date birth, String department, String address) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
        this.department = department;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                ", department='" + department + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
