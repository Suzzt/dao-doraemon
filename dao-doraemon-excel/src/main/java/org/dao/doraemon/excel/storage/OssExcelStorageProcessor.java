package org.dao.doraemon.excel.storage;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于阿里云 OSS 的 Excel 存储处理实现
 *
 * @author sucf
 * @since 1.0
 */
public class OssExcelStorageProcessor implements ExcelStorageProcessor {

    private final OSS ossClient;
    private final String bucketName;
    private final String ossDomain;

    /**
     * 构造方法，初始化 OSS 客户端
     *
     * @param endpoint        OSS 服务地址
     * @param accessKeyId     阿里云 AccessKey ID
     * @param accessKeySecret 阿里云 AccessKey Secret
     * @param bucketName      OSS Bucket 名称
     * @param ossDomain       OSS 绑定的访问域名
     */
    public OssExcelStorageProcessor(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String ossDomain) {
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        this.bucketName = bucketName;
        this.ossDomain = ossDomain;
    }

    @Override
    public String submit(String fileName, Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            byte[] content = outputStream.toByteArray();

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(content));
            ossClient.putObject(putObjectRequest);

            return String.format("%s/%s", ossDomain, fileName);
        } catch (IOException e) {
            throw new RuntimeException("上传 Excel 文件到 OSS 失败", e);
        }
    }

    @Override
    public InputStream download(String path) {
        return null;
    }
}
