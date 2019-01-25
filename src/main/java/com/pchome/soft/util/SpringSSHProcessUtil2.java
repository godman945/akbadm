package com.pchome.soft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * @author nicolee
 */
public class SpringSSHProcessUtil2 {
    private static final Log log = LogFactory.getLog(SpringSSHProcessUtil2.class);

    private String host;
    private int port;
    private String username = "";
    private String password = "";
    private String charSet = "UTF-8";

    private void put(ChannelSftp channelSftp, File srcDir, File destDir, String[] extensions, String renameExts, boolean recursive) throws Exception {
        InputStream in = null;
        OutputStream out = null;

//        log.info("srcDir = " + srcDir.getPath());

        if (srcDir.isFile()) {
            try {
                if (checkExtension(srcDir.getName(), extensions)) {
                    return;
                }

                sshExec("mkdir -p " + destDir.getPath());
                channelSftp.cd(destDir.getPath());

                in = new FileInputStream(srcDir.getPath());
                out = channelSftp.put(srcDir.getName());

                byte[] b = new byte[1024];

                while (true) {
                    int len = in.read(b, 0, b.length);
                    if (len <= 0)
                        break;
                    out.write(b, 0, len); // out.flush();
                }

                out.flush();

//                log.info("put " + destDir.getPath() + File.separator + srcDir.getName());

                // rename extension
                if (StringUtils.isBlank(renameExts)) {
                    return;
                }

                int pos = srcDir.getName().lastIndexOf(".");
                if (pos < 0) {
                    return;
                }

                srcDir.renameTo(new File(srcDir.getPath().replace(srcDir.getName().substring(pos), renameExts)));
            } catch (Exception e) {
                log.error(srcDir.getPath(), e);
                throw e;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        log.error("out close error", e);
                        throw e;
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        log.error("in close error", e);
                        throw e;
                    }
                }
            }
        }
        else if (srcDir.isDirectory()) {
            if (!recursive) {
                return;
            }

            for (File file: srcDir.listFiles()) {
                put(channelSftp, file, new File(destDir.getPath() + File.separator + srcDir.getName()), extensions, renameExts, recursive);
            }
        }
    }

    /**
     * check file extension
     * @param filename
     * @param extensions
     * @return true if extension equals, false if not
     */
    public boolean checkExtension(String filename, String[] extensions) {
        if (extensions == null) {
            return false;
        }

        for (String extension: extensions) {
            if (filename.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 執行 ssh 遠端命令
     * @param command command
     * @return execute result
     */
    public String sshExec(String command) {
        String result = "";

//        log.info("host = " + host);
//        log.info("command = " + command);

        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;

        StringBuffer sb = new StringBuffer();
        InputStream in = null;

        try {
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no"); // ask | yes | no

            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(prop);
            session.connect();

            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.connect();

//            log.info("charSet = " + charSet);

//            InputStreamReader in = new InputStreamReader(channel.getInputStream(), charSet);

            in = channelExec.getInputStream();

            int nextChar = 0;

            while ((nextChar = in.read()) != -1) {
                sb.append((char) nextChar);
            }

            result = sb.toString();
//            result = new URLCodec().decode(sb.toString(), charSet);

            /*
            if(charSet == "BIG5"){
                String str = "";
                str = new URLCodec().encode(Result,"UTF8");
                log.info("----nicolog--------------------------------------");
                log.info("nicolog->utf8 code"+str);
                Result = "";
                Result = new URLCodec().decode(str,"UTF8");
                log.info("nicolog->utf8 word"+Result);
                log.info("----nicolog--------------------------------------");
            }
            */
        } catch (Exception e) {
            log.error(host, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                log.error("InputStream close error", e);
            }
            if ((channelExec != null) && channelExec.isConnected()) {
                channelExec.disconnect();
            }
            if ((session != null) && session.isConnected()) {
                session.disconnect();
            }
        }

        return result;
    }

    /**
     * 檢查兩端資料夾內的檔案數量, 大小是否相同
     * @param locPath 本機資料夾
     * @param srvPath 遠端資料夾
     * @param recursive if true all subdirectories are searched as well
     * @return true if success, false if error
     */
    public boolean scpCheckFolder(String locPath, String srvPath) {
//        log.info("sftp check folder start----");

        boolean checkFlag = false;
        Map<String,Long> srcFileListMap = new HashMap<String,Long>();
        Map<String,Long> destFileListMap = new HashMap<String,Long>();

        // 取得來源資料夾
        Iterator<?> srcit = FileUtils.iterateFiles(new File(locPath), null, false);
        File f = null;
        while (srcit.hasNext()) {
            f = (File) srcit.next();
            srcFileListMap.put(f.getName(), f.length());
        }

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no"); // ask | yes | no

            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(prop);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

//            c.cd(srcPath);
            Vector<?> vt = channelSftp.ls(srvPath);

            if (vt != null) {
                for (int i = 0; i < vt.size(); i++) {
                    Object obj = vt.elementAt(i);
                    if (obj instanceof LsEntry) {
                        SftpATTRS arr = ((LsEntry)obj).getAttrs();
                        arr.getSize();
                        String fileName = ((LsEntry)obj).getFilename();
                        long fileSize = arr.getSize();

                        if (!fileName.equals("..") && !fileName.equals(".")) { // 檔案名稱. 和.. 不做處理
                            if (!arr.isDir()) { //不是目錄才處理
                                destFileListMap.put(fileName, fileSize);
//                                c.get(ini_server_path + File.separator + fileName, ini_local_path + File.separator + fileName);
                            }
                        }
                        arr = null;
                    }
                    obj = null;
                }
            }

            if (srcFileListMap.size() == destFileListMap.size()) {
                checkFlag = true;
                for (String fn: srcFileListMap.keySet()) {
                    if (destFileListMap.containsKey(fn)) {
                        if (srcFileListMap.get(fn).longValue() != destFileListMap.get(fn).longValue()) {
                            checkFlag = false;
                            log.error("file check error size error");
                            break;
                        }
                    } else {
                        checkFlag = false;
                        log.error("file check error upload fail");
                        break;
                    }
                }
            } else {
                log.error("file check error map size not same");
            }
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        } finally {
            if ((channelSftp != null) && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if ((session != null) && session.isConnected()) {
                session.disconnect();
            }
        }

        return checkFlag;
    }

    /**
     * rsync get form server 複製整個資料夾
     * @param locPath 本機資料夾
     * @param srvPath 遠端資料夾
     */
    public void getRemoteFolder(String locPath, String srvPath) {
//        log.info("GetRemoteFolder check folder start----");

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no"); // ask | yes | no

            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(prop);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            Vector<?> vt = channelSftp.ls(srvPath);

            if (vt != null) {
                for (int i = 0; i < vt.size(); i++) {
                    Object obj = vt.elementAt(i);
                    if (obj instanceof LsEntry) {
                        SftpATTRS arr = ((LsEntry)obj).getAttrs();
                        String fileName = ((LsEntry)obj).getFilename();

                        if (!fileName.equals("..") && !fileName.equals(".")) { //檔案名稱. 和.. 不做處理
                            if (!arr.isDir()) { //不是目錄才處理
                                channelSftp.get(srvPath + File.separator + fileName, locPath + File.separator + fileName);
                            }
                        }
                        arr = null;
                    }
                    obj = null;
                }
            }
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        } finally {
            if ((channelSftp != null) && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if ((session != null) && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * rsync get form server 複製單一檔案
     * @param locPath 本機資料夾
     * @param srvPath 遠端檔案
     */
    public void getRemoteFile(String locPath, String srvPath) {
//        log.info("GetRemoteFile  start----");
//        log.info("locFile = " + locPath);

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no"); // ask | yes | no

            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(prop);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.get(srvPath ,locPath );
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        } finally {
            if ((channelSftp != null) && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if ((session != null) && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * scp 複製整個資料夾
     * @param srcPath 來源資料夾或檔案
     * @param destPath 目的資料夾
     * @return true if success, false if error
     */
    public boolean scpPutFolder(String srcPath, String destPath) {
        return this.scpPutFolder(srcPath, destPath, null, null, false);
    }

    /**
     * scp 複製整個資料夾
     * @param srcPath 來源資料夾或檔案
     * @param destPath 目的資料夾
     * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is null, all files are returned.
     * @return true if success, false if error
     */
    public boolean scpPutFolder(String srcPath, String destPath, String[] extensions) {
        return this.scpPutFolder(srcPath, destPath, extensions, null, false);
    }

    /**
     * scp 複製整個資料夾
     * @param srcPath 來源資料夾或檔案
     * @param destPath 目的資料夾
     * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is null, all files are returned.
     * @param renameExts rename extension when scp put success
     * @return true if success, false if error
     */
    public boolean scpPutFolder(String srcPath, String destPath, String[] extensions, String renameExts) {
        return this.scpPutFolder(srcPath, destPath, extensions, renameExts, false);
    }

    /**
     * scp 複製整個資料夾
     * @param srcPath 來源資料夾或檔案
     * @param destPath 目的資料夾
     * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is null, all files are returned.
     * @param renameExts rename extension when scp put success
     * @param recursive if true all subdirectories are searched as well
     * @return true if success, false if error
     */
    public boolean scpPutFolder(String srcPath, String destPath, String[] extensions, String renameExts, boolean recursive) {
//        log.info("sftp copy file start----");

        Session session = null;
        ChannelSftp channelSftp = null;
        JSch jsch = new JSch();

        try {
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no"); // ask | yes | no

            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(prop);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            this.put(channelSftp, new File(srcPath), new File(destPath), extensions, renameExts, recursive);

//            log.info("sftp copy file end----");

            return true;
        } catch (Exception e) {
            log.error(host, e);
        } finally {
            if ((channelSftp != null) && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if ((session != null) && session.isConnected()) {
                session.disconnect();
            }
        }

        return false;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

//    public static void main(String[] args) {
//        SpringSSHProcessUtil springSftpUtil = new SpringSSHProcessUtil();
//
//        springSftpUtil.setUsername("webuser");
//        springSftpUtil.setPassword("7e5nL0H");
//        springSftpUtil.setHost("localhost");
//        springSftpUtil.setPort(2026);
//
//        String command = "mkdir -p /home/webuser/photo/channel/mypaper/data/xml/insert";
//        log.info(springSftpUtil.SSHExec(command));
//
//        springSftpUtil.setCopyPath("/home/webuser/photo/channel/mypaper/data/xml/insert", "/home/webuser/photo/channel/mypaper/data/xml/insert");
//        springSftpUtil.scpPutTxtFolder();
//
//        /*
//        springSftpUtil.setCopyPath("/home/webuser/photo/channel/mypaper/data/xml/insert", "/home/webuser/photo/channel/mypaper/data/xml/insert");
//        springSftpUtil.scpPutFolder();
//
//        springSftpUtil.setCopyPath("/home/webuser/photo/channel/mypaper/data/xml/insert","/home/webuser/photo/channel/mypaper/data/xml/insert");
//        if(springSftpUtil.scpCheckFolder()){
//            log.error("check ok");
//
//            command = "touch /home/webuser/photo/channel/mypaper/data/xml/insert/ok.txt";
//            log.info(springSftpUtil.SSHExec(command));
//
//        }else{
//            log.error("check false");
//        }
//        */
//    }
}