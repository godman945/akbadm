package com.pchome.akbadm.db.vo.pfbx.report;

import java.io.Serializable;
import java.util.Date;


//盈虧查詢用
public class PfbxInComeReportVo implements Serializable{

	private Integer id;
	private String pfbxCustomerInfoId;	//聯播網帳號
	private Date reportdate;			//報表日期
	private float adclkprice;			//PFP花費
	private float adInvalidClkPrice;	//無效點擊
	private float sysclkprice;			//驗證花費
	private float income;				//收入
	private float expense;				//支出
	private float total;				//盈虧
	
	//incomeDetail used
	private String detailItem;			//明細項目 1 pfd收入 2 pfp收入 3 pfb收入 4 pfd支出 5 pfb支出
	private String detailItemName;		//項目名稱
	private String detailIncomeExpense;//收入支出
	private float detailSave;			//儲值金
	private float detailFree;			//贈送金
	private float detailPostPaid;		//後付費
	private float detailTotal;			//小計
	
	private String notMatchFlag;		//PFP花費 & 驗證花費 不相符
	
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Date getReportdate()
	{
		return reportdate;
	}
	public void setReportdate(Date reportdate)
	{
		this.reportdate = reportdate;
	}
	public float getAdclkprice()
	{
		return adclkprice;
	}
	public void setAdclkprice(float adclkprice)
	{
		this.adclkprice = adclkprice;
	}
	public float getSysclkprice()
	{
		return sysclkprice;
	}
	public void setSysclkprice(float sysclkprice)
	{
		this.sysclkprice = sysclkprice;
	}
	public float getIncome()
	{
		return income;
	}
	public void setIncome(float income)
	{
		this.income = income;
	}
	public float getExpense()
	{
		return expense;
	}
	public void setExpense(float expense)
	{
		this.expense = expense;
	}
	public float getTotal()
	{
		return total;
	}
	public void setTotal(float total)
	{
		this.total = total;
	}
	public String getDetailItem()
	{
		return detailItem;
	}
	public void setDetailItem(String detailItem)
	{
		this.detailItem = detailItem;
	}
	public String getDetailItemName()
	{
		return detailItemName;
	}
	public void setDetailItemName(String detailItemName)
	{
		this.detailItemName = detailItemName;
	}
	public String getDetailIncomeExpense()
	{
		return detailIncomeExpense;
	}
	public void setDetailIncomeExpense(String detailIncomeExpense)
	{
		this.detailIncomeExpense = detailIncomeExpense;
	}
	public float getDetailSave()
	{
		return detailSave;
	}
	public void setDetailSave(float detailSave)
	{
		this.detailSave = detailSave;
	}
	public float getDetailFree()
	{
		return detailFree;
	}
	public void setDetailFree(float detailFree)
	{
		this.detailFree = detailFree;
	}
	public float getDetailPostPaid()
	{
		return detailPostPaid;
	}
	public void setDetailPostPaid(float detailPostPaid)
	{
		this.detailPostPaid = detailPostPaid;
	}
	public float getDetailTotal()
	{
		return detailTotal;
	}
	public void setDetailTotal(float detailTotal)
	{
		this.detailTotal = detailTotal;
	}
	public String getPfbxCustomerInfoId()
	{
		return pfbxCustomerInfoId;
	}
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId)
	{
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}
	public float getAdInvalidClkPrice()
	{
		return adInvalidClkPrice;
	}
	public void setAdInvalidClkPrice(float adInvalidClkPrice)
	{
		this.adInvalidClkPrice = adInvalidClkPrice;
	}
	public String getNotMatchFlag()
	{
		return notMatchFlag;
	}
	public void setNotMatchFlag(String notMatchFlag)
	{
		this.notMatchFlag = notMatchFlag;
	}
	
}
