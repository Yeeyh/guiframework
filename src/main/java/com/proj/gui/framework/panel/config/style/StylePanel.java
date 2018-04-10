package com.proj.gui.framework.panel.config.style;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.MainFrame;
import com.proj.gui.framework.component.BpPanel;
import com.tools.bp.jdbc.DbUtil;

@Component("generalstyle")
public class StylePanel extends BpPanel {
	
	private static final long serialVersionUID = 1L;
	@Override
	public void init() {
		DbUtil db = AppContext.getDb();
		Map<String, String> names = new LinkedHashMap<String, String>();
		names.put("com.sun.java.swing.plaf.metal.MetalLookAndFeel", "Metal");
		names.put("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", "Nimbus");
		names.put("com.sun.java.swing.plaf.gtk.GTKLookAndFeel", "GTK+");
		names.put("com.jtattoo.plaf.fast.FastLookAndFeel", "纯XP风格");
		names.put("com.jtattoo.plaf.aero.AeroLookAndFeel", "xp清新风格");
		names.put("com.jtattoo.plaf.smart.SmartLookAndFeel", "木质感xp风格");
		
		Map<String, String> styleArr = new LinkedHashMap<String, String>();
		styleArr.put("Metal", "com.sun.java.swing.plaf.metal.MetalLookAndFeel");
		styleArr.put("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		styleArr.put("GTK+", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		styleArr.put("纯XP风格", "com.jtattoo.plaf.fast.FastLookAndFeel");
		styleArr.put("xp清新风格", "com.jtattoo.plaf.aero.AeroLookAndFeel");
		styleArr.put("木质感xp风格", "com.jtattoo.plaf.smart.SmartLookAndFeel");
		
		JLabel lfont = new JLabel("Look And Feel: ");
		String style = db.fetchOne("select name from bp_style");
		JTextField tstyle = new JTextField(names.get(style));
		tstyle.setPreferredSize(new Dimension(300,30));
		tstyle.setEditable(false);
		tstyle.setHorizontalAlignment(JTextField.CENTER);
		JButton change = new JButton("change");
		
		layout.grid().line(0).grid().column(0).margin().left(-760).add(lfont, this);
		layout.grid().line(0).grid().column(1).margin().left(-400).add(tstyle, this);
		layout.grid().line(0).grid().column(1).margin().left(-30).add(change, this);

		MainFrame main = AppContext.getMainFrame();
		JDialog jDialog = new JDialog(main,"",true);
		AppContext.setLocatinAndSize(jDialog,150,240);
		jDialog.setResizable(false);
		jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JList<String> styleList = new JList<String>(styleArr.keySet().toArray(new String[]{})) ;
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		styleList.setCellRenderer(renderer);
		JScrollPane jp = new JScrollPane(styleList);
		jDialog.getContentPane().add(jp);
		
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jDialog.setVisible(true);
			}
		});
		styleList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount()!=2) {
					return;
				}
				String style = styleList.getSelectedValue();
				db.update("update bp_style set name=?",styleArr.get(style));
				tstyle.setText(style);
				jDialog.setVisible(false);
			}
		});
	}
}