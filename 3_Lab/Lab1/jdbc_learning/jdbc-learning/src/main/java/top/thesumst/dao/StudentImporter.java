package top.thesumst.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Student表专用导入类，支持灵活的列名映射和空值处理
 */
public class StudentImporter {
    // 表名和列名常量
    private static final String TABLE_NAME = "student";
    
    // 必需的列和可能的别名映射
    private static final String[][] COLUMN_ALIASES = {
        {"registno", "学号", "reg_no", "id"},
        {"name", "姓名", "student_name"},
        {"kdno", "考点号", "kd_no", "department_no"},
        {"kcno", "考场号", "kc_no", "room_no"},
        {"ccno", "场次号", "cc_no", "session_no"},
        {"seat", "座位号", "seat_no"}
    };
    
    /**
     * 从CSV导入Student表数据
     * @param connection 数据库连接
     * @param csvFilePath CSV文件路径
     * @return 导入的记录数
     */
    public static int importStudentFromCsv(Connection connection, String csvFilePath) 
            throws Exception {
        String sql = "INSERT INTO student (registno, name, kdno, kcno, ccno, seat) VALUES (?, ?, ?, ?, ?, ?)";
        int importedRows = 0;
        int skippedRows = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath), 16384); // 增加缓冲区大小
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            String line;
            int lineNumber = 0;
            
            // 读取标题行并获取列映射
            String headerLine = reader.readLine();
            lineNumber++;
            
            if (headerLine == null) {
                throw new Exception("CSV文件为空或格式不正确");
            }
            
            System.out.println("解析CSV标题行: " + headerLine);
            
            String[] headers = parseCSVLine(headerLine);
            System.out.println("解析到标题: " + Arrays.toString(headers));
            
            // 创建列名映射，支持别名
            Map<String, Integer> columnMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim().toLowerCase();
                columnMap.put(header, i);
                
                // 打印每个列名
                System.out.println("列名: '" + header + "' 索引: " + i);
            }
            
            // 创建实际列名到CSV列索引的映射
            Map<String, Integer> actualColumnMap = new HashMap<>();
            for (String[] aliases : COLUMN_ALIASES) {
                String columnName = aliases[0]; // 实际列名
                
                // 尝试所有可能的别名
                for (String alias : aliases) {
                    Integer index = columnMap.get(alias.toLowerCase());
                    if (index != null) {
                        actualColumnMap.put(columnName, index);
                        System.out.println("找到列 '" + columnName + "' 的别名: '" + alias + "' 索引: " + index);
                        break;
                    }
                }
                
                // 如果找不到这一列
                if (!actualColumnMap.containsKey(columnName)) {
                    System.err.println("警告: CSV中找不到 '" + columnName + "' 列或其别名");
                    System.err.println("可用的列名: " + columnMap.keySet());
                    throw new Exception("CSV缺少必需列: " + columnName + " 或其别名");
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
                    
                    // 解析CSV行 - 使用改进的解析方法
                    String[] fields = parseCSVLine(line);
                    
                    // 检查是否有足够的字段
                    boolean hasAllFields = true;
                    for (String column : Arrays.asList("registno", "name", "kdno", "kcno", "ccno", "seat")) {
                        Integer index = actualColumnMap.get(column);
                        if (index == null || index >= fields.length) {
                            hasAllFields = false;
                            break;
                        }
                    }
                    
                    if (!hasAllFields) {
                        System.err.println("警告: 行 " + lineNumber + " 字段数不足，跳过此行");
                        skippedRows++;
                        continue;
                    }
                    
                    try {
                        // 从映射中获取字段值
                        String registno = getValueByColumnIndex(fields, actualColumnMap.get("registno"));
                        String name = getValueByColumnIndex(fields, actualColumnMap.get("name"));
                        String kdnoStr = getValueByColumnIndex(fields, actualColumnMap.get("kdno"));
                        String kcnoStr = getValueByColumnIndex(fields, actualColumnMap.get("kcno"));
                        String ccnoStr = getValueByColumnIndex(fields, actualColumnMap.get("ccno"));
                        String seatStr = getValueByColumnIndex(fields, actualColumnMap.get("seat"));
                        
                        // 检查必需字段是否有值
                        if (isEmpty(registno) || isEmpty(name) || isEmpty(kdnoStr) ||
                            isEmpty(kcnoStr) || isEmpty(ccnoStr) || isEmpty(seatStr)) {
                            System.err.println("警告: 行 " + lineNumber + " 缺少必需字段值，跳过此行");
                            skippedRows++;
                            continue;
                        }
                        
                        // 验证学号长度
                        if (registno.length() > 7) {
                            System.out.println("警告: 行 " + lineNumber + " 学号长度超过7个字符，截断处理: " + registno);
                            registno = registno.substring(0, 7);
                        }
                        
                        // 数据类型转换
                        int kdno = Integer.parseInt(kdnoStr.trim());
                        int kcno = Integer.parseInt(kcnoStr.trim());
                        int ccno = Integer.parseInt(ccnoStr.trim());
                        int seat = Integer.parseInt(seatStr.trim());
                        
                        // 设置PreparedStatement参数
                        pstmt.setString(1, registno);
                        pstmt.setString(2, name);
                        pstmt.setInt(3, kdno);
                        pstmt.setInt(4, kcno);
                        pstmt.setInt(5, ccno);
                        pstmt.setInt(6, seat);
                        
                        // 执行插入
                        pstmt.executeUpdate();
                        importedRows++;
                        
                        // 每100行输出一次进度
                        if (importedRows % 100 == 0) {
                            System.out.println("已导入 " + importedRows + " 行数据");
                        }
                        
                    } catch (NumberFormatException e) {
                        System.err.println("警告: 行 " + lineNumber + " 数值转换错误，跳过此行");
                        System.err.println("错误详情: " + e.getMessage());
                        skippedRows++;
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.err.println("警告: 行 " + lineNumber + " 违反完整性约束，跳过此行");
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
                System.out.println("Student表导入完成");
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
     * 根据列索引从字段数组中获取值
     */
    private static String getValueByColumnIndex(String[] fields, Integer index) {
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
