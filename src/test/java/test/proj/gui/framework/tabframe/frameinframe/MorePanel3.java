package test.proj.gui.framework.tabframe.frameinframe;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
@Component("ws1")
@Table(MorePanel2.class)
public class MorePanel3 extends BpPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
	}
}
