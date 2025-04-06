package top.thesumst.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Room表专用导入类，支持列名映射和空值处理
 */
public class RoomImporter {
    // 表名和列名常量
    private static final String TABLE_NAME = "room";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    // 必需的列
    private static final String[] REQUIRED_COLUMNS = {"kdno", "kcno", "ccno", "kdname", "exptime"};
    
    /**
     * 从CSV导入Room表数据
     * @param connection 数据库连接
     * @param csvFilePath CSV文件路径
     * @return 导入的记录数
     */
    public static int importRoomFromCsv(Connection connection, String csvFilePath) 
            throws Exception {
        String sql = "INSERT INTO room (kdno, kcno, ccno, kdname, exptime, papername) VALUES (?, ?, ?, ?, ?, ?)";
        int importedRows = 0;
        int skippedRows = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            String line;
            int lineNumber = 0;
            
            // 读取标题行并获取列映射
            String headerLine = reader.readLine();
            lineNumber++;
            
            if (headerLine == null) {
                throw new Exception("CSV文件为空或格式不正确");
            }
            
            Map<String, Integer> columnMap = parseHeader(headerLine);
            
            // 检查必需列是否存在
            for (String requiredColumn : REQUIRED_COLUMNS) {
                if (!columnMap.containsKey(requiredColumn)) {
                    throw new Exception("CSV缺少必需列: " + requiredColumn);
                }
            }
            
            // 开始事务
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            
            try {
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    
                    // 跳过空行
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    
                    // 解析CSV行 - 改进的解析方法
                    String[] fields = parseCSVLine(line);
                    
                    // 检查解析出的字段是否少于必需的字段
                    boolean missingRequiredField = false;
                    for (String requiredColumn : REQUIRED_COLUMNS) {
                        Integer index = columnMap.get(requiredColumn);
                        if (index == null || index >= fields.length || isEmpty(fields[index])) {
                            missingRequiredField = true;
                            break;
                        }
                    }
                    
                    if (missingRequiredField) {
                        System.err.println("警告: 行 " + lineNumber + " 缺少必需字段，跳过此行: " + line);
                        skippedRows++;
                        continue;
                    }
                    
                    try {
                        // 从映射中获取字段值并进行转换
                        String kdnoStr = getValueByColumnName(fields, columnMap, "kdno");
                        String kcnoStr = getValueByColumnName(fields, columnMap, "kcno");
                        String ccnoStr = getValueByColumnName(fields, columnMap, "ccno");
                        String kdname = getValueByColumnName(fields, columnMap, "kdname");
                        String exptimeStr = getValueByColumnName(fields, columnMap, "exptime");
                        String papername = getValueByColumnName(fields, columnMap, "papername");
                        
                        // 检查必需字段是否有值
                        if (isEmpty(kdnoStr) || isEmpty(kcnoStr) || isEmpty(ccnoStr) || 
                            isEmpty(kdname) || isEmpty(exptimeStr)) {
                            System.err.println("警告: 行 " + lineNumber + " 缺少必需字段值，跳过此行");
                            skippedRows++;
                            continue;
                        }
                        
                        // 数据类型转换
                        int kdno = Integer.parseInt(kdnoStr.trim());
                        int kcno = Integer.parseInt(kcnoStr.trim());
                        int ccno = Integer.parseInt(ccnoStr.trim());
                        
                        // 日期时间转换
                        Timestamp exptime = parseDateTime(exptimeStr);
                        if (exptime == null) {
                            System.err.println("警告: 行 " + lineNumber + " 日期格式不正确，跳过此行");
                            skippedRows++;
                            continue;
                        }
                        
                        // 设置PreparedStatement参数
                        pstmt.setInt(1, kdno);
                        pstmt.setInt(2, kcno);
                        pstmt.setInt(3, ccno);
                        pstmt.setString(4, kdname);
                        pstmt.setTimestamp(5, exptime);
                        
                        // papername可以为空
                        if (isEmpty(papername)) {
                            pstmt.setNull(6, Types.VARCHAR);
                        } else {
                            pstmt.setString(6, papername);
                        }
                        
                        // 执行插入
                        pstmt.executeUpdate();
                        importedRows++;
                        
                        // 每100行输出一次进度
                        if (importedRows % 100 == 0) {
                            System.out.println("已处理 " + importedRows + " 行数据");
                        }
                        
                    } catch (NumberFormatException e) {
                        System.err.println("警告: 行 " + lineNumber + " 数值转换错误，跳过此行");
                        System.err.println("错误详情: " + e.getMessage());
                        skippedRows++;
                    } catch (Exception e) {
                        System.err.println("警告: 处理行 " + lineNumber + " 时出错，跳过此行");
                        System.err.println("错误详情: " + e.getMessage());
                        skippedRows++;
                    }
                }
                
                // 提交事务
                connection.commit();
                System.out.println("Room表导入完成");
                System.out.println("成功导入: " + importedRows + " 行");
                System.out.println("跳过行数: " + skippedRows + " 行");
                
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException("导入过程中出错", e);
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        }
        
        return importedRows;
    }
    
    /**
     * 解析CSV标题行，创建列名到索引的映射
     */
    private static Map<String, Integer> parseHeader(String headerLine) {
        String[] headers = parseCSVLine(headerLine);
        Map<String, Integer> columnMap = new HashMap<>();
        
        for (int i = 0; i < headers.length; i++) {
            String columnName = headers[i].trim().toLowerCase();
            columnMap.put(columnName, i);
        }
        
        return columnMap;
    }
    
    /**
     * 根据列名从字段数组中获取值
     */
    private static String getValueByColumnName(String[] fields, Map<String, Integer> columnMap, String columnName) {
        Integer index = columnMap.get(columnName.toLowerCase());
        if (index == null || index >= fields.length) {
            return null;
        }
        return fields[index];
    }
    
    /**
     * 检查字符串是否为空
     */
    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 解析日期时间字符串
     */
    private static Timestamp parseDateTime(String dateTimeStr) {
        if (isEmpty(dateTimeStr)) {
            return null;
        }
        
        try {
            // 处理可能存在的多余空格
            dateTimeStr = dateTimeStr.trim();
            return new Timestamp(DATE_FORMAT.parse(dateTimeStr).getTime());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 解析CSV行，改进处理逗号分隔和引号包裹的值
     */
    private static String[] parseCSVLine(String line) {
        if (line == null) {
            return new String[0];
        }
        
        // 改进的CSV解析逻辑
        java.util.List<String> tokens = new java.util.ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // 如果引号后面紧跟引号，认为是转义引号
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++; // 跳过下一个引号
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // 逗号作为分隔符，但不在引号内
                tokens.add(sb.toString().trim());
                sb.setLength(0); // 清空缓冲区
            } else {
                sb.append(c);
            }
        }
        
        // 添加最后一个字段
        tokens.add(sb.toString().trim());
        
        return tokens.toArray(new String[0]);
    }
}
