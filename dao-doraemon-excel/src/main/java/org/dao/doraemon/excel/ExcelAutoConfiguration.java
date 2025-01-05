package org.dao.doraemon.excel;

import com.alibaba.excel.util.StringUtils;
import org.dao.doraemon.excel.imported.Dispatcher;
import org.dao.doraemon.excel.server.ExcelHttpServer;
import org.dao.doraemon.excel.storage.ExcelStorageProcessor;
import org.dao.doraemon.excel.storage.LocalExcelStorageProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * Auto-Configuration for Excel.
 * 针对excel项目在SpringBoot日常项目中一些能力的封装运用.
 * 它提供的能力有:
 * 1.Excel导入数据, 统一封装获取数据来处理你的业务, 统一返回封装结果来响应你的业务.
 * 2.Excel导出数据, 统一返回封装结果来响应你的业务.
 * 3.可设置模板文件来下载你的业务场景所需要的.
 * 4.统一的服务接口暴露提供入口.
 *
 * @author sucf
 * @since 1.0
 * @see ExcelHttpServer
 */
@Configuration
@Import(Dispatcher.class)
public class ExcelAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Bean
    public ExcelHttpServer excelHttpServer(Dispatcher dispatcher) {
        return new ExcelHttpServer(dispatcher);
    }

    @Bean
    @ConditionalOnMissingBean(ExcelStorageProcessor.class)
    public ExcelStorageProcessor excelStorageProcessor() {
        String localPath  = environment.getProperty("dao_doraemon.excel.storage.local_path");
        localPath = StringUtils.isBlank(localPath) ? "/data/excel/storage/" : localPath;
        return new LocalExcelStorageProcessor(localPath);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
