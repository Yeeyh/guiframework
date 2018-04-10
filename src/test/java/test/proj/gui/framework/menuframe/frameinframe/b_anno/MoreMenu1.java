package test.proj.gui.framework.menuframe.frameinframe.b_anno;

import javax.swing.JCheckBox;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Menu;
import com.proj.gui.framework.component.BpMenu;

@Component
@Menu
public class MoreMenu1 extends BpMenu {
	private static final long serialVersionUID = 1L;
	public BpMenu init() {
		for (int i = 0; i < 5; i++) {
			JCheckBox cmi = new JCheckBox(i+"");
			add(cmi);
		}
		return this;
	}
}
