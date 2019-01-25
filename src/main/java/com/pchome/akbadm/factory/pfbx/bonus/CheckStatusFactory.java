package com.pchome.akbadm.factory.pfbx.bonus;

import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceCheckStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.account.EnumPfbxCheckStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;


//取出各種Enum對應的值
public class CheckStatusFactory
{
	protected Log log = LogFactory.getLog(this.getClass().getName());
	
	public EnumPfbApplyStatus getOneEnumPfbApplyStatus(String code)
	{
		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbApplyStatus e : EnumPfbApplyStatus.values())
			{
				if (StringUtils.equals(e.getStatus(), code))
				{
					return e;
				}
			}
		}
		
		return null;
	}
	
	public EnumPfbApplyInvoiceStatus getOneEnumInEnumPfbApplyInvoiceStatus(String code)
	{
		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbApplyInvoiceStatus e : EnumPfbApplyInvoiceStatus.values())
			{
				if (StringUtils.equals(e.getStatus(), code))
				{
					return e;
				}
			}
		}
		return null;
	}

	public EnumPfbApplyInvoiceCheckStatus getOneEnumInEnumPfbApplyInvoiceCheckStatus(String code)
	{
		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbApplyInvoiceCheckStatus e : EnumPfbApplyInvoiceCheckStatus.values())
			{
				if (StringUtils.equals(e.getStatus(), code))
				{
					return e;
				}
			}
		}
		return null;
	}
	
	public String getEnumPfbxCheckStatusName(String code)
	{
		String resultStr = "";

		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbxCheckStatus e : EnumPfbxCheckStatus.values())
			{
				if (StringUtils.equals(e.getStatus(), code))
				{
					resultStr = e.getcName();
					break;
				}
			}
		}
		else
		{
			resultStr = "StatusCodeError";
		}

		return resultStr;
	}
	
	public String getEnumPfbxAccountCategoryName(String code)
	{
		String resultStr = "";

		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbxAccountCategory e : EnumPfbxAccountCategory.values())
			{
				if (StringUtils.equals(e.getCategory(), code))
				{
					resultStr = e.getChName();
					break;
				}
			}
		}
		else
		{
			resultStr = "CodeError";
		}

		return resultStr;
	}

	public String getEnumPfbAccountStatusName(String code)
	{
		String resultStr = "";

		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbAccountStatus e : EnumPfbAccountStatus.values())
			{
				if (StringUtils.equals(e.getStatus(), code))
				{
					resultStr = e.getDescription();
					break;
				}
			}
		}
		else
		{
			resultStr = "CodeError";
		}

		return resultStr;
	}
	
	public String getEnumPfbApplyStatusName(String code)
	{
		String resultStr = "";

		if (StringUtils.isNotBlank(code))
		{
			for (EnumPfbApplyStatus e : EnumPfbApplyStatus.values())
			{
				if (StringUtils.equals(e.getStatus(), code))
				{
					resultStr = e.getChName();
					break;
				}
			}
		}
		else
		{
			resultStr = "StatusCodeError";
		}

		return resultStr;
	}
}
