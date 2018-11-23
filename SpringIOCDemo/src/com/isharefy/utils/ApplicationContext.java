package com.isharefy.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by Forever on 2018-11-23
 * From : http://www.isharefy.com/
 * QQ : 434551893
 */
public class ApplicationContext {

	private static Document config;
	private Class classFile = null;
	private static Map<String, Object> staticContainer = new HashMap<>(); // 存放single类型对象
	
	static {
		// IOC 容器启动时，加载配置文件
		SAXReader saxReader = new SAXReader();
		try {
			config = saxReader.read("src/applicationContext.xml");
			System.out.println("IOC启动时，加载配置文件。。。");
			// 容器启动时，加载single类
			createBean();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 容器启动时，加载single类
	private static void createBean() throws Exception {
		String xpath = "//bean[@scope='single']";
		List<Element> elementList = null;
		// 1、找到配置文件中所有scpoe为single的bean
		elementList = config.selectNodes(xpath);
		// 2、遍历所有的bean标签
		for (Element element : elementList) {
			String classPath = element.attributeValue("class");
			Object obj = Class.forName(classPath).newInstance();
			staticContainer.put(classPath, obj);
		}
	}
	
	// 根据Bean对应的id返回Bean对象
	public Object getBean(String beanId) {
		Object bean = null;
		List<Element> list = null;
		String classPath = null;
		
		// 通过XPath解析配置文件
		String xPath = "//bean[@id='"+beanId+"']";
		list = config.selectNodes(xPath);
		Element elementObj = list.get(0);
		classPath = elementObj.attributeValue("class");
		// 看看容器中是否已经存在Bean对象了
		bean = staticContainer.get(classPath);
		// 通过反射机制创建bean对象
		try {
			classFile = Class.forName(classPath);
			if(bean == null) { //容器中没有bean对象时，在通过实例化创建对象
				bean = classFile.newInstance();
			}
			init(bean, elementObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	// 对Bean对象进行初始化
	private void init(Object bean,Element beanElement) throws Exception {
		System.out.println("IOC容器开始对Bean对象进行初始化处理。。。");
		// 检索当前标签是否有子标签
		List<Element> propertyList = beanElement.elements("property");
		if (propertyList == null || propertyList.size() == 0) {
			return;
		}
		
		// 对bean对象进行赋值
		for (Element property : propertyList) {
			// 读取name属性和value属性
			String fieldName  = property.attributeValue("name"); // deptNo
			String fieldValue = property.attributeValue("value");
			Field fieldObj = classFile.getDeclaredField(fieldName); // private int deptNo;
			// 获得当前属性的数据类型名
			String typeName = fieldObj.getType().getName(); // int
			String ref = property.attributeValue("ref");
			if(ref == null || "".equals(ref)) { // 普通属性，直接赋值
				// 根据属性类型进行赋值实现
				setValue(bean,fieldObj,typeName,fieldValue);
			}else {
				// 依赖注入的实现
				setDI(bean, ref, fieldObj);
			}
			
			
		}
	}
	
	// 根据属性类型进行赋值实现
	private void setValue(Object bean, Field field,String typeName, String value) throws Exception {
		field.setAccessible(true);
		if("java.lang.String".equals(typeName)) {
			field.set(bean, value);
		}else if ("int".equals(typeName)) {
			field.set(bean, Integer.valueOf(value));
		}
	}
	
	// 依赖注入的实现
	private void setDI(Object bean, String ref, Field fieldObj) throws Exception {
		String xpath = "//bean[@id='"+ref+"']";
		Element element = null;
		String classPath = null;
		Object oldBean = null;
		
		// 1、定位ref属性关联的bean标签
		element = (Element) config.selectNodes(xpath).get(0);
		// 2、取当前bean标签的class属性
		classPath = element.attributeValue("class");
		// 3、去 staticContainer 静态容器索要对应的bean
		oldBean = staticContainer.get(classPath);
		if(oldBean == null) { // DeptDao类在静态容器中没有实例对象
			Class.forName(classPath).newInstance();
		}
		fieldObj.setAccessible(true);
		fieldObj.set(bean, oldBean);
	}
	
	// 关闭容器
	public void close() {
		staticContainer.clear();
	}
	
}
