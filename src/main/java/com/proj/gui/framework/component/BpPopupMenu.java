package com.proj.gui.framework.component;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class BpPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	private Map<String,JMenuItem> items;
	private Map<String,JMenu> menus;

	private Component c;
	private BpPopupMenu self;
	
	public static BpPopupMenu build(Component c) {
		return new BpPopupMenu(c);
	}

	private BpPopupMenu(Component c) {
		super();
		this.c = c;
		self = this;
		c.addMouseListener(new PopupMenuListener());
		items = new LinkedHashMap<String,JMenuItem>();
		menus = new LinkedHashMap<String,JMenu>();
	}
	
	public void setComponent(Component c) {
		this.c = c;
	}
	
	protected class PopupMenuListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON3)
				return;
			self.show(c, e.getX(), e.getY());
		}
	}
	public BpPopupMenu separator() {
		addSeparator();
		return this;
	}
	public BpPopupMenu addItems(String ... names) {
		return addItems(null,names);
	}
	public BpPopupMenu addItems(String menu, String[] names) {
		for(String name : names) {
			if(name.equalsIgnoreCase("separator")) {
				addSeparator();
				continue;
			}
			JMenuItem item = new JMenuItem(name);
			if(menu == null) {
				add(item);
			} else {
				menus.get(menu).add(item);
			}
			this.items.put(name, item);
		}
		return this;
	}
	public BpPopupMenu addMenus(String ... names) {
		return addMenus(null,names);
	}
	public BpPopupMenu addMenus(String menu,String[] names) {
		for(String name : names) {
			if(name.equalsIgnoreCase("separator")) {
				addSeparator();
				continue;
			}
			JMenu item = new JMenu(name);
			if(menu == null) {
				add(item);
			} else {
				menus.get(menu).add(item);
			}
			this.menus.put(name, item);
		}
		return this;
	}
	
	public JMenuItem getItem(String name) {
		return items.get(name);
	}
	public JMenu getMenus(String name) {
		return menus.get(name);
	}
}
