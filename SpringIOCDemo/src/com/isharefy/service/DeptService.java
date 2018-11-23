package com.isharefy.service;

import com.isharefy.dao.DeptDao;

/**
 * Created by Forever on 2018-11-23
 * From : http://www.isharefy.com/
 * QQ : 434551893
 */
public class DeptService {

	private DeptDao dao;
	
	public DeptDao getDao() {
		return dao;
	}

	public void setDao(DeptDao dao) {
		this.dao = dao;
	}


	public void deptAdd() {
		dao.save();
	}
	
}
