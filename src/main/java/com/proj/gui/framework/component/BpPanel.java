package com.proj.gui.framework.component;

import java.awt.GridBagLayout;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.layout.BpLayout;
import com.proj.gui.framework.layout.BpLayoutHandler;

public abstract class BpPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected BpLayout layout;
	private String name;
	public boolean init = false;
	public boolean close = false;
	public BpPanel() {
		setLayout(new GridBagLayout());
		layout = new BpLayoutHandler();
		
		if(getClass().getGenericSuperclass().getTypeName().equals(BpPanel.class.getTypeName())) {
			for(Annotation an : getClass().getAnnotations()) {
				if(!(an instanceof Component)) {
					continue;
				}
				name = ((Component)an).value();
			}
		}
		if(name==null || name.isEmpty()) {
			name = getClass().getSimpleName();
		}
	}
	public BpPanel(String name) {
		setLayout(new GridBagLayout());
		layout = new BpLayoutHandler();
		this.name = name;
	}
	public void add(JComponent p) {
		layout.grid().line(0)
				.grid().column(0)
				.fill().both()
				.add(p, this);
	}
	public String getTime() {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return sdf.format(new Date());
	 }
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	public BpPanel init0() {
		init = true;
		init();
		return this;
	}
	public abstract void init();
}
