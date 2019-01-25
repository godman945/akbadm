package com.pchome.soft.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpringZipCompress {
    private static final Log log = LogFactory.getLog(SpringZipCompress.class);

    public InputStream getZipStream(Map<String,byte[]> contentMap){
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(fos);

        ByteArrayInputStream in;

        byte[] buffer = new byte[1024];

        String test;
        int len;

        try {
            for(String fileName:contentMap.keySet()){
                ZipEntry ze= new ZipEntry(fileName);
                try {
                    zos.putNextEntry(ze);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                in=new ByteArrayInputStream(contentMap.get(fileName));
                len=0;

                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
            }

            //remember close it
            zos.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new ByteArrayInputStream(fos.toByteArray());
    }
}