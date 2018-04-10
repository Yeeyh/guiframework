package test.proj.gui.framework.tree;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpTree;
@Table
public class TreeTest extends BpPanel {
	private static final long serialVersionUID = 1L;
	public void init() {
		BpTree tree = new BpTree();
		//渲染tree图标
		tree.setCellRenderer(new DefaultTreeCellRenderer(){
			private static final long serialVersionUID = 1L;
			@Override
			public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				ArrayList<String> al = new ArrayList<>();
				String drive = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				for (int i = 0; i < drive.length(); i++) {
					al.add(drive.charAt(i) + ":");
				}
				if (node.getUserObject().equals("/") || al.contains(node.getUserObject())) {
					//获取maven src/test/resources下的文件
					this.setIcon(new ImageIcon(getClass().getResource("/drive.png")));
				} else {
					this.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
				}
				return this;
			}
		});
		
		//展开index数据,展开的时候必须使用invokelater，否则会出现jvm崩溃
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//添加index数据
				tree.root("/");
				//树枝
				tree.branch(new Object[] {"hello","2","3"});
				//叶子
				tree.leaf(new Object[] {"few","123","fweoi"});
				
				tree.select("123");
				DefaultMutableTreeNode node = tree.select();
				tree.leaf(node,new Object[] {"1432","1wef23","faewf"});
				
				tree.select("fweoi");
				node = tree.select();
				tree.leaf(node,new Object[] {"1432","1wef23","faewf"});
				
				tree.select("1wef23");
				tree.flush();
			}
		});
		add(tree);
	}
}
