package utils;
/*
@author:zhengzhao
@time: 2018/08/20 
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConn {
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/shopp?useUnicode=true&characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "root";
    private static Connection conn = null;

    static {
        try{
            Class.forName(driver);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception {
        if (conn == null) {
            conn = DriverManager.getConnection(url,username,password);
            return conn;
        }
        return conn;
    }
    public static void setUsername(String username) {
        DBConn.username = username;
    }

    public static void setPassword(String password) {
        DBConn.password = password;
    }
    private boolean closeConn(Connection conn){
        try{
            conn.close();
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private boolean insert(String sql)throws Exception{
        Statement statement = conn.createStatement();
        int value = statement.executeUpdate(sql);
        if (value>0){
            return true;
        }
        else {
            return false;
        }
    }

    private ResultSet select(String sql) throws Exception{
        Statement statement = conn.createStatement();
        return statement.executeQuery(sql);
    }

    private boolean delet(String sql)throws Exception{
        Statement statement = conn.createStatement();
        int value = statement.executeUpdate(sql);
        if (value>0){
            return true;
        }
        else {
            return false;
        }
    }



    public static void main(String[] args) {
        try{
            Connection c = DBConn.getConn();
            if (c == null) {
                System.out.println("连接失败");
            }
            else {
                System.out.println("连接成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("出现异常");
        }
    }





}
