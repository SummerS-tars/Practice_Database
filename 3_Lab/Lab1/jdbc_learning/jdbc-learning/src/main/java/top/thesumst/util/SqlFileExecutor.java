package top.thesumst.util;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL文件执行工具类
 */
public class SqlFileExecutor {

    /**
     * 从文件中读取SQL语句
     * @param filePath SQL文件路径
     * @return SQL语句列表
     * @throws IOException 读取文件出错时抛出
     */
    public static List<String> readSqlFile(String filePath) throws IOException {
        List<String> sqlList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 忽略注释行
                if (line.trim().startsWith("--") || line.trim().startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                
                sb.append(line).append(" ");
                
                // 如果行以分号结尾，表示一条SQL语句结束
                if (line.trim().endsWith(";")) {
                    sqlList.add(sb.toString().trim());
                    sb.setLength(0); // 清空StringBuilder
                }
            }
            
            // 处理最后一条可能没有分号的SQL
            String lastSql = sb.toString().trim();
            if (!lastSql.isEmpty()) {
                sqlList.add(lastSql);
            }
        }
        
        return sqlList;
    }
    
    /**
     * 执行SQL文件
     * @param connection 数据库连接
     * @param filePath SQL文件路径
     * @throws Exception 执行过程中的异常
     */
    public static void executeSqlFile(Connection connection, String filePath) throws Exception {
        List<String> sqlStatements = readSqlFile(filePath);
        
        try (Statement stmt = connection.createStatement()) {
            System.out.println("开始执行SQL文件: " + filePath);
            System.out.println("共有 " + sqlStatements.size() + " 条SQL语句");
            
            int i = 1;
            for (String sql : sqlStatements) {
                System.out.println("执行第 " + i + " 条SQL: " + sql);
                
                // 根据SQL类型执行不同操作
                if (sql.trim().toUpperCase().startsWith("SELECT")) {
                    ResultSet rs = stmt.executeQuery(sql);
                    printResultSet(rs);
                } else {
                    int rowsAffected = stmt.executeUpdate(sql);
                    System.out.println("影响行数: " + rowsAffected);
                }
                
                i++;
            }
            
            System.out.println("SQL文件执行完成");
        }
    }
    
    /**
     * 打印查询结果
     * @param rs 结果集
     * @throws SQLException SQL异常
     */
    private static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        // 打印列名
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();
        
        // 打印分隔线
        for (int i = 1; i <= columnCount; i++) {
            System.out.print("----------\t");
        }
        System.out.println();
        
        // 打印数据
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
    }
}
