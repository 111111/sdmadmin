package com.sdmadmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * com.sdmadmin.config说明:
 * Created by qinyun
 * 18/5/29 22:41
 */
@Component
@ConfigurationProperties(prefix = "sdmadmin.sys")
public class SysConfig {

    @Value("${sdm.sys.file.dir}")
    private String fileDir;

    @Value("${sdm.sys.upload.path}")
    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
