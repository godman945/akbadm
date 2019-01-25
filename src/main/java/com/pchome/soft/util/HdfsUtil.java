package com.pchome.soft.util;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class HdfsUtil {
    private final static Log log = LogFactory.getLog(HdfsUtil.class);

    private FileSystem fileSystem;

    public HdfsUtil(String path) {
        try {
            Configuration config = new Configuration();

            List<String> list = null;
            String[] strArr = null;

            log.info("path " + path);
            if (StringUtils.isNotBlank(path)) {
                list = FileUtils.readLines(new File(path));
            }

            for (String str: list) {
                strArr = str.split("=");
                if (strArr.length == 2) {
                    config.set(strArr[0], strArr[1]);
                }
                log.info("set " + str);
            }

            fileSystem = FileSystem.get(config);
        } catch (Exception e) {
            log.error(path, e);
        }
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }
}