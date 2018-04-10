package com.proj.gui.framework.component;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.proj.gui.framework.layout.BpLayoutHandler;

public class BpToolBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private ImageIcon run = new ImageIcon(getClass().getClassLoader().getResource("framework-run.png"));
	private ImageIcon stop = new ImageIcon(getClass().getClassLoader().getResource("framework-stop.png"));
	private JLabel status;
	private JProgressBar progressBar;
	private JButton btn;
	boolean openStatus;
	boolean openProgressBar;
	boolean openStopBtn;
	public BpToolBar() {
		this(true,true,true);
	}
	public BpToolBar(boolean openStatus) {
		this(openStatus,false,false);
	}
	public BpToolBar(boolean openStatus, boolean openProgressBar, boolean openStopBtn) {
		setLayout(new GridBagLayout());
		BpLayoutHandler layout = new BpLayoutHandler();
		
		status = new JLabel();
		layout.grid().line(0).grid().column(0).fill().both().grid().width(1).add(status, this);
		progressBar = new JProgressBar();
		progressBar.setVisible(true);
		progressBar.setStringPainted(true);
		layout.grid().line(0).grid().column(1).fill().vertical().grid().width(3).align().right().add(progressBar, this);
		btn = new JButton(stop);
		layout.grid().line(0).grid().column(2).fill().both().grid().width(0.02).add(btn, this);
		this.openStatus = openStatus;
		this.openProgressBar = openProgressBar;
		this.openStopBtn = openStopBtn;
		setVisible();
	}
	private void setVisible() {
		status.setVisible(openStatus);
		progressBar.setVisible(openProgressBar);
		btn.setVisible(openStopBtn);
	}
	public BpToolBar progressBar(boolean open) {
		progressBar.setVisible(open);
		return this;
	}
	public BpToolBar status(boolean open) {
		status.setVisible(open);
		return this;
	}
	public BpToolBar stopBtn(boolean open) {
		btn.setVisible(open);
		return this;
	}
	public void setMinimum(int min) {
		progressBar.setMinimum(min);
	}
	public void setMaximum(int max) {
		progressBar.setMinimum(0);
		progressBar.setMaximum(max);
	}
	public void setValue(int value) {
		progressBar.setValue(value);
	}
	public int getValue() {
		return progressBar.getValue();
	}
	public void increment() {
		increment(1);
	}
	public synchronized void increment(int i) {
		progressBar.setValue(progressBar.getValue()+i);
		if(isSuccess()) {
			success("success");
		}
	}
	public synchronized boolean isSuccess() {
		return progressBar.getValue()==progressBar.getMaximum()?true:false;
	}

	public void start() {
		progressBar.setValue(0);
		btn.setIcon(run);
		btn.setEnabled(true);
	}
	public void status(String text){
		status.setText(text);
	}
	public void setText(String text){
		status.setText(text);
		increment();
	}
	public void success() {
		success("success");
	}
	public void success(String text) {
		status.setText(text);
		stop();
	}
	public void stop() {
		btn.setIcon(stop);
		btn.setEnabled(false);
		setVisible();
	}

	
	public void click(StopClickListener listener) {
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listener.mouseClicked(e);
				btn.setEnabled(false);
			}
		});
	}
	public static interface StopClickListener{
		public void mouseClicked(MouseEvent e);
	}
}