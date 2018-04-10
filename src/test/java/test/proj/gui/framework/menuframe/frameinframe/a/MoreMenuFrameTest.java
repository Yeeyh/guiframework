package test.proj.gui.framework.menuframe.frameinframe.a;

import javax.swing.JCheckBox;
import javax.swing.JMenuItem;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Menu;
import com.proj.gui.framework.component.BpMenu;

@Component
@Menu
public class MoreMenuFrameTest extends BpMenu {
	private static final long serialVersionUID = 1L;
	public BpMenu init() {
		for (int i = 0; i < 5; i++) {
			JCheckBox cmi = new JCheckBox(i+"");
			add(cmi);
		}
		BpMenu bm = new BpMenu("hello") {
			private static final long serialVersionUID = 1L;
			public BpMenu init() {
				for (int i = 0; i < 5; i++) {
					JCheckBox cmi = new JCheckBox(i+"");
					add(cmi);
				}
				return this;
		}};
		add(bm.init());
		for (int i = 0; i < 5; i++) {
			JMenuItem  item  = new  JMenuItem("hello123"+i);
			add(item);
		}
		return this;
	}
}
