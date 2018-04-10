package com.proj.gui.framework.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTable;

public class BpTableCellListener implements PropertyChangeListener {
	private PropertyChangeEvent e;
	@SuppressWarnings("unused")
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (!"tableCellEditor".equals(e.getPropertyName())) {
			return;
		}
		if (e==null) {
			this.e = e;
		}
		JTable table = (JTable) e.getSource();
		if (table.isEditing()) {
			cellChangeBefore(this.e);
		} else {
			cellChangeAfter(this.e);
		}
	}
	public void cellChange(PropertyChangeEvent e) {
		this.e = e;
	}
	protected void cellChangeBefore(PropertyChangeEvent e) {}
	protected void cellChangeAfter(PropertyChangeEvent e) {}
}