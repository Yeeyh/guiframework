package test.proj.gui.framework.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpTable;
@Table
public class TabTest extends BpPanel {
	private static final long serialVersionUID = 1L;
	private BpTable table;
	public void init() {
		JScrollPane scroll = new JScrollPane();
		//每一列的类型必须相同
		table = new BpTable();
		scroll.setViewportView(table);
		//填充标题
		table.title("id","time","tool","message","more").hide("more");
		for (int i = 0; i < 10; i++) {
			//问题:在多类型下，内容不能居中
			table.row(new Object[] {i,false,"tabtest"+i,"msg","other"});
		}
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					table.editor("tool");
				}
			}
		});
		/*
		table.row("tool", "nihao");
		
		Vector<Vector<Object>> datas = new Vector<>();
		for (int i = 0; i < 10; i++) {
			//必须和列相同
			Vector<Object> v = new Vector<Object>();
			v.add(i);
			v.add("goods"+i);
			v.add("ad"+i);
			v.add("fe"+i);
			v.add("fjweo");
			datas.add(v);
		}
		table.row(datas);
		table.row("time", "googog");
		*/
		JTextField filter = new JTextField("regex");
		JTextField exclude = new JTextField("exclude");
		layout.grid().line(0).grid().column(0).fill().both().add(filter, this);
		layout.grid().line(1).grid().column(0).fill().both().add(exclude, this);
		layout.grid().line(2).grid().column(0).fill().both().add(scroll, this);
		//设置过滤
		filter.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "filterTable");  
    filter.getActionMap().put("filterTable", new AbstractAction() {  
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {  
        	String text = filter.getText();
						table.filter(text);
          }  
    });  
    exclude.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "filterTable");  
    exclude.getActionMap().put("filterTable", new AbstractAction() {  
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {  
        	String text = exclude.getText();
						table.excludeFilter(text);
          }  
    });  
		
//		BpTableCellListener tableAction = new BpTableCellListener(){
//			public void cellChangeAfter(PropertyChangeEvent e) {
//				executor.execute(new Runnable() {
//					public void run() {
//						String name = e.getPropertyName();
//						JTextField edit = (JTextField) e.getSource();
//						String oldValue = e.getOldValue().toString();
//						System.out.println(name);
//						System.out.println(edit);
//						System.out.println(oldValue);
//					}
//				});
//			}
//		};
//		table.addPropertyChangeListener(tableAction);
//		JTextField editor = table.editor("tool");
//		String oldName = editor.getText();
//		PropertyChangeEvent event = new PropertyChangeEvent(editor, "tool", oldName, "");
//		tableAction.cellChange(event);
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("Sorting JTable");  
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   TabTest t = new TabTest();
   t.init0();
   frame.add(t,BorderLayout.CENTER);
   
//   bptable(frame); 
   
   frame.setSize(300, 250);  
   frame.setVisible(true);  
	}
	private static void bptable(JFrame frame) {
		BpTable table = new BpTable("domain", "address", "numaddress", "local");
		   for (int i = 0; i < 10; i++) {
		   	table.row(new Object[] { "aa"+i, "aa"+i, "aa"+i, "aa"+i });
						}
		   JScrollPane pane = new JScrollPane(table);  
		   frame.add(pane, BorderLayout.CENTER);  
		   JPanel panel = new JPanel(new BorderLayout());  
		   JLabel label = new JLabel("Filter");  
		   panel.add(label, BorderLayout.WEST);        
		   final JTextField filterText = new JTextField("aa0");  
		   panel.add(filterText, BorderLayout.CENTER);  
		   frame.add(panel, BorderLayout.NORTH); 
		   Action filterAction = new AbstractAction() {  
		       public void actionPerformed(ActionEvent e) {  
		           String text = filterText.getText();  
		           table.excludeFilter(text);
		         }  
		   };  
		   filterText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),  
		           "filterTable");  
		   filterText.getActionMap().put("filterTable",  
		           filterAction);
	}
}
