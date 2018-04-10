package com.proj.gui.framework;

import java.awt.Font;
import java.net.Proxy.Type;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.tools.bp.jdbc.DbUtil;
import com.tools.bp.jdbc.handler.MapHandler;
import com.tools.bp.www.httpclient.Request;
@SuppressWarnings("unchecked")
public class InitConfig {
	private static String PREFIX = AppContext.PREFIX;
	private static DbUtil db = AppContext.getDb();
	public static void initConfig() throws Exception {
		initLookAndFeel();
		initFont();
		initProxy();
	}
	
	private static void initLookAndFeel() throws Exception {
		String table_name = PREFIX+"style";
		//db.update("drop table IF EXISTS style;");
		db.update("CREATE TABLE IF NOT EXISTS '"+table_name+"' ('name'  TEXT);");
		int count = Integer.parseInt(db.fetchOne("select count(name) from "+table_name+";"));
		if(count!=1) {
			db.update("delete from "+table_name+";");
			db.update("insert into "+table_name+" (name) values ('com.jtattoo.plaf.fast.FastLookAndFeel');");
		}
		//统一设置字体,必须在组件初始化之前配置
		String style = db.fetchOne("select name from "+table_name+"");
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		UIManager.setLookAndFeel(style);
	}
	
	private static void initFont() {
		String table_name = PREFIX+"font";
//		db.update("drop table font;");
		db.update("CREATE TABLE IF NOT EXISTS '"+table_name+"' (\"font\"  TEXT,\"name\"  TEXT,\"style\"  TEXT,\"size\"  TEXT);");
		int count = Integer.parseInt(db.fetchOne("select count(font) from "+table_name+";"));
		if(count!=1) {
			db.update("delete from "+table_name+";");
			Font font = Font.decode(null);
			db.update("insert into "+table_name+" (font,name,style,size) values ('"+font.getPSName()+" "+font.getSize()+"ps','"+font.getName()+"','"+font.getStyle()+"','"+font.getSize()+"');");
		}
		//统一设置字体,必须在组件初始化之前配置
		Map<String, Object> map = (Map<String, Object>) db.query("select * from "+table_name, new MapHandler());
		String name = map.get("name").toString();
		int style = Integer.parseInt(map.get("style").toString());
		int size = Integer.parseInt(map.get("size").toString());
		InitGlobalFont(new Font(name, style, size));
	}

	/**
	 * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
	 */
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
//			if(key instanceof String) {  
//				/**设置全局的背景色*/  
//				if(((String) key).endsWith(".background")) {
////					System.out.println("UIManager.put(\""+key+"\", Color.WHITE);");
////					UIManager.put(key, Color.WHITE);  
//				}  
//			}
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
	
	private static void initProxy() {
		String table_name = PREFIX+"proxy";
//		db.update("drop table proxy;");
		db.update("CREATE TABLE IF NOT EXISTS "+table_name+" (\"host\"  TEXT,\"port\"  INTEGER,\"user\"  TEXT,\"pass\"  TEXT,\"type\"  TEXT,\"enable\"  boolean default 0);");
		int count = Integer.parseInt(db.fetchOne("select count(host) from "+table_name+";"));
		if(count!=1) {
			db.update("delete from "+table_name+";");
			db.update("insert into "+table_name+" (host,port,user,pass,type) values ('','','','','');");
		}
		Map<String, Object> map = (Map<String, Object>) db.query("select * from "+table_name, new MapHandler());
		String enable = map.get("enable").toString();
		if(!enable.equals("1")) {
			return;
		}
		String host = map.get("host").toString();
		String port = map.get("port").toString();
		String user = map.get("user").toString();
		String pass = map.get("pass").toString();
		String type = map.get("type").toString();
		Type t = type.equals("HTTP")?Type.HTTP:Type.SOCKS;
		Request.proxy(t,host, Integer.parseInt(port));
		if(!user.trim().equals("") && !pass.trim().equals("")) {
			Request.proxyBasic(user, pass);
		}
	}
}
