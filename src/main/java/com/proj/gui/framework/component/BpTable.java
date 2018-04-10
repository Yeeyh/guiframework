package com.proj.gui.framework.component;

import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Caret;

public class BpTable extends JTable{
	private static final long serialVersionUID = 1L;
	private BpTableModel model;
	private TableRowSorter<BpTableModel> sorter;
	public BpTable() {
		setAutoCreateRowSorter(false);
		//设置默认居中
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		setDefaultRenderer(Object.class, renderer);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3) {
					int row = rowAtPoint(e.getPoint());
					int col = columnAtPoint(e.getPoint());
					setRowSelectionInterval(row, row);
					setColumnSelectionInterval(col, col);
				}
			}
		});
	}
	public BpTable(String ... titles) {
		this();
		title(titles);
	}
	public BpTable title(String ... titles) {
		model = new BpTableModel(titles);
		setModel(model);
		sorter();
		return this;
	}
	public BpTable title(Vector<String> titles) {
		model = new BpTableModel(titles);
		setModel(model);
		sorter();
		return this;
	}
	private void sorter() {
		//java.lang.IndexOutOfBoundsException: Invalid index  
		//DefaultRowSorter的convertRowIndexToModel方法 新的数据必须小于当前数据，
		//否则会抛异常 一般都是0,如果处理了新数据等于当前数据的情况，那么进行排除过滤时将不能正确过滤
		//无论如何都调用父类的convertRowIndexToModel，才能正确的过滤
		//java.lang.ArrayIndexOutOfBoundsException: 0 >= 0
		//虽然报错地方是vector，但也是sorter引起的
		/*
		 * 解决方案：
		 * 1. 覆盖model 的 getValueAt 方法
		 * 2. 覆盖Jtable 的 convertRowIndexToModel 方法
		 */
		sorter = new TableRowSorter<BpTableModel>(model);
		setRowSorter(sorter);
	}
	
	public BpTable row(Vector<Vector<Object>> datas) {
		for(Vector<Object> data : datas) {
			model.insertRow(model.getRowCount(), data);
		}
		return this;
	}
	public BpTable row(Object[] rows) {
		//处理null值
		ArrayList<Object> list = new ArrayList<>();
		for (int i = 0; i < rows.length; i++) {
			list.add(rows[i]==null?"":rows[i]);
		}
		model.insertRow(model.getRowCount(), list.toArray());
		return this;
	}
	public BpTable row(String columnName, Object row) {
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < getColumnCount(); i++) {
			String cName = getColumnName(i);
			if(cName.equals(columnName)) {
				newRow.add(row==null?"":row);
			}else {
				newRow.add("");
			}
		}
		model.insertRow(model.getRowCount(), newRow);
		return this;
	}
	public List<Object> rows(String columnName){
		List<Object> list = new ArrayList<>();
		for(int i=0; i<getRowCount(); i++) {
			list.add(select(i,columnName));
		}
		return list;
	}
	public BpTable setRow(Object value, int row, String column) {
		model.setRow(value,row,column);
		return this;
	}
	public BpTable setRow(Object value, String column) {
		model.setRow(value,getSelectedRow(),column);
		return this;
	}
	public BpTable setRow(Object[] values) {
		if(getSelectedRow()==-1) return this;
		int r = getSelectedRow();
		for(int i=0; i<model.getColumnCount(); i++) {
			String t = model.getColumnName(i);
			if(i>=values.length) {
				break;
			}
			Object v = values[i];
			setRow(v, r, t);
		}
		return this;
	}
	public BpTable setRow(Vector<Vector<Object>> rows) {
		for(Vector<Object> v : rows) {
			setRow(v.toArray());
		}
		return this;
	}
	public BpTable select(Object value, String column) {
		if(getSelectedRow()==-1) return this;
		model.setRow(value,getSelectedRow(),column);
		return this;
	}
	public Object select(int row, String column) {
		return select(row, model.getColumnIndex(column));
	}
	public Object select(int row, int column) {
		return model.getValueAt(row, column);
	}
	public Object select(String column) {
		if(getSelectedRow()==-1) return "";
		return select(getSelectedRow(),column);
	}
	public Object select() {
		if(getSelectedRow()==-1) return "";
		if(getSelectedColumn()==-1) return "";
		return select(getSelectedRow(),getSelectedColumn());
	}
	public String columnName() {
		if(getSelectedColumn()==-1) return "";
		return model.getColumnName(getSelectedColumn());
	}
	public String columnName(int column) {
		return model.getColumnName(column);
	}
	public BpTable autoWidth() {
		//head
		JTableHeader header = getTableHeader();
		TableColumnModel hmodel = header.getColumnModel();
		for (int k = 0; k < hmodel.getColumnCount(); k++) {
			TableColumn hcolumn = hmodel.getColumn(k);
			Object hvalue = hcolumn.getHeaderValue();
			TableCellRenderer hrend = header.getDefaultRenderer();
			Component hcomp = hrend.getTableCellRendererComponent(this, hvalue, false, false, 0, 0);
			int hwidth = (int) hcomp.getPreferredSize().getWidth();
			hcolumn.setPreferredWidth(hwidth);
		}
		//content
		int rowcount = getRowCount();
		int colcount = getColumnCount();
		DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
		for (int i = 0; i < colcount; i++) {
			int maxwidth = 0;
			for (int j = 0; j < rowcount; j++) {
				Object value = getValueAt(j, i);
				Component comp = rend.getTableCellRendererComponent(this, value, false, false, 0, 0);
				int width = (int) comp.getPreferredSize().getWidth();
				TableColumnModel cmodel = getColumnModel();
				TableColumn column = cmodel.getColumn(i);
				maxwidth = Math.max(maxwidth, width);
				if (j == rowcount - 1) {
					Object hvalue = column.getHeaderValue();
					TableCellRenderer hrend = getTableHeader().getDefaultRenderer();
					Component hcomp = hrend.getTableCellRendererComponent(this, hvalue, false, false, 0, 0);
					int hwidth = (int) hcomp.getPreferredSize().getWidth();
					maxwidth = Math.max(maxwidth, hwidth);
				}
				column.setPreferredWidth(maxwidth + 1);
			}
		}
		return this;
	}
	public BpTable width(String columnName, int width) {
		return width(model.getColumnIndex(columnName),width);
	}
	public BpTable width(int columnindex, int width) {
		TableColumn tc = getColumnModel().getColumn(columnindex);
		tc.setMinWidth(width);
		tc.setMaxWidth(width);
		return this;
	}
	public BpTable minWidth(String columnName, int width) {
		return minWidth(model.getColumnIndex(columnName),width);
	}
	public BpTable minWidth(int columnIndex, int width) {
		TableColumn tc = getColumnModel().getColumn(columnIndex);
		tc.setMinWidth(width);
		return this;
	}
	public BpTable maxWidth(String columnName, int width) {
		return maxWidth(model.getColumnIndex(columnName),width);
	}
	public BpTable maxWidth(int columnIndex, int width) {
		TableColumn tc = getColumnModel().getColumn(columnIndex);
		tc.setMaxWidth(width);
		return this;
	}
	
	public BpTable hide(String ... columnName) {
		List<String> names = Arrays.asList(columnName);
		for (int i = 0; i < getColumnCount(); i++) {
			String name = model.getColumnName(i);
			if(!names.contains(name)) {
				continue;
			}
			width(i, 0);
		}
		return this;
	}
	public BpTable hide(int ... columnIndex) {
		for (int i : columnIndex) {
			width(i, 0);
		}
		return this;
	}
	public BpTable hideAll() {
		for (int i = 0; i < getColumnCount(); i++) {
			width(i, 0);
		}
		return this;
	}
	public void copyLines() {
		StringBuilder line = new StringBuilder();
		for(int row : getSelectedRows()) {
			for(int i=0; i<getColumnCount(); i++) {
				Object o = select(row,i);
				line.append(o).append(",");
			}
			line.deleteCharAt(line.length()-1);
			line.append("\r\n");
		}
		if(line.length()>0) {
			copy(line);
		}
	}
	public void copy() {
		copy(select());
	}
	public void copy(String column) {
		copy(select(column));
	}
	public void copy(Object o) {
		if(o==null) return;
		String data = o.toString();
		// 获取系统剪贴板。
		Clipboard clipboard = getToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(data);
		clipboard.setContents(tText, null);
	}
	public void setEditor(boolean isEdit) {
		model.setEdit(isEdit);
	}
	public JTextField editor(String columnName) {
		return editor(getSelectedRow(),columnName);
	}
	public JTextField editor(int row, String columnName) {
		model.setEdit(true);
		editCellAt(row,model.getColumnIndex(columnName));
		JTextField edit = (JTextField)getEditorComponent();
		edit.requestFocusInWindow();
		edit.selectAll();
		Caret caret = edit.getCaret();
		caret.setVisible(true);
		caret.setSelectionVisible(true);
		model.setEdit(false);
		return edit;
	}
	public void filter(String text) {
		sorter.setRowFilter(text.length() == 0?null:RowFilter.regexFilter(text));
	}
	public void excludeFilter(String text) {
		if(text.length() == 0) {
			sorter.setRowFilter(null);
			return;
		}
		RowFilter<BpTableModel, Integer> notFilter = RowFilter.notFilter(new RowFilter<BpTableModel, Integer>(){
			public boolean include(Entry<? extends BpTableModel, ? extends Integer> entry) {
				for(String t : text.split(",")) {
					for (int i = 0; i < entry.getValueCount(); i++) {
						String colValue = entry.getStringValue(i).trim();
						if(colValue.equals(t)) {
							return true;
						}
					}
				}
				return false;
			}
		});
		sorter.setRowFilter(notFilter);
	}
	@Override
	public void removeAll() {
		super.removeAll();
		for (int i = getRowCount() - 1; i >= 0; i--) {
			removeAt(i);
		}
	}
	public BpTable remove() {
		int[] rows = getSelectedRows();
		for (int i = rows.length-1; i >= 0 ; i--) {
			removeAt(rows[i]);
		}
		return this;
	}
	public BpTable removeAt(int index) {
		model.removeRow(index);
		return this;
	}
	
//	@Override //不能覆盖该方法，在table单元格可编辑时，会调用该方法进行更新
//	public void remove(int index) {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				model.removeRow(index);
//			}});
//	}
	
	public void delete(String columnName, String data) {
		for(int i=0; i<getRowCount(); i++) {
			if(select(i,columnName).equals(data)) {
				remove(i);
			}
		}
	}
	@Override
	public int convertRowIndexToModel(int viewRowIndex) {
		try {
			//这里导致第0行不能过滤，即使报错也需要进行更新
//			if(viewRowIndex==0) return viewRowIndex;
			return super.convertRowIndexToModel(viewRowIndex);
		} catch (IndexOutOfBoundsException e) {
			return viewRowIndex;
		}
	}
	
	@SuppressWarnings("unchecked")
 	public class BpTableModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;
		private boolean isEdit = false;

		public BpTableModel(String[] titles) {
			columnIdentifiers = new Vector<String>();
			for(String t : titles) {
				columnIdentifiers.add(t);
			}
		}
		public BpTableModel(Vector<String> titles) {
			columnIdentifiers = titles;
		}
		public void setEdit(boolean isEdit) {
			this.isEdit = isEdit;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return this.isEdit;
		}
		public Class<?> getColumnClass(int columnIndex) {
			return this.getRowCount() == 0?super.getColumnClass(columnIndex):getValueAt(0, columnIndex).getClass();
		}
		@Override
		public Object getValueAt(int row, int column) {
			if(getRowCount()==0) return null;
			return super.getValueAt(row, column);
	  }
		public int getColumnIndex(String column) {
			int index = -1;
			for (int i = 0; i < getColumnCount(); i++) {
				String name = columnIdentifiers.get(i).toString();
				if(!name.equals(column)) { continue; }
				index = i;
			}
			return index;
		}
		public BpTableModel setRow(Object value, int row, String column) {
			((Vector<Object>)dataVector.get(row)).set(getColumnIndex(column), value);
			fireTableDataChanged();
			return this;
		}
	}
}
