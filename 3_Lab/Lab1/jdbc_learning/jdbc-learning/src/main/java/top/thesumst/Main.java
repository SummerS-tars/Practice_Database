package top.thesumst;

import top.thesumst.ssh.SshTunnel;
import top.thesumst.util.ConfigLoader;
import top.thesumst.util.*;
import top.thesumst.dao.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try (SshTunnel tunnel = new SshTunnel()) {
            // 1. 建立SSH隧道
            tunnel.connect();
            System.out.println("SSH隧道建立成功");

            // 2. 构建JDBC连接
            String jdbcUrl = String.format(
                "jdbc:mysql://localhost:%d/%s?%s",
                ConfigLoader.getInt("local.port"),
                ConfigLoader.get("db.name"),
                ConfigLoader.get("jdbc.params")
            );

            // 3. 执行数据库操作
            try (Connection conn = DriverManager.getConnection(
                    jdbcUrl,
                    ConfigLoader.get("db.user"),
                    ConfigLoader.get("db.password"))) {
                
                // 3.1 测试基本连接
                testConnection(conn);
                
                // // 3.2 执行SQL文件创建表结构
                // String sqlFilePath = "e:\\_ComputerLearning\\6_Practice_Database\\3_Lab\\Lab1\\jdbc_learning\\jdbc-learning\\src\\main\\resources\\SQL\\create_first_table.sql";
                // System.out.println("\n初始化数据库表结构:");
                // SqlFileExecutor.executeSqlFile(conn, sqlFilePath);
                
                // 3.3 导入room数据
                // String roomCsvPath = "e:\\_ComputerLearning\\6_Practice_Database\\3_Lab\\Lab1\\Lab1数据\\room.csv";
                // System.out.println("\n开始导入Room表数据:");
                // int roomRows = RoomImporter.importRoomFromCsv(conn, roomCsvPath);
                // System.out.println("Room表导入完成，共导入 " + roomRows + " 行数据");
                
                // 3.4 导入student数据
                String studentCsvPath = "e:\\_ComputerLearning\\6_Practice_Database\\3_Lab\\Lab1\\Lab1数据\\student.csv";
                System.out.println("\n开始导入Student表数据:");
                int studentRows = StudentImporter.importStudentFromCsv(conn, studentCsvPath);
                System.out.println("Student表导入完成，共导入 " + studentRows + " 行数据");
            }
        } catch (Exception e) {
            System.err.println("程序运行失败:");
            e.printStackTrace();
        }
    }
    
    /**
     * 测试数据库连接
     */
    private static void testConnection(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT NOW() AS `current_time`");
            if (rs.next()) {
                System.out.println("数据库连接成功，当前时间: " + rs.getString("current_time"));
            }
        }
    }
}