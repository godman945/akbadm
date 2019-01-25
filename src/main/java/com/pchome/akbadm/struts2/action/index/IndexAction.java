package com.pchome.akbadm.struts2.action.index;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.config.TestConfig;

public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 1221104773621312531L;

	public String execute() throws Exception{
		return SUCCESS;
	}
/*
	public static void main(String arg[]){

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.path);

		IndexAction indexAction = (IndexAction) context.getBean("IndexAction");
		
		try {
			indexAction.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
