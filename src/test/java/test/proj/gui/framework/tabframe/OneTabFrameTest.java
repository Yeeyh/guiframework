package test.proj.gui.framework.tabframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.TabFrame;
import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpPopupMenu;

@Component
@Table
public class OneTabFrameTest extends BpPanel{
	private static final long serialVersionUID = 1L;
	public OneTabFrameTest() {
//		Map<String, BpPanel> panels = AppContext.getPanels();
//		System.out.println(panels.size());
	}
	@Override
	public void init() {
		BpPopupMenu menu = BpPopupMenu.build(this)
				.addItems("select ws12","select ws1");
		menu.getItem("select ws12").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TabFrame frame = AppContext.getTabFrame();
				BpPanel p = AppContext.getPanel("ws12");
				frame.select(p);
			}
		});
		menu.getItem("select ws1").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TabFrame frame = AppContext.getTabFrame();
				BpPanel p = AppContext.getPanel("ws1");
				frame.select(p);
			}
		});
	}
}
