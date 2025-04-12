package org.dao.doraemon.excel.storage;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(LocalExcelStorageProcessor.class);

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
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(ipAddress)
                .port(serverPort);

        String normalizedContextPath = formatContextPath(contextPath);
        if (StringUtils.hasLength(normalizedContextPath)) {
            builder.pathSegment(normalizedContextPath.split("/"));
        }

        return builder.path("/excel/download/{fileName}")
                .buildAndExpand(fileName)
                .toUriString(); }

    @Override
    public InputStream download(String path) {
        File file = new File(storageLocalPath + File.separator + path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        LOGGER.error("文件不存在：{}", file.getAbsolutePath());
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

    private String formatContextPath(String path) {
        if (!StringUtils.hasLength(path)) return "";
        return path.replaceAll("^/+|/+$", "");
    }
}
