package test.proj.gui.framework.grid.filemanger;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpPopupMenu;
import com.proj.gui.framework.component.BpTable;
import com.proj.gui.framework.component.BpTree;

@Component
@Table
public class Filemanager extends BpPanel {
	private static final long serialVersionUID = 1L;
	@Override
	public boolean isEnabled() {
		return false;
	}
	public void init() {
		//上
		JTextField	path = new JTextField();
		JButton		read = new JButton("读取");
				//左
		BpTree	tree = new BpTree();
		JScrollPane treepane = new JScrollPane(tree);
		treepane.setPreferredSize(new Dimension(25, 0));
		//右
		BpTable list = new BpTable();
		list.title("图标","文件","时间","大小","属性");
		JScrollPane listpane = new JScrollPane(list);
		
		//下-状态栏
		JToolBar bar = new JToolBar();
		JLabel status = new JLabel("完成");
		bar.add(status);
		bar.setFloatable(false);//不能移动
		
		layout.grid().line(0).grid().column(0).fill().horizontal()
		.grid().width(1).grid().colspan(2).grid().rowspan(1).add(path, this);
		layout.grid().line(0).grid().column(2).add(read, this);
		
		layout.grid().line(1).grid().column(0).fill().vertical()
		.grid().height(1).padding().width(200).add(treepane, this);
		
		layout.grid().line(1).grid().column(1).fill().both()
		.grid().colspan(2).grid().rowspan(1).grid().width(1).grid().height(1)
		.margin().left(5).add(listpane, this);
		
		layout.grid().line(2).grid().column(0).fill().horizontal()
		.grid().colspan(3).grid().rowspan(1).grid().width(100)
		.add(bar, this);
		
		//右键
		BpPopupMenu.build(tree)
						.addItems("刷新");
		BpPopupMenu.build(listpane)
						.addItems("刷新","separator")
						.addItems("上传","separator")
						.addMenus("新建")
						.addItems("新建",new String[] {"文件","文件夹"});
		BpPopupMenu.build(list)
						.addItems("刷新","separator")
						.addItems("上传","下载","separator","打开","删除","重命名","修改时间")
						.addMenus("新建")
						.addItems("新建",new String[] {"文件","文件夹"});
	}
}
