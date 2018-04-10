package test.proj.gui.framework.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpPopupMenu;
import com.proj.gui.framework.component.BpTable;

@Component
@Table
public class PopTest extends BpPanel {
	private static final long serialVersionUID = 1L;
	private BpTable table;
	@Override
	public void init() {
		JScrollPane scroll = new JScrollPane();
		table = new BpTable();
		table.title("time","tool","message","more").hide("more");
		scroll.setViewportView(table);
		add(scroll);
		
		Vector<Vector<Object>> datas = new Vector<>();
		for (int i = 0; i < 10; i++) {
			//必须和列相同
			Vector<Object> v = new Vector<Object>();
			v.add("goods"+i);
			v.add("ad"+i);
			v.add("fe"+i);
			v.add("fewaf"+i);
			datas.add(v);
		}
		table.row(datas);
		
		BpPopupMenu tablePop = BpPopupMenu.build(table).addItems("复制详细信息","separator","导入","检查所有");
		tablePop.getItem("复制详细信息").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.copy("more");
			}
		});
		
	}
}
