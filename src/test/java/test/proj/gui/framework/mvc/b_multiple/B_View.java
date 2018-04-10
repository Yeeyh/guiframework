package test.proj.gui.framework.mvc.b_multiple;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.TabFrame;
import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;

@Table
@Component("bview")
public class B_View extends BpPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		TabFrame f = AppContext.getTabFrame();
		for (int i = 0; i < 10; i++) {
			B_Controller_ bc = (B_Controller_) AppContext.getBean("b_Controller_");
			f.addTab(bc);
		}
	}
}
