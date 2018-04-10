package test.proj.gui.framework.mvc.a_single;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.TabFrame;
import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;

@Component("aview")
@Table
public class View extends BpPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		TabFrame f = AppContext.getTabFrame();
		for (int i = 0; i < 10; i++) {
			Controller_ bc = (Controller_) AppContext.getBean("controller_");
			f.addTab(bc);
		}
	}
}
