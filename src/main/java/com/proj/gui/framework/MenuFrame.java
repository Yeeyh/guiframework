package com.proj.gui.framework;

import java.util.Map;

import javax.swing.JMenuBar;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Menu;
import com.proj.gui.framework.component.BpMenu;
import com.proj.gui.framework.panel.exception.AlertsException;
@Component
public class MenuFrame extends JMenuBar {
	private static final long serialVersionUID = 1L;
	public MenuFrame init() {
		Map<String, BpMenu> menus = AppContext.getMenus();
		for(BpMenu bm : menus.values()) {
			try {
				Menu an = bm.getClass().getAnnotation(Menu.class);
				if(an==null) continue;
				Class<?> clazz = an.value();
				if(!BpMenu.class.equals(clazz.getSuperclass())) {
					add(bm.init());
				} else {
					@SuppressWarnings("unchecked")
					BpMenu menu = AppContext.getMenu((Class<BpMenu>)clazz);
					menu.add(bm.init());
				}
			} catch (Exception e) {
				AlertsException.error(bm.getName(),e);
			}
		}
		return this;
	}
}
