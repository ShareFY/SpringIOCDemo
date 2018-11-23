package com.isharefy.beans;

/**
 * Created by Forever on 2018-11-23
 * From : http://www.isharefy.com/
 * QQ : 434551893
 */
public class Dept {

	public Dept() {
		System.out.println("Dept的构造方法被调用了。。。");
	}

	private int deptNo;
	private String dname;
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	
//	@Override
//	public String toString() {
//		return "Dept [deptNo=" + deptNo + ", dname=" + dname + "]";
//	}
	
}
