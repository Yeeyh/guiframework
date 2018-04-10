package test.proj.gui.framework.mvc.a_single;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.proj.gui.framework.component.BpController;

@Controller
public class Controller_ implements BpController{

	@Autowired
	View_ v;
	@Override
	public void init() {
		System.out.println("a controller init"+v.hashCode());
	}

}
