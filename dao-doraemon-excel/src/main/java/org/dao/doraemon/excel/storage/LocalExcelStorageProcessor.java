package org.dao.doraemon.excel.storage;

import cn.hutool.core.io.IoUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地Excel存储管理
 *
 * @author sucf
 * @create_time 2024/12/28 22:35
 */
@Component
public class LocalExcelStorageProcessor implements ExcelStorageProcessor {

    @Override
    public String save(String path, InputStream inputStream) {
        File file = new File(path);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new RuntimeException("无法创建目录：" + parentDir.getAbsolutePath());
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            IoUtil.copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败", e);
        }
        return null;
    }

    @Override
    public void delete(String path) {

    }

    @Override
    public InputStream get(String path) {
        return null;
    }
}
