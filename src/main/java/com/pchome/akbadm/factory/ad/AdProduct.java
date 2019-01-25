package com.pchome.akbadm.factory.ad;

public class AdProduct extends ABuildAdProcess{
	
	private String tproTag = EnumAdTag.START_TAG.getAdTag()+EnumAdTag.TPRO_TAG.getAdTag();		// 商品Tag
	private String cssContent;															
	private String htmlContent;
	private String path;

	@Override
	public String getTag() throws Exception {
		// TODO Auto-generated method stub
		return this.tproTag;
	}

	@Override
	public String getPath() throws Exception {
		// TODO Auto-generated method stub
		return this.path;
	}
	
	public void setPath(String path) throws Exception{
		this.path = path;
	}

	@Override
	public String getCssContent() throws Exception {
		// TODO Auto-generated method stub
		return this.cssContent;
	}

	@Override
	public void setCssContent(String css) throws Exception {
		// TODO Auto-generated method stub
		this.cssContent = css;
	}
	
	@Override
	public String getHtmlContent() throws Exception {
		// TODO Auto-generated method stub
		return this.htmlContent;
	}

	@Override
	public void setHtmlContent(String html) throws Exception {
		// TODO Auto-generated method stub
		this.htmlContent = html;
	}
	
}
