/**
 * @author nicolee
 * username --> scp login name
 * password --> scp login password
 * host --> scp host
 * port --> scp port
 *
 * ----上傳資料夾或檔案---------
 * function boolen scpPutFolder(String srcPath,String decPath)
 * srcPath ==> 本機資料夾,或一個檔案
 * decPath ==> scp 遠端資料夾
 *
 *
 * ----複製整個資料夾,日誌複製------
 * function  boolean scpPutTxtFolder(String srcFolder,String decFolder){
 * 只有副檔名是 txt 才 copy
 * copy 完將 txt 改為 ok
 * srcFolder ==>可以是資料夾或單一檔案
 * decFolder ==>目的資料夾
 *
 *
 *
 * ----檢查兩端資料夾內的檔案數量,大小是否相同-----
 * function boolean scpCheckFolder(String srcPath,String decPath)
 * srcPath ==> 本機資料夾
 * decPath ==> 遠端資料夾
 *
 * ---執行遠端命令-----
 * function String SSHExec(String host,int port,String username,String password,String command)
 * host port username password 同上
 * command 命令
 */

package com.pchome.soft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SpringSSHProcessUtil {
    private static final Log log = LogFactory.getLog(SpringSSHProcessUtil.class);

    private String host;
    private int port;
    private String username="";
    private String password="";

    private String srcPath="";
    private String decPath="";

    private String charSet="";

    /**
     * 執行 ssh 遠端命令
     * @param command command
     * @return execute result
     */
    public String SSHExec(String command){
        String result="no";

//        log.info("ssh host="+host);

        StringBuffer sb = new StringBuffer();
        InputStream in = null;
        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no");// StrictHostKeyChecking:
            // ask | yes | no
            session.setConfig(prop);
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand(command);
            channel.connect();

            //log.info("charSet="+charSet);

            if(charSet=="" ){
                charSet="UTF-8";
            }

            //InputStreamReader in = new InputStreamReader(channel.getInputStream(),charSet);

            in = channel.getInputStream();

            int nextChar;

            while (true) {
                while ((nextChar = in.read()) != -1) {
                    sb.append((char) nextChar);
                }
                if (channel.isClosed()) {
                    //log.error("exit-status: "
                            //+ channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            channel.disconnect();
            session.disconnect();

            result=new URLCodec().decode(sb.toString(),charSet);

            /*
            if(charSet=="BIG5"){
                String str="";
                str=new URLCodec().encode(Result,"UTF8");
                log.info("----nicolog--------------------------------------");
                log.info("nicolog->utf8 code"+str);
                Result="";
                Result=new URLCodec().decode(str,"UTF8");
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
        }

        return result;
    }

    public boolean scpCheckFolder(String srcFolder,String decFolder){
        setCopyPath(srcFolder,decFolder);
        return scpCheckFolder();
    }

    /**
     * scp 複製整個資料夾
     * @return true if success, false if error
     */
    public boolean scpCheckFolder() {
//        log.info("sftp check folder start----");

        boolean checkFlag=false;
        Map<String,Long> srcFileListMap=new HashMap<String,Long>();
        Map<String,Long> decFileListMap=new HashMap<String,Long>();

        //取得來源資料夾
        Iterator<File> srcit=FileUtils.iterateFiles(new File(srcPath), null, true);
        File f = null;
        while (srcit.hasNext()) {
            f = srcit.next();
            srcFileListMap.put(f.getName(), f.length());
        }

        Session session;
        Channel channel;
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no");// StrictHostKeyChecking:
            // ask | yes | no
            session.setConfig(prop);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp c = (ChannelSftp) channel;

            //c.cd(srcPath);
            Vector<?> vt = c.ls(decPath);

            if (vt != null) {
                for(int i=0;i<vt.size();i++){
                    Object obj=vt.elementAt(i);
                    if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
                        SftpATTRS arr = ((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getAttrs();
                        arr.getSize();
                        String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry )obj).getFilename();
                        long fileSize =  arr.getSize();

                        if (!fileName.equals("..") && !fileName.equals(".")) { //檔案名稱. 和.. 不做處理
                            if (!arr.isDir()){ //不是目錄才處理
                                decFileListMap.put(fileName, fileSize);
                                //    c.get(ini_server_path + "/" + fileName ,ini_local_path + File.separator+ fileName );
                            }
                        }
                        arr = null;
                    }
                    obj = null;
                }
            }

            c.disconnect();
            channel.disconnect();
            session.disconnect();

            if(srcFileListMap.size()==decFileListMap.size()){
                checkFlag=true;
                for(String fn:srcFileListMap.keySet()){
                    if(decFileListMap.containsKey(fn)){
                        if(srcFileListMap.get(fn).longValue() != decFileListMap.get(fn).longValue()){
                            checkFlag=false;
                            log.warn("file check error size error");
                            break;
                        }
                    }else{
                        checkFlag=false;
                        log.warn("file check error upload fail");
                        break;
                    }
                }
            }else{
                log.warn("file check error map size not same");
            }
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        }

        return checkFlag;
    }

    /**
     * rsync get form server 複製整個資料夾
     * @param locFolder local folder
     * @param srvFolder remote folder
     */
    public void GetRemoteFolder(String locFolder,String srvFolder) {
//        log.info("GetRemoteFolder check folder start----");

        Session session;
        Channel channel;
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no");// StrictHostKeyChecking:
            // ask | yes | no
            session.setConfig(prop);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp c = (ChannelSftp) channel;

            Vector<?> vt = c.ls(srvFolder);

            if (vt != null) {
                for(int i=0;i<vt.size();i++){
                    Object obj=vt.elementAt(i);
                    if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
                        SftpATTRS arr = ((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getAttrs();
                        String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry )obj).getFilename();

                        if (!fileName.equals("..") && !fileName.equals(".")) { //檔案名稱. 和.. 不做處理
                            if (!arr.isDir()){ //不是目錄才處理
                                c.get(srvFolder + "/" + fileName ,locFolder + "/" + fileName );
                            }
                        }
                        arr = null;
                    }
                    obj = null;
                }
            }

            c.disconnect();
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        }
    }

    /**
     * rsync get form server 複製單一檔案
     * @param locFile local file
     * @param srvFile remote file
     */
    public void GetRemoteFile(String locFile,String srvFile) {
//        log.info("GetRemoteFile  start----");
//        log.info("locFile="+locFile);

        Session session;
        Channel channel;
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no");// StrictHostKeyChecking:
            // ask | yes | no
            session.setConfig(prop);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp c = (ChannelSftp) channel;

            c.get(srvFile ,locFile );

            c.disconnect();
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        }
    }

    /**
     * scp 複製整個資料夾
     * @param srcFolder 可以是資料夾或單一檔案
     * @param decFolder 目的資料夾
     * @return true if success, false if error
     */
    public boolean scpPutFolder(String srcFolder,String decFolder){
        setCopyPath(srcFolder,decFolder);
        return scpPutFolder();
    }

    /**
     * scp 複製整個資料夾
     * @return true if success, false if error
     */
    public boolean scpPutFolder() {
//        log.info("sftp copy file start----");
        List<String> filenames = new ArrayList<String>();

        File srcDir=new File(srcPath);

        if(srcDir.isDirectory()){
            Iterator<?> itera = FileUtils.iterateFiles(new File(srcPath), null, false);

            while (itera.hasNext()) {
                File f = (File) itera.next();
                //log.error("get file="+f.getAbsolutePath());
                filenames.add(f.getAbsolutePath());
            }
        }else{
            filenames.add(srcPath);
        }

        if (filenames.size() == 0){
//            log.info("sftp no file copy ----");
            return false;
        }

        Session session;
        Channel channel;
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no");// StrictHostKeyChecking:
            // ask | yes | no
            session.setConfig(prop);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp c = (ChannelSftp) channel;

            c.cd(decPath);

            InputStream in = null;
            OutputStream out = null;

            for (int i = 0; i < filenames.size(); i++) {
                String filename = filenames.get(i);

                if (filename == null || "".equals(filename)) {
                    continue;
                }

                int idx = filename.lastIndexOf(File.separator);

                //取得"/"後面的檔名
                String uploadname = filename.substring(idx == -1 ? 0 : idx + 1);
                //c.cd(decPath);
                out = c.put(uploadname);

                in = new FileInputStream(filename);

                byte[] b = new byte[1024];

                while (true) {
                    int len = in.read(b, 0, b.length);
                    if (len <= 0)
                        break;
                    out.write(b, 0, len); // out.flush();
                }

                out.flush();
                out.close();

//                log.info(uploadname+" upload ok");

                in.close();
            }

            c.disconnect();
            channel.disconnect();
            session.disconnect();

//            log.info("sftp copy file end----");

            return true;
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        } catch (IOException e) {
            log.error(host, e);
        }

        return false;
    }

    /**
     * scp 複製整個資料夾,日誌複製<br />
     * 只有副檔名是 txt 才 copy<br />
     * copy 完將 txt 改為 ok
     * @param srcFolder 可以是資料夾或單一檔案
     * @param decFolder 目的資料夾
     * @return true if success, false if error
     */
    public boolean scpPutTxtFolder(String srcFolder,String decFolder){
        setCopyPath(srcFolder,decFolder);
        return scpPutTxtFolder();
    }

    /**
     * scp 複製整個資料夾
     * @return true if success, false if error
     */
    public boolean scpPutTxtFolder() {
//        log.info("sftp copy txt file start----");
        List<String> filenames = new ArrayList<String>();

        File srcDir=new File(srcPath);

        if(srcDir.isDirectory()){
            Iterator<?> itera = FileUtils.iterateFiles(new File(srcPath), new String[]{"txt"}, false);

            while (itera.hasNext()) {
                File f = (File) itera.next();
                //log.debug("get file="+f.getAbsolutePath());
                filenames.add(f.getAbsolutePath());
            }
        }else{
            filenames.add(srcPath);
        }

        if (filenames.size() == 0){
//            log.info("sftp no file copy ----");
            return false;
        }

        Session session;
        Channel channel;
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties prop = new Properties();
            prop.setProperty("StrictHostKeyChecking", "no");// StrictHostKeyChecking:
            // ask | yes | no
            session.setConfig(prop);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp c = (ChannelSftp) channel;

            c.cd(decPath);

            InputStream in = null;
            OutputStream out = null;

            File oldFile=null;
            File newFile=null;
            String newFileName="";

            for (int i = 0; i < filenames.size(); i++) {
                String filename = filenames.get(i);

                if (filename == null || "".equals(filename)) {
                    continue;
                }

                int idx = filename.lastIndexOf(File.separator);

                //取得"/"後面的檔名
                String uploadname = filename.substring(idx == -1 ? 0 : idx + 1);
                //c.cd(decPath);
                out = c.put(uploadname);

                in = new FileInputStream(filename);

                byte[] b = new byte[1024];

                while (true) {
                    int len = in.read(b, 0, b.length);
                    if (len <= 0)
                        break;
                    out.write(b, 0, len); // out.flush();
                }

                out.flush();
                out.close();

//                log.info(uploadname+" upload ok");

                in.close();

                newFileName=filename.replace(".txt", ".ok");

                oldFile=new File(filename);
                newFile=new File(newFileName);
                oldFile.renameTo(newFile);
            }

            c.disconnect();
            channel.disconnect();
            session.disconnect();

//            log.info("sftp copy file end----");

            return true;
        } catch (JSchException e) {
            log.error(host, e);
        } catch (SftpException e) {
            log.error(host, e);
        } catch (IOException e) {
            log.error(host, e);
        }

        return false;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setCopyPath(String srcPath,String decPath){
        this.srcPath=srcPath;
        this.decPath=decPath;
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
//        String command="mkdir -p /home/webuser/photo/channel/mypaper/data/xml/insert";
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
//            command="touch /home/webuser/photo/channel/mypaper/data/xml/insert/ok.txt";
//            log.info(springSftpUtil.SSHExec(command));
//
//        }else{
//            log.error("check false");
//        }
//        */
//    }
}