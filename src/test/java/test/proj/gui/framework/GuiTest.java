package test.proj.gui.framework;

import com.proj.gui.framework.AppContext;

public class GuiTest {
	public static void main(String[] args) {
		AppContext.CONTEXT = "beans.xml";
		AppContext.start();
	}
}
