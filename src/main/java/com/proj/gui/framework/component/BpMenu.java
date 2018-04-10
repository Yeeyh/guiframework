package com.proj.gui.framework.component;

import java.lang.annotation.Annotation;

import javax.swing.JMenu;
import javax.swing.SwingConstants;

import org.springframework.stereotype.Component;

public class BpMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private String name;
	public BpMenu() {
		setHorizontalAlignment(SwingConstants.CENTER);
		if(getClass().getGenericSuperclass().getTypeName().equals(BpMenu.class.getTypeName()) && 
				getClass().getDeclaredAnnotations().length>0) {
			Annotation an = getClass().getDeclaredAnnotations()[0];
			Component c = (Component)an;
			this.name = c.value();
		}
		if(name==null || name.isEmpty()) {
			name = getClass().getSimpleName();
		}
		setText(name);
	}
	public BpMenu(String name) {
		this();
		setText(name);
		this.name = name;
	}
	public boolean isEnabled() {
		return true;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	public BpMenu init() {
		return this;
	}
	
}
