package top.thesumst.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import top.thesumst.util.ConfigLoader;

public class SshTunnel implements AutoCloseable {
    private Session session;

    public void connect() throws Exception {
        JSch jsch = new JSch();
        
        // 创建会话
        session = jsch.getSession(
            ConfigLoader.get("ssh.user"),
            ConfigLoader.get("ssh.host"),
            ConfigLoader.getInt("ssh.port")
        );
        
        // 设置密码认证
        session.setPassword(ConfigLoader.get("ssh.password"));
        session.setConfig("StrictHostKeyChecking", "no");
        
        // 连接SSH服务器
        session.connect();

        // 端口转发
        int localPort = ConfigLoader.getInt("local.port");
        session.setPortForwardingL(
            localPort,
            ConfigLoader.get("db.host"),
            ConfigLoader.getInt("db.port")
        );
    }

    @Override
    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}