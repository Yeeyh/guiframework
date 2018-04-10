package test.proj.gui.framework.toolbar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.component.BpToolBar;
import com.proj.gui.framework.component.BpToolBar.StopClickListener;
@Table
public class ToolBarTest extends BpPanel {
	private static final long serialVersionUID = 1L;
	public void init() {
		BpToolBar tb = new BpToolBar();
		tb.setMaximum(15);
		TbTest tbTest = new TbTest(tb);
		final Thread t = new Thread(tbTest);
		t.start();
		JButton b = new JButton("start");
		StopClickListener stopClickListener = new StopClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				tb.stop();
				t.stop();
			}
		};
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread t = new Thread(tbTest);
				t.start();
			}
		});
		tb.click(stopClickListener);
		
		
		layout.grid().line(0).grid().column(0).fill().both().add(b, this);
		layout.grid().line(1).grid().column(0).fill().both().add(tb, this);
	}
}
class TbTest implements Runnable{
	BpToolBar tb;
	public TbTest(BpToolBar tb) {
		this.tb = tb;
	}
	@Override
	public void run() {
		tb.start();
		for (int i = 0; i <= 15; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tb.setText(i+"");
		}
		tb.success("success");
	}
	
}
