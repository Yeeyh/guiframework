package com.proj.gui.framework;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.proj.gui.framework.component.BpController;
import com.proj.gui.framework.component.BpMenu;
import com.proj.gui.framework.component.BpPanel;
import com.tools.bp.jdbc.DbUtil;

@org.springframework.stereotype.Component
public final class AppContext {
	//-------------config------------------
	public static int WIDTH=1024;
	public static int HEIGHT=WIDTH * 768 / 1024;
	public static String DBNAME="bp.db";
	public static String CONTEXT = "applicationContext.xml";
	public static String TITLE = "bp 1.0 Release";
	public static String PREFIX = "bp_";
	//-------------------------------------
	private static DbUtil db;
	private static ApplicationContext ac;
	//存储TabFrame和MenuFrame的对象
	private static Map<Class<?>, Object> frames;  
	//存储用户自定义的面板的对象
	private static Map<String, BpPanel> panels;  	
	private static Map<String, BpController> controllers;  
	//存储用户自定义的菜单的对象
	private static Map<String, BpMenu> menus;  				

	@SuppressWarnings("unchecked")
	public static <T> T getFrame(Class<T> clazz) {
		return (T) frames.get(clazz);
	}
	
	public static <T> void setFrame(Class<T> clazz, T t) {
		frames.put(clazz, t);
	}
	
	public static MainFrame getMainFrame() {
		return (MainFrame) frames.get(MainFrame.class);
	}
	public static TabFrame getTabFrame() {
		return (TabFrame) frames.get(TabFrame.class);
	}
	public static MenuFrame getMenuFrame() {
		return (MenuFrame) frames.get(MenuFrame.class);
	}

	public static Map<String, BpPanel> getPanels() {
		return panels;
	}

	public static BpPanel getPanel(String name) {
		return panels.get(name);
	}
	
	public static Map<String, BpController> getControllers() {
		return controllers;
	}
	public static BpController getController(String name) {
		return controllers.get(name);
	}
	
	public static BpPanel getPanel(Class<? extends BpPanel> clazz) {
		return ac.getBean(clazz);
	}

	public static Map<String, BpMenu> getMenus() {
		return menus;
	}

	public static BpMenu getMenu(String name) {
		return menus.get(name);
	}
	
	public static void setMenu(String name, BpMenu menu) {
		menus.put(name,menu);
	}
	
	public static BpMenu getMenu(Class<BpMenu> clazz) {
		return ac.getBean(clazz);
	}
	
	public static Object getBean(String name) {
		return ac.getBean(name);
	}
	
	public static DbUtil getDb() {
		return db;
	}
	
	/*
	 * 注册外部bean到spring容器里
	 */
	public static void register(BpPanel p) {
		if (ac == null) {
			throw new RuntimeException("applicationContext is null");
		}
		//获取BeanFactory有以下两种
		//使用该种有时候拿到的beanFactory是null，特别是在应用第三方框架时
//		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)ac.getParentBeanFactory();
		//用于存储Spring容器管理之外的Bean，Spring内部很少使用
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)ac.getAutowireCapableBeanFactory();  
		
		//根据obj的类型、创建一个新的bean、添加到Spring容器中，  
		//注意BeanDefinition有不同的实现类，注意不同实现类应用的场景  
		//注入一个新的Bean到Spring容器中 
//		BeanDefinition beanDefinition = new GenericBeanDefinition();  
//		beanDefinition.setBeanClassName(p.getClass().getName());  
//		beanFactory.registerBeanDefinition(p.getClass().getName(), beanDefinition);
		//一般重复注入一个新Bean的情况较少，多数情况都是讲已有的Bean注入到容器中
		beanFactory.applyBeanPostProcessorsAfterInitialization(p, p.getClass().getSimpleName());  
   beanFactory.registerSingleton(p.getClass().getSimpleName(), p); 
   
	}
	
	public static void setLocatinAndSize(Component c, int width, int height) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension dimension = kit.getScreenSize();
		int screen_width = (int) dimension.getWidth();
		int screen_height = (int) dimension.getHeight();
		// 如果显示屏幕分辨率没有已设定的面板大, 则按显示屏幕分辨率显示
		width = screen_width < width ? screen_width : width;
		height = screen_height < height ? screen_height : height;
		c.setLocation((screen_width - width) / 2, (screen_height - height) / 2); // 默认距屏幕左上角(0, 0), 此处设为居中显示屏幕
		c.setSize(width, height); // 大小
	}
	
	public static void start() {
		PluginLoader.loadClasspath();
		db = DbUtil.getSqlite(DBNAME);
		ac = new ClassPathXmlApplicationContext(CONTEXT);
		frames = new LinkedHashMap<Class<?>, Object>();
		frames.put(MainFrame.class, ac.getBean(MainFrame.class));
		frames.put(TabFrame.class,  ac.getBean(TabFrame.class));  		
		frames.put(MenuFrame.class, ac.getBean(MenuFrame.class));
		//includeNonSingletons 是否包含非单例
		//allowEagerInit 是否包含懒加载
		panels = ac.getBeansOfType(BpPanel.class,false,false);  
		controllers = ac.getBeansOfType(BpController.class,false,false);
		menus = ac.getBeansOfType(BpMenu.class,false,false);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					InitConfig.initConfig();
					getMainFrame().init();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	public static void main(String[] args) {
		AppContext.start();
	}
}
