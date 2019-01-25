package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;

public class PfbxBlockCusurlDAO extends BaseDAO<PfbxBlockCusurl,String> implements IPfbxBlockCusurlDAO {
	
	public List<PfbxBlockCusurl> getSYSList_By_PfbId(String pfbId , String searchurl) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		List<Object> con = new ArrayList<Object>();
		List<PfbxBlockCusurl> list = null;
		
		sb.append("from PfbxBlockCusurl where 1=1 ");
		sb.append("and pfbxUserOption.pfbxCustomerInfo.customerInfoId = ?");
		con.add(pfbId);
		sb.append("and pfbxUserOption.optionName = ? ");
		con.add("SYS");
		
		if(StringUtils.isNotBlank(searchurl))
		{
			sb.append("and url like ?");
			con.add("%" + searchurl + "%");
		}
		
		list = this.getHibernateTemplate().find(sb.toString(), con.toArray());
		
		return list;
	}
	
	public int chk_oid_url(String oid , String url)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into pfbx_block_cusurl ");
		sql.append("(cu_id , o_id , url) ");
		sql.append("values ");
		
		String sql1 = "select count(*) from pfbx_block_cusurl where o_id = '"+oid+"' and url = '"+url+"' ";
		
		Session sss = this.getSession();
		Number rs = (Number)sss.createSQLQuery(sql1).uniqueResult();
		sss.flush();
		
		return rs.intValue();
	}
	
	@Transactional
	public int insPfbxBlockCusurl(Map blockInfo)
	{
		StringBuffer sql1 = new StringBuffer();
		sql1.append("insert into pfbx_user_option ");
		sql1.append("(o_id , option_name , piosition_allow_type , url_allow_type , size_allow_type , customer_info_id) ");
		sql1.append("values ");
		sql1.append("('"+ blockInfo.get("oid") +"' , '"+blockInfo.get("optionName")+"' , "+blockInfo.get("piositionType")+" , "); 
		sql1.append(" "+ blockInfo.get("urlType") +" , "+ blockInfo.get("sizeType") +" ,'"+ blockInfo.get("pfbId") +"' )");
		
		StringBuffer sql2 = new StringBuffer();
		sql2.append("insert into pfbx_block_cusurl ");
		sql2.append("(cu_id , o_id , url , board_mesg) ");
		sql2.append("values ");
		sql2.append("( '"+blockInfo.get("cuid")+"' , '"+blockInfo.get("oid")+"' , '"+blockInfo.get("blockUrl")+"'  , '"+blockInfo.get("blockDesc")+"')");
		
		Session sss = this.getSession();
		sss.createSQLQuery(sql1.toString()).executeUpdate();
		sss.createSQLQuery(sql2.toString()).executeUpdate();
		sss.flush();
		
		return 1;
	}
	
	public int ins_oid_url(String cuid , String oid , String url , String boardMesg)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into pfbx_block_cusurl ");
		sql.append("(cu_id , o_id , url , board_mesg) ");
		sql.append("values ");
		sql.append("( '"+cuid+"' , '"+oid+"' , '"+url+"'  , '"+boardMesg+"')");
		
		int rs = 0;
		Session sss = this.getSession();
		rs = sss.createSQLQuery(sql.toString()).executeUpdate();
		sss.flush();
		
		return rs;
	}
   
}
