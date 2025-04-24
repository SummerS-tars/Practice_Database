package top.thesumst.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshConnectionTest {
    // SSH连接配置
    private static final String SSH_USER = "thesumst";
    private static final String SSH_HOST = "192.168.31.124";
    private static final int SSH_PORT = 22; // 默认SSH端口
    private static final String SSH_PASSWORD = "Zhu232301771902"; // 替换为您的SSH密码
    
    // 数据库连接配置
    private static final String DB_HOST = "localhost"; // 目标数据库主机
    private static final int DB_PORT = 3306; // 目标数据库端口
    private static final int LOCAL_PORT = 3307; // 本地转发端口
    
    public static void main(String[] args) {
        Session session = null;
        
        try {
            System.out.println("开始测试SSH连接...");
            
            JSch jsch = new JSch();
            
            // 创建会话
            System.out.println("创建SSH会话...");
            session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
            
            // 设置密码认证
            System.out.println("使用密码认证...");
            session.setPassword(SSH_PASSWORD);
            
            // 不进行公钥检查
            session.setConfig("StrictHostKeyChecking", "no");
            
            // 连接
            System.out.println("连接到SSH服务器...");
            session.connect(30000); // 设置超时时间为30秒
            
            if (session.isConnected()) {
                System.out.println("SSH连接成功！");
                
                // 设置端口转发
                System.out.println("设置端口转发: localhost:" + LOCAL_PORT + " -> " + DB_HOST + ":" + DB_PORT);
                session.setPortForwardingL(LOCAL_PORT, DB_HOST, DB_PORT);
                
                System.out.println("端口转发设置成功！");
                System.out.println("现在可以通过 localhost:" + LOCAL_PORT + " 访问数据库");
                
                // 保持连接一段时间以测试
                System.out.println("SSH隧道已建立，将保持60秒...");
                Thread.sleep(60000);
            } else {
                System.out.println("SSH连接失败！");
            }
        } catch (Exception e) {
            System.err.println("SSH连接测试出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭连接
            if (session != null && session.isConnected()) {
                System.out.println("关闭SSH连接...");
                session.disconnect();
                System.out.println("SSH连接已关闭。");
            }
        }
    }
}
