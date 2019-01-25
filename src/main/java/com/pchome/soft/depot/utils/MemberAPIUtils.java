package com.pchome.soft.depot.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class MemberAPIUtils {

	Logger log = Logger.getLogger(this.getClass());

	private static final String memberServer = "http://member1.pchome.com.tw/";
	private static final String updateMember = "updateMemberInfo4ADAPI.html";	
	private static final String checkAvalibeEmail = "checkEmailAvalibe4ADAPI.html";
	private static final String findMember = "findMemberInfo4ADAPI.html";
	
	private static final MemberAPIUtils instance = new MemberAPIUtils();

	private MemberAPIUtils() {
	}

	public static MemberAPIUtils getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> findMemberDetail(String pcId) throws Exception {

		String url = memberServer + findMember + "?ad_user_id=" + pcId;
		
		Map<String, String> memberMap = new HashMap<String, String>();
		
		JSONObject jsonObject = new JSONObject(this.getJsonByUrl(url));
		
		Iterator<String> nameItr = jsonObject.keys();
		
		String key;
		while(nameItr.hasNext()) {
			key = nameItr.next();
			memberMap.put(key, jsonObject.getString(key));
		}
		
		return memberMap;
	}

	@SuppressWarnings("rawtypes")
	public void updateMemberDetail(HashMap<String, String> memberMap) throws Exception{
				
		JSONObject jsonObject = new JSONObject();
		
		Iterator iter  = memberMap.entrySet().iterator();

		while (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    String key = (String)entry.getKey();
		    String val = (String)entry.getValue();
		    jsonObject.put(key, val);
		} 
		
		String url = memberServer + updateMember + "?ad_user_data="+
					java.net.URLEncoder.encode(RSAUtils.encode(jsonObject.toString()),"UTF-8");
		this.getJsonByUrl(url);
	}
	
	public boolean isPcIdExist(String email) throws Exception{
	
		String url = memberServer + checkAvalibeEmail +	"?ad_email="+email;
		
		JSONObject jsonObject = new JSONObject(this.getJsonByUrl(url));
		
		String stat = jsonObject.get("stat").toString();

		if(stat.equals("yes")){
			return false;
		}else{
			return true;
		}
	}
	
	private String getJsonByUrl(String urlStr) throws Exception {
		
		BufferedReader reader = null;
		URL url = new URL(urlStr);
		reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		
		StringBuffer buffer = new StringBuffer();
		int read;
		char[] chars = new char[1024];
		
		while((read = reader.read(chars)) != -1){
			buffer.append(chars,0,read);
		}
		
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	public static void main(String age[]) throws Exception {

		MemberAPIUtils utils = new MemberAPIUtils();
		
		String url = "http://member.pchome.com.tw/findMemberInfo4ADAPI.html?ad_user_id=" + "reantoilpc";
		
		
		JSONObject jsonObject = new JSONObject(utils.getJsonByUrl(url));
		
		Iterator<String> nameItr = jsonObject.keys();
		int i = 0;
		String name;
		while(nameItr.hasNext()) {
			i++;
			name = nameItr.next();
			System.out.println(i+" = "+name+"  "+jsonObject.getString(name));
		
		}
		

	}

}
