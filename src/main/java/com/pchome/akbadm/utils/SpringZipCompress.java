package com.pchome.akbadm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SpringZipCompress {

	public InputStream getZipStream(Map<String,byte[]> contentMap){

		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(fos);

		ByteArrayInputStream in;

		byte[] buffer = new byte[1024];

		int len;

		try {
			
			for(String fileName:contentMap.keySet()){
				ZipEntry ze= new ZipEntry(fileName);
				try {
					zos.putNextEntry(ze);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ByteArrayInputStream(fos.toByteArray());

	}

	
	public InputStream getZipStreamByFilePath(Map<String,String> contentMap){

		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(fos);

		ByteArrayInputStream in;

		byte[] buffer = new byte[1024];

		int len;

		try {
			
			for(String fileName:contentMap.keySet()){
				File file = new File(contentMap.get(fileName));
				
				if(file != null){
					ZipEntry ze= new ZipEntry(fileName);
					try {
						zos.putNextEntry(ze);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					in=new ByteArrayInputStream(getBytesFromFile(file));
					len=0;
					
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					in.close();
					zos.closeEntry();	
				}
			}

			//remember close it
			zos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ByteArrayInputStream(fos.toByteArray());

	}
	
	public static byte[] getBytesFromFile(File f){ 
        if (f == null) { 
            return null; 
        } 
        try { 
            FileInputStream stream = new FileInputStream(f); 
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024); 
            byte[] b = new byte[1024]; 
            int n; 
            while ((n = stream.read(b)) != -1) 
                out.write(b, 0, n); 
            stream.close(); 
            out.close(); 
            return out.toByteArray(); 
        } catch (IOException e){ 
        } 
        return null; 
    } 
}
