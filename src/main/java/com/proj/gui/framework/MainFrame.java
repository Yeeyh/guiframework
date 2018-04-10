package com.proj.gui.framework;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.springframework.stereotype.Component;
@Component
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public void init() {
		setTitle(AppContext.TITLE);
		AppContext.setLocatinAndSize(this, AppContext.WIDTH, AppContext.HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(1, 1));
		setVisible(true);
		add(AppContext.getFrame(TabFrame.class).init(), BorderLayout.CENTER);
		setJMenuBar(AppContext.getMenuFrame().init());
		AppContext.setFrame(MainFrame.class, this);
	}
}
