package com.pchome.akbadm.factory.store;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public abstract class AbstractStore {
    protected Logger log = LogManager.getRootLogger();

    protected String fsDefaultName;
    protected String hadoopJobUgi;
    protected String savePath;

    public void put() throws Exception {
        this.put(Calendar.getInstance().getTime());
    }

    public void put(Date date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hdf = new SimpleDateFormat("yyyy-MM-dd" + File.separator + "HH");

        StringBuffer src = new StringBuffer();
        src.append(savePath).append(File.separator);
        src.append(sdf.format(date)).append(File.separator);

        // check dir
        File dir = new File(src.toString());
        if (!dir.isDirectory()) {
            log.info(dir.getPath() + " is not dir");
            return;
        }

        StringBuffer dest = new StringBuffer();
        dest.append(savePath).append(File.separator);
        dest.append(hdf.format(date));
        Path destPath = new Path(dest.toString());
        StringBuffer remoteStr = null;
        File file = null;
        int count = 0;

        // hdfs
        Configuration conf = new Configuration();
        conf.set("hadoop.job.ugi", hadoopJobUgi);
        conf.set("fs.default.name", fsDefaultName);
        FileSystem fs = null;
        Path okPath = null;

        try {
            fs = FileSystem.get(conf);

            // mkdirs
            log.info("hdfs mkdirs " + destPath);
            fs.mkdirs(destPath);

            File newFile = null;
            for (File hostDir: dir.listFiles()) {
                if (!hostDir.isDirectory()) {
                    log.info(hostDir.getPath() + " is not dir");
                    continue;
                }

                for (Object obj: FileUtils.listFiles(hostDir, new String[]{"txt"}, true)) {
                    file = (File) obj;

                    // put
                    remoteStr = new StringBuffer();
                    remoteStr.append(dest.toString());
                    remoteStr.append(file.getParent().substring(file.getParent().lastIndexOf(File.separator)));
                    remoteStr.append("-");
                    remoteStr.append(file.getName());

                    log.info("hdfs copy " + file.getPath() + " " + remoteStr);
                    fs.copyFromLocalFile(new Path(file.getPath()), new Path(remoteStr.toString()));

                    // rename
                    newFile = new File(file.getPath().replace(".txt", ".ok"));

                    log.info("rename " + file.getPath() + " " + newFile.getPath());
                    file.renameTo(newFile);

                    count++;
                }
            }

            log.info("update " + count);

            // create ok.txt
            if (count > 0) {
                okPath = new Path(dest.toString() + Path.SEPARATOR + "ok.txt");

                log.info("hdfs create " + okPath);
                fs.create(okPath).close();
            }
            else {
                fs.delete(destPath, true);
            }
        } catch (Exception e) {
            log.error(file != null ? file.getPath() : dest, e);
            throw e;
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
    }

    public void setFsDefaultName(String fsDefaultName) {
        this.fsDefaultName = fsDefaultName;
    }

    public void setHadoopJobUgi(String hadoopJobUgi) {
        this.hadoopJobUgi = hadoopJobUgi;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}