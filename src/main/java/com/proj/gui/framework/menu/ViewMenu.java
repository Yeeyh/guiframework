package com.proj.gui.framework.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.TabFrame;
import com.proj.gui.framework.annotation.Menu;
import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpMenu;
import com.proj.gui.framework.component.BpPanel;

@Component("view")
@Menu
public class ViewMenu extends BpMenu {
	private static final long serialVersionUID = 1L;
	private Map<String,JCheckBox> items = new HashMap<>();
	public JCheckBox getItem(String name) {
		return items.get(name);
	}
	@SuppressWarnings("unchecked")
	public BpMenu init() {
		for (BpPanel p : AppContext.getPanels().values()) {
			String name = p.getName();
			Table an = p.getClass().getAnnotation(Table.class);
			if(an==null) continue;
			Class<?> clazz = an.value();
			
			boolean boo = false;
			for(java.awt.Component c : p.getComponents()){
				if(c instanceof TabFrame) {
					boo = true;
				}
			}
			BpMenu menu = null;
			if(!BpPanel.class.equals(clazz.getSuperclass())) {
				menu = this;
			}else {
				BpPanel panel = AppContext.getPanel((Class<BpPanel>)clazz);
				menu = AppContext.getMenu(panel.getName());
				if(menu==null) {
					menu = new BpMenu(panel.getName());
					AppContext.setMenu(panel.getName(), menu);
				}
				JCheckBox cmi = items.get(panel.getName());
				if(cmi==null) {
					cmi = new JCheckBox(panel.getName());
					cmi.setSelected(panel.isEnabled());
					menu.add(cmi);
					items.put(panel.getName(), cmi);
					listener(cmi,panel);
				}
			}
			if(boo) {
				BpMenu bm = AppContext.getMenu(name);
				if(bm==null) {
					bm = new BpMenu(name);
					AppContext.setMenu(name, bm);
				}
				menu.add(bm);
			}else {
				JCheckBox cmi = new JCheckBox(name);
				cmi.setSelected(p.isEnabled());
				menu.add(cmi);
				items.put(name, cmi);
				listener(cmi,p);
			}
		}
		return this;
	}
	private void listener(JCheckBox cmi, BpPanel p) {
		TabFrame tab = AppContext.getTabFrame();
		cmi.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Table an = p.getClass().getAnnotation(Table.class);
				if(an==null) return;
				Class<?> clazz = an.value();
				if(!BpPanel.class.equals(clazz.getSuperclass())) {
					if (cmi.isSelected()) {
						tab.setTab(p,3,true,false);
					} else {
						tab.remove(p);
					}
				}else {
					@SuppressWarnings("unchecked")
					BpPanel bp = AppContext.getPanel((Class<BpPanel>)clazz);
					TabFrame tf = null;
					for(java.awt.Component c : bp.getComponents()){
						if(!(c instanceof TabFrame)) {
							continue;
						}
						tf = (TabFrame)c;
					}
					if(tf==null) {
						tf = new TabFrame();
					}
					if (cmi.isSelected()) {
						tf.setTab(p,3,true,false);
						bp.add(tf);
					} else {
						tf.remove(p);
					}
				}
			}
		});
	}
}
