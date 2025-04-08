package org.dao.doraemon.excel.storage;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于华为云 OBS 的 Excel 存储处理实现
 *
 * @author sucf
 * @since 1.0
 */
public class ObsExcelStorageProcessor implements ExcelStorageProcessor {

    private final ObsClient obsClient;
    private final String bucketName;
    private final String obsDomain;

    /**
     * 构造方法，初始化 OBS 客户端
     *
     * @param endpoint        OBS 服务地址
     * @param accessKeyId     华为云 AccessKey ID
     * @param accessKeySecret 华为云 AccessKey Secret
     * @param bucketName      OBS Bucket 名称
     * @param obsDomain       OBS 绑定的访问域名
     */
    public ObsExcelStorageProcessor(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String obsDomain) {
        this.obsClient = new ObsClient(accessKeyId, accessKeySecret, endpoint);
        this.bucketName = bucketName;
        this.obsDomain = obsDomain;
    }

    @Override
    public String submit(String fileName, Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            byte[] content = outputStream.toByteArray();
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(fileName);
            request.setInput(new ByteArrayInputStream(content));
            obsClient.putObject(request);
            return String.format("%s/%s", obsDomain, fileName);
        } catch (IOException e) {
            throw new RuntimeException("上传 Excel 文件到 OBS 失败", e);
        }
    }

    @Override
    public InputStream download(String path) {
        return null;
    }
}
