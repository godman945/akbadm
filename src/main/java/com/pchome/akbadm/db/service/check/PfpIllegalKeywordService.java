package com.pchome.akbadm.db.service.check;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.check.PfpIllegalKeywordDAO;
import com.pchome.akbadm.db.pojo.PfpIllegalKeyword;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.config.TestConfig;

public class PfpIllegalKeywordService extends BaseService<PfpIllegalKeyword, String> implements IPfpIllegalKeywordService {

	public int getIllegalKeywordCountByCondition(String queryString) throws Exception {
		return ((PfpIllegalKeywordDAO) dao).getIllegalKeywordCountByCondition(queryString);
	}

	public List<PfpIllegalKeyword> getIllegalKeywordByCondition(String queryString, int pageNo, int pageSize) throws Exception {
		return ((PfpIllegalKeywordDAO) dao).getIllegalKeywordByCondition(queryString, pageNo, pageSize);
	}

	public void updateIllegalKeywordBySeq(String seq, String content) throws Exception {
		((PfpIllegalKeywordDAO) dao).updateIllegalKeywordBySeq(seq, content);
	}

	public void deleteIllegalKeywordBySeq(String seq) throws Exception {
		((PfpIllegalKeywordDAO) dao).deleteIllegalKeywordBySeq(seq);
	}

	public int checkIllegalKeywordExists(String queryString) throws Exception {
		return ((PfpIllegalKeywordDAO) dao).checkIllegalKeywordExists(queryString);
	}

	/**
	 * 手動匯入禁用字 
	 */
	public static void main(String args[]) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		PfpIllegalKeywordService service = (PfpIllegalKeywordService) context.getBean("PfpIllegalKeywordService");

		List<String> data = FileUtils.readLines(new File("D://illegal.txt"), "utf-8");

		Date now = new Date();

		System.out.println("*********** start");
		for (int i=0; i<data.size(); i++) {
			if (StringUtils.isNotEmpty(data.get(i))) {

				String str = data.get(i).trim();
				System.out.println(">>> str = " + str);

				int count = service.checkIllegalKeywordExists(str);

				if (count==0) {

					PfpIllegalKeyword vo = new PfpIllegalKeyword();
					vo.setContent(data.get(i).trim());
					vo.setCreateDate(now);
					vo.setUpdateDate(now);

					service.saveOrUpdate(vo);
				}
			}
		}
		System.out.println("*********** end");
	}
}
