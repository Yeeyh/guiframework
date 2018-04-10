package test.proj.gui.framework.mvc.b_multiple;

import org.springframework.context.annotation.Scope;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;

@Scope("prototype")
@Table(B_View.class)
public class B_View_ extends BpPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		System.out.println("b view init"+this.hashCode());
	}
}
