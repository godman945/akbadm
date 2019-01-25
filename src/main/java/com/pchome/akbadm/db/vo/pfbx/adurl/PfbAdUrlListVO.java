package com.pchome.akbadm.db.vo.pfbx.adurl;

import java.util.Date;

public class PfbAdUrlListVO
{
	//list
	private String pfbid; 			//pfbid
	private String companyname; 	//公司名稱
	private String memberid;		//帳號
	private String category;		//類別
	private String url;				//申請網址	
	private String notincount;		//不符合網址數量
	private String blockcount;		//已封鎖數量
	
	private String status;			//帳戶狀態
	private String taxid;			//統一編號
	
	//detail
	private String detailid;		//詳細資料id
	private Date detaildate;		//日期
	private String detailurl;		//網址
	private int detailcount;		//數量
	private String detaildesc;		//封鎖原因
	
	
	public String getPfbid()
	{
		return pfbid;
	}
	public void setPfbid(String pfbid)
	{
		this.pfbid = pfbid;
	}
	public String getCompanyname()
	{
		return companyname;
	}
	public void setCompanyname(String companyname)
	{
		this.companyname = companyname;
	}
	public String getMemberid()
	{
		return memberid;
	}
	public void setMemberid(String memberid)
	{
		this.memberid = memberid;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getNotincount()
	{
		return notincount;
	}
	public void setNotincount(String notincount)
	{
		this.notincount = notincount;
	}
	
	public Date getDetaildate()
	{
		return detaildate;
	}
	public void setDetaildate(Date detaildate)
	{
		this.detaildate = detaildate;
	}
	public String getDetailurl()
	{
		return detailurl;
	}
	public void setDetailurl(String detailurl)
	{
		this.detailurl = detailurl;
	}
	public int getDetailcount()
	{
		return detailcount;
	}
	public void setDetailcount(int detailcount)
	{
		this.detailcount = detailcount;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getTaxid()
	{
		return taxid;
	}
	public void setTaxid(String taxid)
	{
		this.taxid = taxid;
	}
	public String getBlockcount()
	{
		return blockcount;
	}
	public void setBlockcount(String blockcount)
	{
		this.blockcount = blockcount;
	}
	public String getDetailid()
	{
		return detailid;
	}
	public void setDetailid(String detailid)
	{
		this.detailid = detailid;
	}
	public String getDetaildesc()
	{
		return detaildesc;
	}
	public void setDetaildesc(String detaildesc)
	{
		this.detaildesc = detaildesc;
	}
	
}
