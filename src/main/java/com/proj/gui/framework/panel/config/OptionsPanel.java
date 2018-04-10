package com.proj.gui.framework.panel.config;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.AppContext;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.panel.config.font.FontPanel;
import com.proj.gui.framework.panel.config.proxy.ProxyPanel;
import com.proj.gui.framework.panel.config.style.StylePanel;

@Component("options")
public class OptionsPanel extends BpPanel {
	private static final long serialVersionUID = 1L;

	public void init() {
		layout.grid().line(0)
		.grid().column(0)
		.fill().horizontal()
		.grid().height(0)
		.align().topLeft()
		.margin().same(1)
		.add(AppContext.getPanel(ProxyPanel.class), this);
		
		layout.grid().line(1)
		.grid().column(0)
		.fill().horizontal()
		.grid().width(.5)
		.grid().height(0)
		.align().topLeft()
		.margin().same(1)
		.add(AppContext.getPanel(FontPanel.class), this);
		
		layout.grid().line(2)
		.grid().column(0)
		.fill().horizontal()
		.grid().width(.5)
		.grid().height(.5)
		.align().topLeft()
		.margin().same(1)
		.add(AppContext.getPanel(StylePanel.class), this);
	}
}
