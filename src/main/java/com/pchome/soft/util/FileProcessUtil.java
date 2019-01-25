package com.pchome.soft.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * converient process file util
 * @author nicolee
 * @since 1.0
 * @version 1.0
 */
public class FileProcessUtil {
    private static final Log log = LogFactory.getLog(FileProcessUtil.class);

    private static final FileProcessUtil fileProcessUtil=new FileProcessUtil();

    private FileProcessUtil(){};

    /**
     * get singleton instance
     * @return instance
     */
    public static FileProcessUtil getInstance(){
        return fileProcessUtil;
    }

    /**
     * 執行 shell 指令
     * @param command command
     */
    public void execShellCommand(String command){
        try {
            Process proc = Runtime.getRuntime().exec(command);
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            log.info("<OUTPUT>");
            while ( (line = br.readLine()) != null)
                log.info(line);
            log.info("</OUTPUT>");
            int exitVal = proc.waitFor();
            log.info("Process exitValue: " + exitVal);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 取得主機名
     * @return host name
     */
    public String getHostName(){
        String hostName="";

        if(StringUtils.isNotBlank(hostName)){
            return hostName;
        }else{
            //String hostName="";

            try {
                InetAddress addr = InetAddress.getLocalHost();

                // Get IP Address
                //byte[] ipAddr = addr.getAddress();

                // Get hostname
                hostName = addr.getHostName();

                log.info("hostname="+hostName);

            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }

            return hostName;
        }
    }

     /**
      * 過濾xml特殊字元
      * @param oldFile input file
      * @param charSet encoding of input and output file
      * @return output file
      */
     public File checkXmlCharacter(File oldFile,String charSet){
        // xxxxx_new.xml
        StringBuilder newFileName = new StringBuilder(oldFile.getParent());
        newFileName.append("/");
        newFileName.append(FilenameUtils.getBaseName(oldFile.getName()));
        newFileName.append("_new");
        newFileName.append(".");
        newFileName.append(FilenameUtils.getExtension(oldFile.getName()));
        File newFile = new File(newFileName.toString());

        log.info("charSet="+charSet);
        try {
            // read oldFile
            FileInputStream in = new FileInputStream(oldFile);
            InputStreamReader inReader = new InputStreamReader(in, charSet);
            BufferedReader bufReader = new BufferedReader(inReader);

            // output newFile
            FileOutputStream out = new FileOutputStream(newFile);
            OutputStreamWriter outS = new OutputStreamWriter(out, charSet);
            String con = "";
            while((con = bufReader.readLine())!= null){
                // check xml character
                //outS.write(stripNonValidXMLCharacters(con));
                outS.write(stripNonValidXMLCharacters(con));
            }
            IOUtils.closeQuietly(bufReader);
            IOUtils.closeQuietly(outS);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return newFile;
    }

    /**
     * 過濾xml中不合法的字元
     * @param in input string
     * @return string filtered invalid char
     */
    private String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in)))
            return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
            // here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF))){
                out.append(current);
            }
        }
        return out.toString();
    }

    /**
     * 說明: check md5<br />
     * 補充: 原本是將整個file讀進來轉成byte[]後, 進行Md5, 後來改成對檔案的大小進行Md5, 介接時又有...SO...名稱也沒改了
     * @param srcFile src file path (xxx.xml)
     * @param md5File md5 file path 相關訊息 (xxx.xml.md5)
     * @return true if check md5 Success, false if error
     */
    public boolean checkFileMd5(File srcFile, File md5File) {
        boolean flag = false;

        try {
            // step1
            String srcData = String.valueOf(srcFile.length());
            String srcFileMd5 = DigestUtils.md5Hex(srcData);
            log.info("srcFileMd5: "+srcFileMd5);

            // step2
            String tempMd5Content = FileUtils.readFileToString(md5File);
            String[] md5Contents = StringUtils.split(StringUtils.trim(tempMd5Content), " ");

            // step3
            if (md5Contents.length != 2) {
                log.info("**** Erros In Md5 File, Content : " + tempMd5Content);
            } else {
                String currentMd5 = DigestUtils.md5Hex(md5Contents[0]);
                log.info("**** File : " + srcFile);
                log.info("**** srcFileMd5  : " + srcFileMd5);
                log.info("**** currenteMd5 : " + currentMd5);

                if(StringUtils.indexOf(md5Contents[1], srcFile.getName())!= -1){
                    if (StringUtils.equalsIgnoreCase(srcFileMd5, currentMd5)) {
                        log.info("**** Check Md5 Success");
                        flag = true;
                    } else {
                        log.info("**** Check Md5 Failur");
                    }
                } else {
                    log.info("******** ERROR, path error");
                    log.info("******** srcFile      : " + srcFile.getPath());
                    log.info("******** checkMd5File : " + md5Contents[1]);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (OutOfMemoryError e){
            log.error(e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 取得檔案列表
     * @param folderPath file path
     * @return file list
     */
    public List<File> getFileList(String folderPath){
        Iterator<File> iterator=FileUtils.iterateFiles(new File(folderPath), new String[]{"xml"}, false);
        List<File> fileList=new ArrayList<File>();
        File f;
        while(iterator.hasNext()){
            f=iterator.next();
            fileList.add(f);
            //System.out.println(f.getAbsolutePath());
        }

        return fileList;
    }

    /**
     * 路徑不存在建立資料夾
     * @param directoryPath directory path
     */
    public void createDirectory(String directoryPath){
        File dirPath=new File(directoryPath);
        if(!dirPath.exists()){
            try {
                FileUtils.forceMkdir(dirPath);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 刪除資料夾
     * @param directoryPath directory path
     */
    public void deleteDirectory(String directoryPath){
        File dirPath=new File(directoryPath);
        if(dirPath.exists()){
            try {
                FileUtils.deleteDirectory(dirPath);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 刪除檔案
     * @param filePath file path
     */
    public void deleteFile(String filePath){
        File file=new File(filePath);
        if(file.exists()){
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * copy file 到當日日期資料夾, 如果檔案存在新增檔案序號
     * @param srcFilePath source file
     * @param decDir target file
     */
    public void copyFileToDayDirectory(String srcFilePath,String decDir){
        File srcFile=new File(srcFilePath);//src file
        DateValueUtil.getInstance().reloadCalender();
        String decYearPath=decDir+"/"+DateValueUtil.getInstance().getDateYear();//dec folder add year
        createDirectory(decYearPath);
        String decMonthPath=decYearPath+"/"+DateValueUtil.getInstance().getDateMonthO();//dec folder add year/month
        createDirectory(decMonthPath);
        String decDayPath=decMonthPath+"/"+DateValueUtil.getInstance().getDateDayO();//dec folder add year/month/day
        createDirectory(decDayPath);

        String DecFilePath=decDayPath+"/"+srcFile.getName();
        //檢查是否存在取得重複檔案新序號檔名
        String copyDecFile=checkFileExists(DecFilePath,decDayPath);

        try {
            FileUtils.copyFile(srcFile, new File(copyDecFile));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String checkFileExists(String file,String dir){
        File f=new File(file);
        String decFilePath="";

        //System.out.println("in file="+file);

        String ext="";//副檔名
        String ff="";//檔名

        //檢查目的檔案是否存在
        if(f.exists()){
            String decFileName=f.getName();//目的檔名
            String fpa[]=decFileName.split("\\.");

            if(fpa.length==2){
                ext=fpa[1];//副檔名
                ff=fpa[0];//檔名
            }

            if(fpa.length==3){
                ext=fpa[1]+"."+fpa[2];//副檔名
                ff=fpa[0];//檔名
            }

            String no="";//序號
            String fname="";//移除序號後的原檔名

            if(ff.indexOf('-')>=0){// serach '-'
                //get no
                no=ff.substring(ff.indexOf('-')+1,ff.length());
                int num=Integer.parseInt(no);
                num++;//no ++
                no=String.valueOf(num);
                if(no.length()==1){
                    no="0"+no;
                }
                //get org name
                fname=ff.substring(0,ff.indexOf('-'));
            }else{//not find '-'
                no="01";
                fname=fpa[0];
            }

            decFilePath=dir+"/"+fname+"-"+no+"."+ext;
            //System.out.println("dec-file="+decFilePath);
            decFilePath=checkFileExists(decFilePath,dir);
            //return decFilePath;
        }else{
            decFilePath=file;
        }

        return decFilePath;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf(".");

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

//    public static void main(String avg[]){
//        //file copy test
//        String filePath="/home/webuser/photoSingleSearch/data/xml/src/aa.xml";
//        String decDir="/home/webuser/photoSingleSearch/data/xml/backup";
//        FileProcessUtil.getInstance().copyFileToDayDirectory(filePath,decDir);
//    }
}