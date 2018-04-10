package com.proj.gui.framework.panel.exception;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.TabFrame;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpPopupMenu;
import com.proj.gui.framework.component.BpTable;
@Component("alerts")
public class AlertsPanel extends BpPanel {
	private static final long serialVersionUID = 1L;
	private BpTable table;
	
	public void init() {
		JScrollPane scroll = new JScrollPane();
		table = new BpTable();
		table.title("time","tool","message","more").hide("more");
		scroll.setViewportView(table);
		add(scroll);
		
		BpPopupMenu tablePop = BpPopupMenu.build(table).addItems("复制详细信息");
		tablePop.getItem("复制详细信息").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.copy("more");
			}
		});
	}
	
	public boolean isEnabled() {
		return false;
	}
	
	public void error(String tool,String message,String fullMsg) {
		table.row(new Object[] {getTime(),tool,message,fullMsg});
		TabFrame frame = AppContext.getTabFrame();
		frame.setForegroundAt(frame.indexOfComponent(this), Color.RED);
	}
}
