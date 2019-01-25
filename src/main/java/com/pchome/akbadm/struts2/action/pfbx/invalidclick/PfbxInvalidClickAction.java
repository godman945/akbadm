package com.pchome.akbadm.struts2.action.pfbx.invalidclick;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.pojo.PfbxInvalidTrafficDetail;
import com.pchome.akbadm.db.pojo.PfbxPosition;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.ad.IPfpAdClickService;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusDayReportService;
import com.pchome.akbadm.db.service.pfbx.invalidclick.IPfbxInvalidClickService;
import com.pchome.akbadm.db.service.pfbx.invalidclick.IPfbxInvalidTrafficDetailService;
import com.pchome.akbadm.db.service.pfbx.invalidclick.IPfbxInvalidTrafficService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidClickVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfbx.invalidclick.EnumPfbInvalidTrafficType;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.sequence.EnumSequenceTableName;

public class PfbxInvalidClickAction extends BaseCookieAction {

	private static final long serialVersionUID = 1L;
	
	private IPfbxInvalidClickService pfbxInvalidClickService;
	private IPfbxInvalidTrafficService pfbxInvalidTrafficService;
	private IPfbxInvalidTrafficDetailService pfbxInvalidTrafficDetailService;
	private ISequenceService sequenceService;
	private IPfbxBonusDayReportService pfbxBonusDayReportService;
	private IPfpAdClickService pfpAdClickService;
	private IAdmAccesslogService admAccesslogService;
	private IPfbxPositionService pfbxPositionService;

	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	
	//訊息
	private String message = "";
	
	//頁數
	private int pageNo = 1;       					// 初始化目前頁數
	private int pageSize = 50;     					// 初始化每頁幾筆
	private int pageCount = 0;    					// 初始化共幾頁
	private long totalCount = 0;   					// 初始化共幾筆
	private long totalSize = 0;						//總比數
	
	//輸入參數
	private String startDate;
	private String endDate;
	private String pfbxCustomerInfoId;
	private String pfbxPositionId;
	private String selectType = "1";
	private String mount = "5";
	private String groupPositionId = "";
	
	private Map<String,String> selectTypeMap;
	private LinkedList<String> tableHeadList =null; 				//頁面table head
	private LinkedList<LinkedList<String>> tableDataList =null;		//頁面table data
	private LinkedList<String> align_data = null;
	
	//新增無效流量參數
	private String invSelectType;
	private String invMount;
	private String invGroupPositionId;
	private String invDate;
	private String invPfbId;
	private String invPfbxPositionId;
	private String invInvalidNote1;
	private String invInvalidNote2;
	
	public String execute() throws Exception {
		
		getMapData();
		
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}
		
		/*if (StringUtils.isEmpty(pfbxCustomerInfoId)) {
			this.message = "PFB帳號不可空白！";
			return SUCCESS;
		}*/
		
		getTableHeadData();
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		if(StringUtils.isNotEmpty(startDate)){
			conditionMap.put("startDate", startDate);
		}
		
		if(StringUtils.isNotEmpty(endDate)){
			conditionMap.put("endDate", endDate);
		}
		
		if(StringUtils.isNotEmpty(pfbxCustomerInfoId)){
			conditionMap.put("pfbxCustomerInfoId", pfbxCustomerInfoId);
		}
		
		if(StringUtils.isNotEmpty(pfbxPositionId)){
			conditionMap.put("pfbxPositionId", pfbxPositionId);
		}
		
		if(StringUtils.isNotEmpty(groupPositionId)){
			conditionMap.put("groupPositionId", groupPositionId);
		}
		
		if(StringUtils.isNotEmpty(selectType)){
			conditionMap.put("selectType", selectType);
		}
		
		if(StringUtils.isNotEmpty(mount)){
			conditionMap.put("mount", mount);
		} else {
			conditionMap.put("mount", "5");
		}
		
		List<PfbxInvalidClickVO> resultData = pfbxInvalidClickService.getInvalidClickByCondition(conditionMap);
		
		totalCount = resultData.size();
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
		totalSize = totalCount;
		
		//重新組裝table data
		getTableData(resultData);
		
		return SUCCESS;
	}
	
	public String addInvalidTraffic() throws Exception {
		
		getMapData();
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		Integer number = 0;
		String invNote = "";
		
		if(StringUtils.isNotEmpty(invMount)){
			number = Integer.parseInt(invMount);
		}
		
		if(StringUtils.isNotEmpty(invSelectType)){
			conditionMap.put("invSelectType", invSelectType);
		}
		
		String pfbxPositionIdText = "不分版位";
		if(!StringUtils.equals(invGroupPositionId,"N")){
			if(StringUtils.isNotEmpty(invPfbxPositionId)){
				pfbxPositionIdText = invPfbxPositionId;
				conditionMap.put("invPfbxPositionId", invPfbxPositionId);
			}
		}
		
		if(StringUtils.isNotEmpty(invDate)){
			conditionMap.put("invDate", invDate);
		}
		
		if(StringUtils.isNotEmpty(invPfbId)){
			conditionMap.put("invPfbId", invPfbId);
		}
		
		if(StringUtils.isNotEmpty(invInvalidNote1)){
			invNote += invInvalidNote1;
			conditionMap.put("invInvalidNote1", invInvalidNote1);
		}
		
		if(StringUtils.isNotEmpty(invInvalidNote2)){
			
			if(StringUtils.isNotEmpty(invNote)){
				invNote += " / ";
			}
			invNote += invInvalidNote2;
			
			conditionMap.put("invInvalidNote2", invInvalidNote2);
		}
		
		//檢查是否為當日的紀錄，若為當日則不新增
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		String today = sdf.format(new Date());
		
		long todayTime = sdf.parse(today).getTime();
		long invDateTime = sdf.parse(invDate).getTime();
		
		if(invDateTime >= todayTime){
			return SUCCESS;
		}
		
		
		List<PfpAdClick> dataList = pfbxInvalidClickService.getInvalidData(conditionMap);
		
		if(!dataList.isEmpty()){
			
			List<PfbxInvalidTrafficDetail> detailList = new ArrayList<PfbxInvalidTrafficDetail>();
			List<PfpAdClick> clkList = new ArrayList<PfpAdClick>();
			float invPrice = 0;
			
			for(int i=number;i<dataList.size();i++){
				PfpAdClick data = dataList.get(i);
				
				List<PfbxPosition> pfbxPositionList = pfbxPositionService.findPositionByPfbCustomerId(data.getPfbxPositionId(), invPfbId);
				if (pfbxPositionList.size()<=0){
					log.error(">>>PFB Position Not Found: PositionId = "+data.getPfbxPositionId()+" , PfbId = "+invPfbId);
					continue;
				}
				
				
				PfbxInvalidTrafficDetail pitDetail = new PfbxInvalidTrafficDetail();
				invPrice += data.getAdPrice();
				pitDetail.setAdClickId(data.getAdClickId());
				pitDetail.setPfbxPositionId(data.getPfbxPositionId());
				pitDetail.setAdType(data.getAdType());
				pitDetail.setRecordTime(data.getRecordTime());
				pitDetail.setAdClk(data.getAdClk());
				pitDetail.setAdPrice(data.getAdPrice());
				pitDetail.setUpdateTime(new Date());
				pitDetail.setCreateDate(new Date());
				
				detailList.add(pitDetail);
				
				data.setMaliceType(99);
				data.setUpdateDate(new Date());
				clkList.add(data);
			}
			
			
			//PfbxInvalidTrafficDetail資料大於0才寫PfbxInvalidTraffic table資料
			if (detailList.size()>0){
				PfbxInvalidTraffic pit = new PfbxInvalidTraffic();
				float invPfbBonus = 0;
				
				String invId = sequenceService.getSerialNumber(EnumSequenceTableName.PFBX_INVALID_TRAFFIC);
				
				pit.setInvId(invId);
				pit.setPfbId(invPfbId);
				pit.setInvDate(dateFormate.parse(invDate));
				pit.setInvType(invSelectType);
				pit.setInvNote(invNote);
				pit.setUpdateTime(new Date());
				pit.setCreateDate(new Date());
				
				//當日點擊費用
				float totalClkPrice = pfbxBonusDayReportService.findPfbxTotalClkPrice(invPfbId, dateFormate.parse(invDate), dateFormate.parse(invDate));
				//當日獎金
				float totalBonus = pfbxBonusDayReportService.findPfbxTotalMonthBonus(invPfbId, dateFormate.parse(invDate), dateFormate.parse(invDate));
				//無效獎金 = (無效費用*當日獎金)/當日點擊費用
				if(totalClkPrice != 0 && totalBonus != 0){
					invPfbBonus = (invPrice*totalBonus)/totalClkPrice;
				}
				
				pit.setInvPrice(invPrice);
				pit.setInvPfbBonus(invPfbBonus);
				
				pfbxInvalidTrafficService.saveOrUpdate(pit);
				
				for(PfbxInvalidTrafficDetail pfbxInvalidTrafficDetail:detailList){
					pfbxInvalidTrafficDetail.setPfbxInvalidTraffic(pit);
				}
				
				pfbxInvalidTrafficDetailService.saveOrUpdateAll(detailList);
				
				pfpAdClickService.saveOrUpdateAll(clkList);
				
				//access log
			    String logMsg = "無效類別：" + selectTypeMap.get(invSelectType) + 
			    				";紀錄日期：" + invDate + 
			    				";PFB帳號：" + invPfbId + 
			    				";PFB版位：" + pfbxPositionIdText + 
			    				";數量/花費金額：" + detailList.size() + "/$" + df.format(invPrice);
			    admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.PFB_INVALID_TRAFFIC, logMsg, 
				    super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
				    null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
			}
		
		}
		
		return SUCCESS;
	}
	
	private void getMapData(){
		selectTypeMap = new LinkedHashMap<String,String>();
		for(EnumPfbInvalidTrafficType enumPfbInvalidTrafficType:EnumPfbInvalidTrafficType.values()){
			selectTypeMap.put(enumPfbInvalidTrafficType.getType(), enumPfbInvalidTrafficType.getChName());
		}
	}
	
	private void getTableHeadData(){
		tableHeadList = new LinkedList<String>();
		align_data = new LinkedList<String>();
		tableHeadList.add("記錄日期");
		align_data.add("center");
		/*tableHeadList.add("記錄時間");
		align_data.add("center");*/
		tableHeadList.add("PFB帳戶");
		align_data.add("center");
		
		if(!StringUtils.equals(groupPositionId, "N")){
			tableHeadList.add("PFB版位");
			align_data.add("center");
		}
		
		switch(Integer.parseInt(selectType)) {
			case 1:
				tableHeadList.add("會員編號");
				align_data.add("center");
				break;
			case 2:
				tableHeadList.add("uuid");
				align_data.add("center");
				break;
			case 3:
				tableHeadList.add("uuid");
				align_data.add("center");
				break;
			case 4:
				tableHeadList.add("ip");
				align_data.add("center");
				break;
			case 5:
				tableHeadList.add("referer");
				align_data.add("left");
				break;
			case 6:
				tableHeadList.add("滑鼠移動判斷");
				align_data.add("center");
				break;
			case 7:
				tableHeadList.add("uuid");
				align_data.add("center");
				tableHeadList.add("ip");
				align_data.add("center");
				break;
			case 8:
				tableHeadList.add("user_agent");
				align_data.add("left");
				break;
			default:
				tableHeadList.add("會員編號");
				align_data.add("center");
				break;
		}
		
		tableHeadList.add("點擊數");
		align_data.add("right");
		tableHeadList.add("點擊費用");
		align_data.add("right");
	}
	
	private void getTableData(List<PfbxInvalidClickVO> resultData){
		
		tableDataList = new LinkedList<LinkedList<String>>();
		
		for(int i=0; i<resultData.size(); i++){
			PfbxInvalidClickVO vo = resultData.get(i);
			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
				LinkedList<String> tableData = new LinkedList<String>();
				tableData.add(vo.getRecordDate());
				/*tableData.add(vo.getRecordTime());*/
				tableData.add(vo.getPfbxCustomerInfoId());
				
				if(!StringUtils.equals(groupPositionId, "N")){
					tableData.add(vo.getPfbxPositionId());
				}
				
				switch(Integer.parseInt(selectType)) {
					case 1:
						if(StringUtils.isNotEmpty(vo.getMemId())){
							tableData.add(vo.getMemId());
						} else {
							tableData.add(" ");
						}
						break;
					case 2:
						if(StringUtils.isNotEmpty(vo.getUuid())){
							tableData.add(vo.getUuid());
						} else {
							tableData.add(" ");
						}
						break;
					case 3:
						if(StringUtils.isNotEmpty(vo.getUuid())){
							tableData.add(vo.getUuid());
						} else {
							tableData.add(" ");
						}
						break;
					case 4:
						if(StringUtils.isNotEmpty(vo.getRemoteIp())){
							tableData.add(vo.getRemoteIp());
						} else {
							tableData.add(" ");
						}
						break;
					case 5:
						if(StringUtils.isNotEmpty(vo.getReferer())){
							tableData.add(vo.getReferer());
						} else {
							tableData.add(" ");
						}
						break;
					case 6:
						tableData.add(vo.getMouseMoveFlag());
						break;
					case 7:
						if(StringUtils.isNotEmpty(vo.getUuid())){
							tableData.add(vo.getUuid());
						} else {
							tableData.add(" ");
						}
						if(StringUtils.isNotEmpty(vo.getRemoteIp())){
							tableData.add(vo.getRemoteIp());
						} else {
							tableData.add(" ");
						}
						break;
					case 8:
						if(StringUtils.isNotEmpty(vo.getUserAgent())){
							tableData.add(vo.getUserAgent());
						} else {
							tableData.add(" ");
						}
						break;
					default:
						if(StringUtils.isNotEmpty(vo.getMemId())){
							tableData.add(vo.getMemId());
						} else {
							tableData.add(" ");
						}
						break;
				}
				
				tableData.add(vo.getCount());
				tableData.add("$ " + vo.getPrice());
				
				tableDataList.add(tableData);
			}
		}
	}
	
	public void setPfbxInvalidClickService(IPfbxInvalidClickService pfbxInvalidClickService) {
		this.pfbxInvalidClickService = pfbxInvalidClickService;
	}

	public void setPfbxInvalidTrafficService(IPfbxInvalidTrafficService pfbxInvalidTrafficService) {
		this.pfbxInvalidTrafficService = pfbxInvalidTrafficService;
	}

	public void setPfbxInvalidTrafficDetailService(IPfbxInvalidTrafficDetailService pfbxInvalidTrafficDetailService) {
		this.pfbxInvalidTrafficDetailService = pfbxInvalidTrafficDetailService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setPfbxBonusDayReportService(IPfbxBonusDayReportService pfbxBonusDayReportService) {
		this.pfbxBonusDayReportService = pfbxBonusDayReportService;
	}

	public void setPfpAdClickService(IPfpAdClickService pfpAdClickService) {
		this.pfpAdClickService = pfpAdClickService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public String getMessage() {
		return message;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPfbxCustomerInfoId() {
		return pfbxCustomerInfoId;
	}

	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}

	public String getPfbxPositionId() {
		return pfbxPositionId;
	}

	public void setPfbxPositionId(String pfbxPositionId) {
		this.pfbxPositionId = pfbxPositionId;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public Map<String, String> getSelectTypeMap() {
		return selectTypeMap;
	}

	public LinkedList<String> getTableHeadList() {
		return tableHeadList;
	}

	public LinkedList<LinkedList<String>> getTableDataList() {
		return tableDataList;
	}

	public LinkedList<String> getAlign_data() {
		return align_data;
	}

	public String getGroupPositionId() {
		return groupPositionId;
	}

	public void setGroupPositionId(String groupPositionId) {
		this.groupPositionId = groupPositionId;
	}

	public void setInvSelectType(String invSelectType) {
		this.invSelectType = invSelectType;
	}

	public void setInvMount(String invMount) {
		this.invMount = invMount;
	}

	public void setInvGroupPositionId(String invGroupPositionId) {
		this.invGroupPositionId = invGroupPositionId;
	}

	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}

	public void setInvPfbId(String invPfbId) {
		this.invPfbId = invPfbId;
	}

	public void setInvPfbxPositionId(String invPfbxPositionId) {
		this.invPfbxPositionId = invPfbxPositionId;
	}

	public void setInvInvalidNote1(String invInvalidNote1) {
		this.invInvalidNote1 = invInvalidNote1;
	}

	public void setInvInvalidNote2(String invInvalidNote2) {
		this.invInvalidNote2 = invInvalidNote2;
	}

	public IPfbxPositionService getPfbxPositionService() {
		return pfbxPositionService;
	}

	public void setPfbxPositionService(IPfbxPositionService pfbxPositionService) {
		this.pfbxPositionService = pfbxPositionService;
	}
	
}
