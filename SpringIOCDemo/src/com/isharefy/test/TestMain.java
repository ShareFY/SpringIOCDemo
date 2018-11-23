package com.isharefy.test;

import com.isharefy.beans.Dept;
import com.isharefy.service.DeptService;
import com.isharefy.utils.ApplicationContext;

/**
 * Created by Forever on 2018-11-23
 * From : http://www.isharefy.com/
 * QQ : 434551893
 */
public class TestMain {

	public static void main(String[] args) {

		ApplicationContext context = new ApplicationContext();
//		Dept dept1 = (Dept) context.getBean("dept");
//		Dept dept2 = (Dept) context.getBean("dept");
//		System.out.println(dept1 == dept2); // 单例的时候，创建的两个对象是一样的
		
		DeptService service = (DeptService) context.getBean("myService");
		service.deptAdd();
	}

}
