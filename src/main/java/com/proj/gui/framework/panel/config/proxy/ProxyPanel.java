package com.proj.gui.framework.panel.config.proxy;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Proxy.Type;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.panel.exception.AlertsException;
import com.tools.bp.jdbc.DbUtil;
import com.tools.bp.jdbc.handler.MapHandler;
import com.tools.bp.www.httpclient.Request;

@Component("generalproxy")
public class ProxyPanel extends BpPanel {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		DbUtil db = AppContext.getDb();
		
		Dimension dim = new Dimension(300, 30);
		JLabel lhost = new JLabel("host:");
		JTextField host = new JTextField();
		host.setPreferredSize(dim);
		JLabel lport = new JLabel("port:");
		JTextField port = new JTextField();
		port.setPreferredSize(dim);
		JLabel luser = new JLabel("user:");
		JTextField user = new JTextField();
		user.setPreferredSize(dim);
		JLabel lpass = new JLabel("pass:");
		JTextField pass = new JTextField();
		pass.setPreferredSize(dim);
		JLabel ltype = new JLabel("type:");
		JComboBox<String> type = new JComboBox<String>();
		type.addItem("HTTP");
		type.addItem("SOCKS");
		
		JButton save = new JButton("save");
		JCheckBox enable = new JCheckBox("Enable");
		
		layout.grid().line(0).grid().column(0).margin().left(-850).add(lhost, this);
		layout.grid().line(0).grid().column(1).margin().left(-520).add(host, this);
		layout.grid().line(1).grid().column(0).margin().left(-850).add(lport, this);
		layout.grid().line(1).grid().column(1).margin().left(-520).add(port, this);
		layout.grid().line(2).grid().column(0).margin().left(-850).add(luser, this);
		layout.grid().line(2).grid().column(1).margin().left(-520).add(user, this);
		layout.grid().line(3).grid().column(0).margin().left(-850).add(lpass, this);
		layout.grid().line(3).grid().column(1).margin().left(-520).add(pass, this);
		layout.grid().line(4).grid().column(0).margin().left(-850).add(ltype, this);
		layout.grid().line(4).grid().column(1).margin().left(-740).add(type, this);
		layout.grid().line(4).grid().column(2).margin().left(-600).add(save, this);
		layout.grid().line(4).grid().column(2).margin().left(-290).add(enable, this);

		
		Map<String, Object> map = (Map<String, Object>) db.query("select * from bp_proxy", new MapHandler());
		String ihost = map.get("host").toString();
		String iport = map.get("port").toString();
		String iuser = map.get("user").toString();
		String ipass = map.get("pass").toString();
		String itype = map.get("type").toString();
		boolean ienable = map.get("enable").toString().equals("1")?true:false;
		host.setText(ihost);
		port.setText(iport);
		user.setText(iuser);
		pass.setText(ipass);
		enable.setSelected(ienable);
		if(!itype.equals(""))
		{
			type.setSelectedItem(itype);
		}
		
		//listen
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String shost = host.getText().trim();
				String sport = port.getText().trim();
				String suser = user.getText().trim();
				String spass = pass.getText().trim();
				String stype = type.getSelectedItem().toString();
				boolean senable = enable.isSelected(); 
				int res = db.update("update bp_proxy set host=?,port=?,user=?,pass=?,type=?,enable=?",shost,sport,suser,spass,stype,senable);
				if(res!=1) {
					AlertsException.error("options", new SQLException("proxy update error"));
				}
			}
		});
		enable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.update("update bp_proxy set enable=?",enable.isSelected());
				if(!enable.isSelected()) {
					Request.proxyClean();
					return;
				}
				String shost = host.getText().trim();
				String sport = port.getText().trim();
				String suser = user.getText().trim();
				String spass = pass.getText().trim();
				String stype = type.getSelectedItem().toString();
				Type t = stype.equals("HTTP")?Type.HTTP:Type.SOCKS;
				Request.proxy(t,shost, Integer.parseInt(sport));
				if(!suser.trim().equals("") && !spass.trim().equals("")) {
					Request.proxyBasic(suser, spass);
				}
			}
		});
	}
}
