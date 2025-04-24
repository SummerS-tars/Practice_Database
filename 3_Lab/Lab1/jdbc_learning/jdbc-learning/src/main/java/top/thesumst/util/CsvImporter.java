package top.thesumst.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * CSV文件导入工具类
 */
public class CsvImporter {

    /**
     * 通用CSV导入方法
     * @param connection 数据库连接
     * @param csvFilePath CSV文件路径
     * @param tableName 表名
     * @param columnNames 列名数组
     * @param hasHeader CSV是否有标题行
     * @param batchSize 批处理大小
     * @return 导入的记录数
     */
    public static int importCsvToTable(Connection connection, String csvFilePath, 
                                      String tableName, String[] columnNames, 
                                      boolean hasHeader, int batchSize) throws Exception {
        
        // 构建INSERT语句
        StringBuilder sql = new StringBuilder("INSERT INTO ")
            .append(tableName)
            .append(" (");
        
        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) sql.append(", ");
            sql.append(columnNames[i]);
        }
        
        sql.append(") VALUES (");
        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) sql.append(", ");
            sql.append("?");
        }
        sql.append(")");
        
        int importedRowCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            
            String line;
            int count = 0;
            boolean isFirstLine = true;
            
            // 开始事务
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            
            try {
                while ((line = reader.readLine()) != null) {
                    // 跳过标题行
                    if (isFirstLine && hasHeader) {
                        isFirstLine = false;
                        continue;
                    }
                    
                    // 分割CSV行
                    String[] fields = parseCSVLine(line);
                    
                    // 检查字段数
                    if (fields.length != columnNames.length) {
                        System.err.println("警告: 行 " + (importedRowCount + 1) + " 的字段数量不匹配，跳过此行");
                        continue;
                    }
                    
                    // 设置参数
                    for (int i = 0; i < fields.length; i++) {
                        pstmt.setString(i + 1, fields[i].trim());
                    }
                    
                    pstmt.addBatch();
                    count++;
                    
                    // 执行批处理
                    if (count % batchSize == 0) {
                        int[] results = pstmt.executeBatch();
                        importedRowCount += sumArray(results);
                        System.out.println("已导入 " + importedRowCount + " 行数据");
                    }
                }
                
                // 处理剩余的批次
                if (count % batchSize != 0) {
                    int[] results = pstmt.executeBatch();
                    importedRowCount += sumArray(results);
                }
                
                // 提交事务
                connection.commit();
                
                System.out.println("CSV导入完成，共导入 " + importedRowCount + " 行数据到表 " + tableName);
                
            } catch (Exception e) {
                // 出现异常，回滚事务
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        }
        
        return importedRowCount;
    }
    
    /**
     * 带数据转换的CSV导入方法
     * @param connection 数据库连接
     * @param csvFilePath CSV文件路径
     * @param sqlPreparer SQL准备函数，接收一行CSV数据，返回完整的PreparedStatement设置逻辑
     * @param hasHeader CSV是否有标题行
     * @param batchSize 批处理大小
     * @return 导入的记录数
     */
    public static int importCsvWithTransformation(Connection connection, String csvFilePath,
                                               Function<String[], PreparedStatement> sqlPreparer,
                                               boolean hasHeader, int batchSize) throws Exception {
        // 实现带自定义转换的导入逻辑
        // 此处略
        return 0;
    }
    
    /**
     * 解析CSV行，处理引号等复杂情况
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // 处理引号
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // 双引号表示转义
                    field.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // 非引号内的逗号表示字段分隔符
                result.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        
        // 添加最后一个字段
        result.add(field.toString());
        
        return result.toArray(new String[0]);
    }
    
    /**
     * 计算批处理结果数组的和
     */
    private static int sumArray(int[] array) {
        int sum = 0;
        for (int value : array) {
            if (value > 0) sum += value;
        }
        return sum;
    }
}
