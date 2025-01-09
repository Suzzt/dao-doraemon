package org.dao.doraemon.excel.storage;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 本地Excel存储管理
 *
 * @author sucf
 * @since 1.0
 */
@Component
public class LocalExcelStorageProcessor implements ExcelStorageProcessor {

    @Value(value = "${server.servlet.context-path:#{null}}")
    private String contextPath;

    @Value(value = "${server.port:8080}")
    private String serverPort;

    private final String storageLocalPath;

    public LocalExcelStorageProcessor(String storageLocalPath) {
        this.storageLocalPath = storageLocalPath;
    }

    @Override
    public String submit(String fileName, Workbook workbook) {
        String fullPath = storageLocalPath + File.separator + fileName;

        File file = new File(fullPath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new RuntimeException("无法创建目录：" + parentDir.getAbsolutePath());
            }
        }

        // 创建文件并写入数据
        try (FileOutputStream fileOutputStream = new FileOutputStream(fullPath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String ipAddress = getIpAddress();
        if (StringUtils.hasLength(contextPath)) {
            contextPath = contextPath.startsWith("/") ? contextPath : "/" + contextPath;
        }
        return "http://" + ipAddress + ":" + serverPort + (contextPath != null ? contextPath : "") + "/excel/download/" + fileName;
    }

    @Override
    public InputStream download(String path) {
        File file = new File(storageLocalPath + path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private String getIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof java.net.Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return "127.0.0.1";
    }
}
