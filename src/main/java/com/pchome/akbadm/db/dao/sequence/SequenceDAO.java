package com.pchome.akbadm.db.dao.sequence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.Sequence;
import com.pchome.config.TestConfig;

public class SequenceDAO extends BaseDAO<Sequence, String> implements ISequenceDAO {

	public static void main(String args[]){

		//log.info(TestConfig.path.toString());

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		Logger log = LogManager.getRootLogger();

		SequenceDAO sequenceDAO = (SequenceDAO) context.getBean("SequenceDAO");
		//Product po=productDAO.get("P0010001");
		//log.info("po="+po.getProdName());

		Sequence pList= sequenceDAO.get("ord");

		//log.info(pList.size());

		//for(Sequence p:pList){

			log.info(pList.getTableName());

		//}
	}
}
