package com.proj.gui.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

import org.springframework.stereotype.Component;

import com.proj.gui.framework.annotation.Table;
import com.proj.gui.framework.annotation.View;
import com.proj.gui.framework.component.BpController;
import com.proj.gui.framework.component.BpPanel;
import com.proj.gui.framework.menu.ViewMenu;
import com.proj.gui.framework.panel.exception.AlertsException;
@Component
public class TabFrame extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	private boolean dragging = false;
  private Image tabImage = null;
  private Point currentMouseLocation = null;
  private int draggedTabIndex = 0;
  public TabFrame() {
		UIManager.put("nimbusBase", new ColorUIResource(0, 0, 0));//背景
		UIManager.put("textForeground", Color.WHITE);
		listener();
  }
  public TabFrame init() {
	  Map<String, BpPanel> panels = AppContext.getPanels();
		List<BpPanel> list = new ArrayList<>();
		// module & plugins
		for (BpPanel p : panels.values()) {
			try {
				p.init0();
				list.add(p);
			} catch (Exception e) {
				p.setEnabled(false);
				AlertsException.error(p.getName(),e);
			}
		}
		Map<String, BpController> controllers = AppContext.getControllers();
		for(BpController c : controllers.values()) {
			try {
				c.init();
			} catch (Exception e) {
				AlertsException.error(c.getClass().getSimpleName(),e);
			}
		}
		for(BpPanel p : list) {
			addTab0(p, false, false);
		}
		setTab(panels.get("alerts"));
		setTab(panels.get("options"));
		return this;
  }
  public void addTab(BpPanel p, BpController c) {
	  addTab(p,c,false);
  }
  public void addTab(BpPanel p, BpController c, boolean select) {
	  addTab(p,c,false,false);
  }
  public void addTab(BpPanel p, BpController c, boolean select, boolean close) {
	  	p.init0();
	  	c.init();
	  	Field[] fields = c.getClass().getDeclaredFields();
	  	for(Field f : fields) {
	  		try {
	  			f.setAccessible(true);
	  			if(f.getType().equals(p.getClass())) {
	  					f.set(c, p);
	  			}
				} catch (Exception e) {
					AlertsException.error(p.getName(),e);
				}
	  	}
	  	addTab0(p, select, close);
  }
  public void addTab(BpController c) {
	  addTab(c,false);
  }
  public void addTab(BpController c, boolean select) {
	  addTab(c,false,false);
  }
  public void addTab(BpController c, boolean select, boolean close) {
	  List<BpPanel> list = new ArrayList<BpPanel>();
	  	Field[] fields = c.getClass().getDeclaredFields();
	  	for(Field f : fields) {
	  		try {
	  			f.setAccessible(true);
	  			Object o = f.get(c);
	  			if((o instanceof BpPanel)) {
	  					BpPanel p = (BpPanel)o;
	  					if(!p.init) {
	  						p.init0();
	  					}
	  					View an = f.getAnnotation(View.class);
	  					if(an!=null) {
	  							list.add(p);
	  					}
	  			}
				} catch (Exception e) {
					AlertsException.error(c.getClass().getSimpleName(),e);
				}
	  	}
	  	c.init();
	  	for(BpPanel p : list) {
	  			addTab0(p, select, close);
	  	}
  }
  public void addTab(BpPanel p) {
	  addTab(p,false);
  }
  public void addTab(BpPanel p, boolean select) {
	  addTab(p,false,false);
  }
	public void addTab(BpPanel p, boolean select, boolean close) {
		p.init0();//当组建多的时候会占用内存
		addTab0(p, select, close);
	}
	private void addTab0(BpPanel p, boolean select, boolean close) {
		if (!p.isEnabled())
			return;
		Table an = p.getClass().getAnnotation(Table.class);
		if(an==null) return;
		Class<?> clazz = an.value();
		if(!BpPanel.class.equals(clazz.getSuperclass())) {
			setTab(p,select,close);
		}else {
			@SuppressWarnings("unchecked")
			BpPanel bp = AppContext.getPanel((Class<BpPanel>)clazz);
			TabFrame tf = null;
			for(java.awt.Component c : bp.getComponents()){
				if(!(c instanceof TabFrame)) {
					continue;
				}
				tf = (TabFrame)c;
			}
			if(tf==null) {
				tf = new TabFrame();
			}
			tf.setTab(p,select,close);
			bp.add(tf);
		}
	}
	public void setTab(BpPanel p) {
		setTab(p, false);
	}
	public void setTab(BpPanel p, boolean select) {
		setTab(p, select, false);
	}
	public void setTab(BpPanel p, boolean select,boolean close) {
		setTab(p, 1, select, close);
	}
	public void setTab(BpPanel p, int index, boolean select,boolean close) {
		p.close = close;
		addTab(p.getName(), p);
		insertTab(p.getName(), null, p, null, getTabCount()-index<0?0:getTabCount()-index);
		setTabComponentAt(getTabCount()-index<0?0:getTabCount()-index, getCloseLab(p,close));
		if(select) select(p);
	}
	public void select(BpPanel p) {
		setSelectedIndex(indexOfComponent(p));
	}
	private JPanel getCloseLab(BpPanel p, boolean close) {
		// 设置title栏左侧图标，中间插件名称，右侧关闭图标
		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false); // 默认false，组件透明
		JLabel n = new JLabel(p.getName());
		titlePanel.add(n, BorderLayout.CENTER);
		if(close) {
			JLabel closeLabel = new JLabel("x");
			closeLabel.addMouseListener(new CloseLabAction(p, closeLabel));
			titlePanel.add(closeLabel, BorderLayout.EAST);
		}
		return titlePanel;
	}
	
	private void listener() {
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (!dragging) {
					// Gets the tab index based on the mouse position
					int tabNumber = getUI().tabForCoordinate(TabFrame.this, e.getX(), e.getY());

					if (tabNumber >= 0) {
						draggedTabIndex = tabNumber;
						Rectangle bounds = getUI().getTabBounds(TabFrame.this, tabNumber);

						// Paint the tabbed pane to a buffer
						Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics totalGraphics = totalImage.getGraphics();
						totalGraphics.setClip(bounds);
						// Don't be double buffered when painting to a static image.
						setDoubleBuffered(false);
						paintComponent(totalGraphics);

						// Paint just the dragged tab to the buffer
						tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
						Graphics graphics = tabImage.getGraphics();
						graphics.drawImage(totalImage, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y,
								bounds.x + bounds.width, bounds.y + bounds.height, TabFrame.this);

						dragging = true;
						repaint();
					}
				} else {
					currentMouseLocation = e.getPoint();
					// Need to repaint
					repaint();
				}
				super.mouseDragged(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (dragging) {
					int tabNumber = getUI().tabForCoordinate(TabFrame.this, e.getX(), 10);
					if (tabNumber >= 0) {
						BpPanel comp = (BpPanel) getComponentAt(draggedTabIndex);
						String title = getTitleAt(draggedTabIndex);
						removeTabAt(draggedTabIndex);
						insertTab(title, null, comp, null, tabNumber);
						setSelectedIndex(tabNumber);
						if(comp.close) {
						 setTabComponentAt(tabNumber, getCloseLab(comp,true));
						}
					}
				}

				dragging = false;
				tabImage = null;
			}
		});
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(getSelectedIndex()==-1)
					return;
				setForegroundAt(getSelectedIndex(), Color.BLACK);
			}
		});
	}
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    // Are we dragging?
	    if(dragging && currentMouseLocation != null && tabImage != null) {
	      // Draw the dragged tab
	      g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
	    }
	  }
	private class CloseLabAction extends MouseAdapter {
		private BpPanel p;
		private JLabel closeLabel;

		public CloseLabAction(BpPanel p, JLabel closeLabel) {
			this.p = p;
			this.closeLabel = closeLabel;
		}
		public void mouseEntered(MouseEvent e) {
			closeLabel.setForeground(Color.WHITE);
		}
		public void mouseExited(MouseEvent e) {
			closeLabel.setForeground(Color.BLACK);
		}
		public void mouseClicked(MouseEvent e) {
			remove(p);
			ViewMenu menu = (ViewMenu) AppContext.getMenu("view");
			JCheckBox cbox = menu.getItem(p.getName());
			if(cbox!=null) cbox.setSelected(false);
		}
	}
}
