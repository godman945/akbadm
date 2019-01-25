package com.pchome.akbadm.utils.images;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface ILoadImage {

	
	public BufferedImage getProcessImageBuffer(Map<IEnumImageData,String> dataStringMap);
	public BufferedImage getProcessImageBufferPrint(Map<IEnumImageData,String> dataStringMap);
	public void setSrcImagePath(String imagePath);
	
	
	
}
