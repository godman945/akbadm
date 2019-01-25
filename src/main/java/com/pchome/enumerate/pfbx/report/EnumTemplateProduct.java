package com.pchome.enumerate.pfbx.report;

public enum EnumTemplateProduct {

	WORD(1, "文字"),
	PIC(2, "圖像"),
	FLASH(3, "影音"),
	WORDPIC(4, "圖文");

	private final Integer templateId;
	private final String template;

	private EnumTemplateProduct(Integer templateId, String template){
		this.templateId = templateId;
		this.template = template;		
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public String getTemplate() {
		return template;
	}

	
}
