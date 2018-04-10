package com.proj.gui.framework.layout;

import java.awt.Container;

import javax.swing.JComponent;

public interface BpLayout {
	Fill fill();
	Grid grid();
	Align align();
	Margin margin();
	Padding padding();
	/**
	 * Automatizes the {@code JComponent#add(java.awt.Component, Object)} using
	 * configured {@code GridBagConstraints} object.
	 * 
	 * @param element
	 * 
	 * @param panel
	 * 
	 * @see JComponent#add(java.awt.Component, Object)
	 */
	void add(JComponent element, Container panel);
	
	public interface Grid {
		BpLayout column(int column);
		BpLayout line(int line);
		BpLayout colspan(int cols);
		BpLayout rowspan(int rows);
		BpLayout width(double with);
		BpLayout height(double height);
	}
	public interface Fill {
		BpLayout horizontal();
		BpLayout vertical();
		BpLayout both();
	}
	public interface Align {
		BpLayout label();
		BpLayout top();
		BpLayout topRight();
		BpLayout topLeft();
		BpLayout bottom();
		BpLayout bottomRight();
		BpLayout bottomLeft();
		BpLayout centered();
		BpLayout left();
		BpLayout right();
	}
	public interface Margin {
		BpLayout same(int margin);
		BpLayout margin(int top, int left, int bottom, int right);
		BpLayout top(int top);
		BpLayout bottom(int bottom);
		BpLayout left(int left);
		BpLayout right(int right);
	}
	public interface Padding {
		BpLayout same(int margin);
		BpLayout width(int left);
		BpLayout height(int right);
	}
}