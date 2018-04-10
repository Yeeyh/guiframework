package test.proj.gui.framework.mvc.b_multiple;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.proj.gui.framework.annotation.View;
import com.proj.gui.framework.component.BpController;

@Scope("prototype")
@Controller
public class B_Controller_ implements BpController{
	@View
	B_View_ v;
	@Override
	public void init() {
		System.out.println("b controller init"+this.hashCode());
	}

}
