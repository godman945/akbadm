package com.pchome.akbadm.db.service.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.menu.MenuDAO;
import com.pchome.akbadm.db.dao.menu.MenuVO;
import com.pchome.akbadm.db.pojo.AdmMenu;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.config.TestConfig;

public class MenuService extends BaseService<AdmMenu, String> implements IMenuService {

	private MenuDAO menuDAO;

	public List<MenuVO> getSortMenu(List<String> checkedIdList) throws Exception {
		List<AdmMenu> parentList = menuDAO.getParentMenu();
		List<AdmMenu> childrenList = menuDAO.getChildMenu();

		Map<String, MenuVO> parentMap = new LinkedHashMap<String, MenuVO>();
		for (int i=0; i<parentList.size(); i++) {
			AdmMenu menu = parentList.get(i);

			MenuVO menuVO = new MenuVO();
			menuVO.setMenuId(menu.getMenuId().toString());
			menuVO.setDisplayName(menu.getDisplayName());
			menuVO.setAction(menu.getAction());
			menuVO.setSort(Integer.toString(menu.getSort()));

			parentMap.put(menuVO.getMenuId(), menuVO);
		}

		for (int i=0; i<childrenList.size(); i++) {
			AdmMenu menu = childrenList.get(i);
			String parentId = menu.getParentMenuId().toString();

			MenuVO menuVO = new MenuVO();
			menuVO.setMenuId(menu.getMenuId().toString());
			menuVO.setDisplayName(menu.getDisplayName());
			menuVO.setAction(menu.getAction());
			menuVO.setSort(Integer.toString(menu.getSort()));
			if (checkedIdList!=null && checkedIdList.contains(menuVO.getMenuId())) {
				menuVO.setCheckedFlag("checked");
			}

			parentMap.get(parentId).addChildrenList(menuVO);
		}

		return new ArrayList<MenuVO>(parentMap.values());
	}

	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("MenuService");
	    List<MenuVO> parentList = service.getSortMenu(null);

	    for (int i=0; i<parentList.size(); i++) {
	    	MenuVO parentMenu = parentList.get(i);
	    	System.out.println(parentMenu.getDisplayName() + "(" + parentMenu.getMenuId() + ")");
	    	List<MenuVO> childrenList = parentMenu.getChildrenList();
	    	for (int k=0; k<childrenList.size(); k++) {
	    		MenuVO childMenu = childrenList.get(k);
	    		System.out.println("   - " + childMenu.getDisplayName() + "(" + childMenu.getMenuId() + ")  " + childMenu.getAction());
	    	}
	    }

	    System.out.println("===== end test =====");
	}
}
