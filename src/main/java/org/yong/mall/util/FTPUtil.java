package org.yong.mall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yong on 2017/6/13.
 */
public class FTPUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FTPUtil.class);

    private static String IP = PropertiesUtil.getProperty("ftp.server.ip", "127.0.0.1");
    private static String PORT = PropertiesUtil.getProperty("ftp.server.port", "21");
    private static String USER = PropertiesUtil.getProperty("ftp.server.user", "ftpuser");
    private static String PASS = PropertiesUtil.getProperty("ftp.server.pass", "ftp12345");

    private String ip;
    private String port;
    private String user;
    private String pass;
    private FTPClient client;

    public FTPUtil(String ip, String port, String user, String pass) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public static boolean saveFile(List<File> files) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(IP, PORT, USER, PASS);
        LOG.info("开始连接 ftp 服务器...");

        boolean result = ftpUtil.saveFile(files, "img");

        LOG.info("文件上传完成...");
        return result;
    }

    private boolean saveFile(List<File> files, String remotePath) throws IOException {
        boolean result = false;
        FileInputStream fis = null;
        if (connectFTPServer(this.getIp(), this.getPort(), this.getUser(), this.getPass())) {
            try {
                client.changeWorkingDirectory(remotePath);
                client.setBufferSize(1024);
                client.setControlEncoding("UTF-8");
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();

                for (File item : files) {
                    fis = new FileInputStream(item);
                    client.storeFile(item.getName(), fis);
                }
                result = true;
            } catch (IOException e) {
                LOG.error("上传文件异常", e);
                e.printStackTrace();
            } finally {
                fis.close();
                client.disconnect();
            }
        }
        return result;
    }

    private boolean connectFTPServer(String ip, String port, String user, String pass) {
        boolean result = false;
        client = new FTPClient();
        try {
            client.connect(ip);
            result = client.login(user, pass);
        } catch (IOException e) {
            LOG.error("连接 FTP 服务器出错", e);
            e.printStackTrace();
        }
        return result;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public FTPClient getClient() {
        return client;
    }

    public void setClient(FTPClient client) {
        this.client = client;
    }
}
