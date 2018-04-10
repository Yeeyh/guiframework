package test.proj.gui.framework.mvc.a_single;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;

@Table(View.class)
public class View_ extends BpPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		System.out.println("a view init"+this.hashCode());
	}
}
