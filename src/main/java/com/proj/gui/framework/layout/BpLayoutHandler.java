package com.proj.gui.framework.layout;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
/*
 * http://www.cnblogs.com/LiuYanYGZ/p/6156061.html
gridx　　指定组件左上角所在位置横坐标。
gridy　　指定组件左上角所在位置纵坐标。
gridwidth　　指定组件显示区域的某一行中的单元格数。 默认值1，水平占一格
gridheight　　指定在组件显示区域的一列中的单元格数。默认值1，垂直占一格
weightx　　指定如何分布额外的水平空间。 默认值0，额外水平空间不分配。 0: 当窗口放大时，长度不变
weighty　　指定如何分布额外的垂直空间。 默认值0，额外垂直空间不分配。	0: 当窗口放大时，高度不变
ipadx　　指定组件最小宽度，可确保组件不会过份缩放。	组件内部填充空间，即给组件的最小宽度添加多大的空间
ipady　　指定组件最小高度，可确保组件不会过份缩放。   组件内部填充空间，即给组件的最小高度添加多大的空间
inserts　　在组件周围增加空白区域。		组件彼此的间距
anchor　　单个组件水平、垂直都不填充时，设置组件应该显示在区域的哪个方位，西北还是东南。
anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部


gridx gridy: 单元格位置，如果跨越多个格则为左上角位置
gridwidth gridheight: 组件水平、垂直方向跨越的格数
weightx weighty: 组件相对于同一行、列内其他组件的大小（相对权重）
fill: 水平或者垂直方向拉伸，可选属性如下（实质int）
    GridBagConstraints内的常成员：NONE HORIZONTAL VERITAL BOTH
anchor: 对齐方式，可选属性如下（实质int）
    GridBagConstraints内的常成员：WEST EAST 分别为向左、右单元格对齐
*/
public class BpLayoutHandler implements BpLayout {
	private GridBagConstraints constraints;
	private GridHandler grid;
	private AlignHandler aligner;
	private FillHandler fill;
	private MarginHandler margin;
	private PaddingHandler padding;
	public BpLayoutHandler() {
		if (this.constraints == null) {
			this.constraints = new GridBagConstraints();
		}
		grid = new GridHandler();
		aligner = new AlignHandler();
		fill = new FillHandler();
		margin = new MarginHandler();
		padding = new PaddingHandler();
	}
	public GridBagConstraints getGridBagConstraints() {
		return this.constraints;
	}

	public void setGridBagConstraints(GridBagConstraints c) {
		if (c == null) {
			throw new NullPointerException("GridBagConstraints cannot be null.");
		}
		this.constraints = c;
	}

	@Override
	public void add(JComponent element, Container panel) {
		if (!panel.getLayout().getClass().equals(GridBagLayout.class)) {
			throw new IllegalArgumentException(
					"The panel component must use GridBagLayout as layout manager. Use panel.setLayout(new GridBagLayout()) to set.");
		}
		if (element == null) {
			throw new NullPointerException("The element component could not be null.");
		}
		if (element.equals(panel)) {
			throw new NullPointerException("The element could not be inserted at itself.");
		}
		panel.add(element, this.constraints);
		this.constraints = new GridBagConstraints();
	}

	@Override
	public Align align() {
		return aligner.initHandler(this);
	}

	@Override
	public Margin margin() {
		return margin.initHandler(this);
	}

	@Override
	public Grid grid() {
		return grid.initHandler(this);
	}

	@Override
	public Fill fill() {
		return fill.initHandler(this);
	}
	
	@Override
	public Padding padding() {
		return padding.initHandler(this);
	}
	public class AlignHandler implements Align {
		private static final int LEFT = GridBagConstraints.WEST;
		private static final int RIGHT = GridBagConstraints.EAST;
		private static final int CENTER = GridBagConstraints.CENTER;
		private static final int NORTH = GridBagConstraints.NORTH;
		private static final int NORTH_RIGHT = GridBagConstraints.NORTHEAST;
		private static final int NORTH_LEFT = GridBagConstraints.NORTHWEST;
		private static final int SOUTH = GridBagConstraints.SOUTH;
		private static final int SOUTH_RIGHT = GridBagConstraints.SOUTHEAST;
		private static final int SOUTH_LEFT = GridBagConstraints.SOUTHWEST;
		private BpLayoutHandler handle;
		private GridBagConstraints constraints;

		public AlignHandler initHandler(BpLayout layout) {
			this.handle = (BpLayoutHandler) layout;
			this.constraints = this.handle.getGridBagConstraints();
			return this;
		}

		@Override
		public BpLayout top() {
			this.constraints.anchor = NORTH;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout topRight() {
			this.constraints.anchor = NORTH_RIGHT;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout topLeft() {
			this.constraints.anchor = NORTH_LEFT;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout bottom() {
			this.constraints.anchor = SOUTH;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout bottomRight() {
			this.constraints.anchor = SOUTH_RIGHT;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout bottomLeft() {
			this.constraints.anchor = SOUTH_LEFT;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout centered() {
			this.constraints.anchor = CENTER;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout left() {
			this.constraints.anchor = LEFT;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout right() {
			this.constraints.anchor = RIGHT;
			this.handle.setGridBagConstraints(constraints);
			return this.handle;
		}

		@Override
		public BpLayout label() {
			return this.right();
		}
	}

	public class FillHandler implements Fill {
		private BpLayoutHandler layout;
		private GridBagConstraints constraints;

		public FillHandler initHandler(BpLayout layout) {
			this.layout = (BpLayoutHandler) layout;
			this.constraints = this.layout.getGridBagConstraints();
			return this;
		}

		@Override
		public BpLayout horizontal() {
			this.constraints.fill = GridBagConstraints.HORIZONTAL;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout vertical() {
			this.constraints.fill = GridBagConstraints.VERTICAL;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout both() {
			this.constraints.fill = GridBagConstraints.BOTH;
			// this.constraints.gridwidth = GridBagConstraints.RELATIVE;
			 this.constraints.weightx = 1.0;
			 this.constraints.weighty = 1.0;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}
	}

	public class GridHandler implements Grid {
		private BpLayoutHandler layout;
		private GridBagConstraints constraints;

		public GridHandler initHandler(BpLayout layout) {
			this.layout = (BpLayoutHandler) layout;
			this.constraints = this.layout.getGridBagConstraints();
			return this;
		}

		@Override
		public BpLayout column(int column) {
			this.constraints.gridx = column;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout line(int line) {
			this.constraints.gridy = line;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout colspan(int cols) {
			this.constraints.gridwidth = cols;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout rowspan(int rows) {
			this.constraints.gridheight = rows;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout width(double with) {
			this.constraints.weightx = with;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout height(double height) {
			this.constraints.weighty = height;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}
	}

	public class MarginHandler implements Margin {
		private BpLayoutHandler layout;
		private GridBagConstraints constraints;

		public MarginHandler initHandler(BpLayout layout) {
			this.layout = (BpLayoutHandler) layout;
			this.constraints = this.layout.getGridBagConstraints();
			return this;
		}

		@Override
		public BpLayout margin(int top, int left, int bottom, int right) {
			this.constraints.insets = new Insets(top, left, bottom, right);
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout top(int top) {
			return this.margin(top, 0, 0, 0);
		}

		@Override
		public BpLayout bottom(int bottom) {
			return this.margin(0, 0, bottom, 0);
		}

		@Override
		public BpLayout left(int left) {
			return this.margin(0, left, 0, 0);
		}

		@Override
		public BpLayout right(int right) {
			return this.margin(0, 0, 0, right);
		}

		@Override
		public BpLayout same(int margin) {
			return this.margin(margin, margin, margin, margin);
		}
	}
	public class PaddingHandler implements Padding {
		private BpLayoutHandler layout;
		private GridBagConstraints constraints;

		public PaddingHandler initHandler(BpLayout layout) {
			this.layout = (BpLayoutHandler) layout;
			this.constraints = this.layout.getGridBagConstraints();
			return this;
		}
		
		@Override
		public BpLayout same(int xy) {
			this.constraints.ipadx = xy;
			this.constraints.ipady = xy;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout width(int ipadx) {
			this.constraints.ipadx = ipadx;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}

		@Override
		public BpLayout height(int ipady) {
			this.constraints.ipady = ipady;
			this.layout.setGridBagConstraints(constraints);
			return this.layout;
		}
	}
}
