package org.dao.doraemon.excel.storage;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;

/**
 * 定义Excel存储处理管理
 *
 * @author sucf
 * @create_time 2024/12/28 22:29
 */
public interface ExcelStorageProcessor {
    /**
     * 保存
     *
     * @param fileName 下载时文件名
     * @param workbook Excel文件
     * @return 下载链接
     */
    String submit(String fileName, Workbook workbook);

    /**
     * 删除
     *
     * @param path 保存路径
     */
    void delete(String path);

    /**
     * 获取下载文件
     *
     * @param path 文件路径
     * @return 得到文件流
     */
    InputStream download(String path);
}