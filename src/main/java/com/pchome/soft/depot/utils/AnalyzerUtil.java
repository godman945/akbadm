package com.pchome.soft.depot.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.dic.Dictionary;

public class AnalyzerUtil {

	private final static AnalyzerUtil analyzerUtil=new AnalyzerUtil();

	private static Log log = LogFactory.getLog(AnalyzerUtil.class);

	private AnalyzerUtil(){}

	public static AnalyzerUtil getInstance(){
		return analyzerUtil;
	}

	public List<String> getAnalyerList(String text){
		List<String> words=new ArrayList<String>();
		Map<String,String> samword=new HashMap<String,String>();

		IKSegmentation ikSeg = new IKSegmentation(new StringReader(text)); 

		Lexeme s;
		try {
			s = ikSeg.next();
			while(s != null){
				if(s.getLexemeText().length()>1){
					if(!samword.containsKey(s.getLexemeText())){

						words.add(s.getLexemeText());

						samword.put(s.getLexemeText(), s.getLexemeText());
					}
				}

				s=ikSeg.next();
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  

		return words;
	}

	public String getAnalyerSearchText(String text){
		StringBuffer searchText=new StringBuffer();
		Map<String,String> samword=new HashMap<String,String>();

		IKSegmentation ikSeg = new IKSegmentation(new StringReader(text)); 

		Lexeme s;
		try {
			s = ikSeg.next();
			while(s != null){
				if(s.getLexemeText().length()>1){
					if(!samword.containsKey(s.getLexemeText())){
						//log.info(s.getLexemeText());
						searchText.append(s.getLexemeText());
						//searchText.append(" ");
						samword.put(s.getLexemeText(), s.getLexemeText());

					}
				}

				s=ikSeg.next();
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  

		return searchText.toString();
	}

	public void loadDictionaryFormFile(String analyzerFile){


		File analyzerFileDir=new File(analyzerFile);
		if(analyzerFileDir.exists()){

			List<String> keywordList=null;

			try {
				keywordList = FileUtils.readLines(analyzerFileDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//for(String s:keywordList){
			//log.info(s);
			//}
			Dictionary.loadExtendWords(keywordList);
		}
	}


}
