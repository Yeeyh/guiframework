package com.proj.gui.framework.panel.config.font;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.component.BpPanel;
import com.tools.bp.jdbc.DbUtil;

@Component("generalfont")
public class FontPanel extends BpPanel {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		DbUtil db = AppContext.getDb();
		JLabel lfont = new JLabel("Font And Size: ");
		String font = db.fetchOne("select font from bp_font");
		JTextField tfont = new JTextField(font);
		tfont.setPreferredSize(new Dimension(300,30));
		tfont.setEditable(false);
		tfont.setHorizontalAlignment(JTextField.CENTER);
		JButton change = new JButton("change");
		
		layout.grid().line(0).grid().column(0).margin().left(-760).add(lfont, this);
		layout.grid().line(0).grid().column(1).margin().left(-400).add(tfont, this);
		layout.grid().line(0).grid().column(1).margin().left(-30).add(change, this);
	
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 构造字体选择器，参数字体为预设值
				FontChooser fontChooser = new FontChooser(tfont.getFont());
				// 打开一个字体选择器窗口，参数为父级所有者窗体。返回一个整型，代表设置字体时按下了确定或是取消，可参考MQFontChooser.APPROVE_OPTION和MQFontChooser.CANCEL_OPTION
				int returnValue = fontChooser.showFontDialog(AppContext.getMainFrame());
				// 如果按下的是确定按钮
				if (returnValue == FontChooser.APPROVE_OPTION) {
					// 获取选择的字体
					Font font = fontChooser.getSelectFont();
					// 将字体设置到JTextArea中
					tfont.setText(font.getPSName()+" "+font.getSize()+"pt");
					db.update("update bp_font set font=?, name=?, style=?, size=?",tfont.getText(),font.getName(),font.getStyle(),font.getSize());
				}
			}
		});
	}
}