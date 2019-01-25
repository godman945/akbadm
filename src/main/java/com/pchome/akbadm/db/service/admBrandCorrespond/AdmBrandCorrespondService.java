package com.pchome.akbadm.db.service.admBrandCorrespond;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.admBrandCorrespond.IAdmBrandCorrespondDAO;
import com.pchome.akbadm.db.pojo.AdmBrandCorrespond;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.AdmBrandCorrespondVO;
import com.pchome.config.TestConfig;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class AdmBrandCorrespondService extends BaseService<AdmBrandCorrespond, Integer> implements IAdmBrandCorrespondService{

	/**
	 * 取得 品牌對應關鍵字 資料
	 * @param admBrandCorrespondVO
	 * @return
	 */
	@Override
	public List<AdmBrandCorrespondVO> getBrandCorrespondList(AdmBrandCorrespondVO admBrandCorrespondVO) {
		List<Object> list = ((IAdmBrandCorrespondDAO) dao).getBrandCorrespondList(admBrandCorrespondVO);
		
		List<AdmBrandCorrespondVO> resultData = new ArrayList<AdmBrandCorrespondVO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		for (int i = 0; i < list.size(); i++) {
			Object[] objArray = (Object[]) list.get(i);
			int id = (int) objArray[0];
			String brandEng = (String) objArray[1];
			String brandCh = (String) objArray[2];
			String updateDate = dateFormat.format((Date) objArray[3]);
			String createDate = dateFormat.format((Date) objArray[4]);

			AdmBrandCorrespondVO queryDataVo = new AdmBrandCorrespondVO();
			queryDataVo.setId(id);
			queryDataVo.setBrand_eng(brandEng);
			queryDataVo.setBrand_ch(brandCh);
			queryDataVo.setUpdate_date(updateDate);
			queryDataVo.setCreate_date(createDate);
			
			resultData.add(queryDataVo);
		}

		return resultData;
	}

	/**
	 * 檢查新增的資料是否重複
	 * @param admBrandCorrespond
	 * @return
	 */
	@Override
	public int checkBrandCorrespondTableData(AdmBrandCorrespond admBrandCorrespond) {
		return ((IAdmBrandCorrespondDAO) dao).checkBrandCorrespondTableData(admBrandCorrespond).size();
	}

	/**
	 * 新增品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	@Override
	public void insertBrandCorrespond(AdmBrandCorrespond admBrandCorrespond) {
		Date date = new Date();
		admBrandCorrespond.setUpdateDate(date);
		admBrandCorrespond.setCreateDate(date);
		if (admBrandCorrespond.getBrandEng().isEmpty()) {
			admBrandCorrespond.setBrandEng("NA");
		}

		if (admBrandCorrespond.getBrandCh().isEmpty()) {
			admBrandCorrespond.setBrandCh("NA");
		}
		((IAdmBrandCorrespondDAO) dao).insertBrandCorrespond(admBrandCorrespond);
	}

	/**
	 * 更新品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	@Override
	public void updateBrandCorrespond(AdmBrandCorrespond admBrandCorrespond) {
		if (admBrandCorrespond.getBrandEng().isEmpty()) {
			admBrandCorrespond.setBrandEng("NA");
		}

		if (admBrandCorrespond.getBrandCh().isEmpty()) {
			admBrandCorrespond.setBrandCh("NA");
		}
		((IAdmBrandCorrespondDAO) dao).updateBrandCorrespond(admBrandCorrespond);
	}

	/**
	 * 刪除品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	@Override
	public void deleteBrandCorrespondData(AdmBrandCorrespond admBrandCorrespond) {
		((IAdmBrandCorrespondDAO) dao).deleteBrandCorrespondData(admBrandCorrespond);
	}

	/**
	 * 下載品牌對應關鍵字資料
	 * @param admBrandCorrespondVO
	 * @return
	 */
	@Override
	public StringBuffer makeDownloadReportData(AdmBrandCorrespondVO admBrandCorrespondVO) {
		
		List<AdmBrandCorrespondVO> dataList = getBrandCorrespondList(admBrandCorrespondVO);
		StringBuffer content = new StringBuffer();
		content.append("報表名稱\t品牌對應關鍵字");
		content.append("\n\n");
		
	    content.append("\"英文關鍵字\"");
	    content.append("\t");
	    content.append("\"中文關鍵字\"");
	    content.append("\t");
	    content.append("\n");
		
	    for(int i=0; i < dataList.size(); i++){
	    	content.append("\"" + dataList.get(i).getBrand_eng() + "\"");
	    	content.append("\t");
	    	content.append("\"" + dataList.get(i).getBrand_ch() + "\"");
	    	content.append("\t");
	    	content.append("\n");
	    }
	    content.append("\n");
	    
		return content;
	}

	/**
	 * 處理品牌對應關鍵字api
	 * @param admBrandCorrespondVO
	 * @return
	 */
	@Override
	public Map<String, Object> getBrandCorrespondListApi(AdmBrandCorrespondVO admBrandCorrespondVO) {
		List<Object> list = ((IAdmBrandCorrespondDAO) dao).getBrandCorrespondList(admBrandCorrespondVO);
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Object> resultData = new ArrayList<Object>();

		for (int i = 0; i < list.size(); i++) {
			Object[] objArray = (Object[]) list.get(i);
			String brandEng = (String) objArray[1];
			String brandCh = (String) objArray[2];

			LinkedHashMap<String, String> map = new LinkedHashMap<>();
			if("eng".equalsIgnoreCase(admBrandCorrespondVO.getCheckboxButton())){
				map.put("Brand_eng", brandEng);
			}else if("ch".equalsIgnoreCase(admBrandCorrespondVO.getCheckboxButton())){
				map.put("Brand_ch", brandCh);
			}else if("all".equalsIgnoreCase(admBrandCorrespondVO.getCheckboxButton())){
				map.put("Brand_eng", brandEng);
				map.put("Brand_ch", brandCh);
			}
			
			resultData.add(map);
		}
		
		dataMap.put("brand", resultData);
		return dataMap;
	}
	
	/**
	 * 手動匯入已整理在excle的品牌對應表
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 */
	public static void main(String[] args) throws BiffException, IOException {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		AdmBrandCorrespondService service = (AdmBrandCorrespondService) context.getBean("AdmBrandCorrespondService");
		
		// 創建唯讀的 Excel 工作薄的物件(只能讀取，不能寫入)
        Workbook workbook = Workbook.getWorkbook(new File("D:/品牌對應表20180315.xls"));

        /* 接著建立 Sheet，Workbook可以看成是一個excel檔案，Sheet顧名思義就是一個頁籤。
		         可以直接 getSheet(0) 表示第一個頁籤(從0開始算)
		         也可以透過 getSheet("頁籤名稱") 來取得頁籤*/
        Sheet sheet = workbook.getSheet(0); // (只能讀取，不能寫入)
		
		for (int rows = 0; rows < sheet.getRows(); rows++) {
			AdmBrandCorrespond admBrandCorrespond = new AdmBrandCorrespond();
			admBrandCorrespond.setBrandEng(sheet.getCell(0, rows).getContents());
			admBrandCorrespond.setBrandCh(sheet.getCell(1, rows).getContents());
			System.out.println("第" + (rows+1) + "筆:" + admBrandCorrespond.getBrandEng() + "," + admBrandCorrespond.getBrandCh());
			service.insertBrandCorrespond(admBrandCorrespond);
		}
		System.out.println("匯入完成。");
	}
	
}
