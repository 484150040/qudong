package com.isycores.driver.utils;

import java.sql.*;

public class DBConnection {

    private Connection con;            //定义数据库连接类对象
    private PreparedStatement pstm;
    private String user = "sa";        //连接数据库用户名
    private String password = "zjKfb_400";        //连接数据库密码
    private String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";    //数据库驱动
    private String url = "jdbc:sqlserver://172.17.1.155:1433;database=sjtb";
    //连接数据库的URL,后面的是为了防止插入数据 库出现乱码，?useUnicode=true&characterEncoding=UTF-8
//构造函数
    public DBConnection() {

    }

    /**
     * 创建数据库连接
     */
    public Connection getConnection() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("加载数据库驱动失败！");
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(url, user, password);        //获取数据库连接
        } catch (SQLException e) {
            System.out.println("创建数据库连接失败！");
            con = null;
            e.printStackTrace();
        }
        return con;                    //返回数据库连接对象
    }

    public static void main(String[] args) {
        Connection mConnection = new DBConnection().getConnection();
        if (mConnection != null) {
            try {
                String sql = "select * from m_base_term where termid=408";
                PreparedStatement pstm = mConnection.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("updatedt"));
                }
                rs.close();
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mConnection != null) {
                        mConnection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}