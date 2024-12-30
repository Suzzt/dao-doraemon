package org.dao.doraemon.excel.storage;

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
     * @param path        保存路径
     * @param inputStream Excel文件
     * @return 下载链接
     */
    String save(String path, InputStream inputStream);

    /**
     * 删除
     *
     * @param path 保存路径
     */
    void delete(String path);

    /**
     * 获取
     *
     * @param path 保存路径
     * @return 得到文件流
     */
    InputStream get(String path);
}
