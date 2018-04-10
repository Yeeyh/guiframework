package com.proj.gui.framework.panel.config.font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import com.proj.gui.framework.layout.BpLayout;
import com.proj.gui.framework.layout.BpLayoutHandler;
@SuppressWarnings("serial")
public class FontChooser extends JDialog {
	public static final int CANCEL_OPTION = 0;
	public static final int APPROVE_OPTION = 1;
	private static final String VIEW_STRING = "<html><body>你好世界！<br/>Hello World！<br/>0123456789</body></html>";
	// 预设字体，也是将来要返回的字体
	private Font font = null;
	// 字体选择器组件容器
	private JPanel box = null;
	// 字体文本框
	private JTextField fontText = null;
	// 样式文本框
	private JTextField styleText = null;
	// 文字大小文本框
	private JTextField sizeText = null;
	// 预览文本框
	private JLabel previewText = null;
	// 字体选择框
	private JList<String> fontList = null;
	// 样式选择器
	private JList<String> styleList = null;
	// 文字大小选择器
	private JList<String> sizeList = null;
	// 确定按钮
	private JButton approveButton = null;
	// 取消按钮
	private JButton cancelButton = null;
	// 所有字体
	private String [] fontArray = null;
	// 所有样式
	private String [] styleArray = {"常规", "粗体", "斜体", "粗斜体"};
	// 所有预设字体大小
	private String [] sizeArray = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "22", "24", "26", "28", "36", "48"};
	// 上面数组中对应的字体大小
	private int [] sizeIntArray = {8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 24, 26, 28, 36, 48};
	// 返回的数值，默认取消
	private int returnValue = CANCEL_OPTION;
	public FontChooser() {
		this(new Font("宋体", Font.PLAIN, 13));
	}
	public FontChooser(Font font) {
		setTitle("字体选择");
		this.font = font;
		// 初始化UI组件
		init();
		// 添加监听器
		addListener();
		// 按照预设字体显示
		setup();
		// 基本设置
		setModal(true);
		setResizable(false);
		// 自适应大小
		pack();
	}
	/**
	 * 初始化组件
	 */
	private void init(){
		// 获得系统字体
		GraphicsEnvironment eq = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontArray = eq.getAvailableFontFamilyNames();
		BpLayout layout = new BpLayoutHandler();
		// 主容器
		box = new JPanel();
		box.setBackground(Color.WHITE);
		box.setLayout(new GridBagLayout());
		fontText = new JTextField();
		fontText.setEditable(false);
		fontText.setBackground(Color.WHITE);
		styleText = new JTextField();
		styleText.setEditable(false);
		styleText.setBackground(Color.WHITE);
		sizeText = new JTextField("13");
		// 给文字大小文本框使用的Document文档，制定了一些输入字符的规则
		Document doc = new PlainDocument(){
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null) {
					return;
				}
				if (getLength() >= 3) {
					return;
				}
				if (!str.matches("[0-9]+")) {
					return;
				}
				super.insertString(offs, str, a);
				sizeList.setSelectedValue(sizeText.getText(), true);
			}
		};
		sizeText.setDocument(doc);
		previewText = new JLabel();
		previewText.setHorizontalAlignment(JTextField.CENTER);
		previewText.setBackground(Color.WHITE);
		fontList = new JList<String>(fontArray);
		styleList = new JList<String>(styleArray);
		sizeList = new JList<String>(sizeArray);
		approveButton = new JButton("确定");
		cancelButton = new JButton("取消");
		JLabel l1 = new JLabel("字体:");
		JLabel l2 = new JLabel("字形:");
		JLabel l3 = new JLabel("大小:");
		layout.grid().line(0).grid().column(0).fill().both().add(l1, box);
		layout.grid().line(0).grid().column(1).fill().both().add(l2, box);
		layout.grid().line(0).grid().column(2).fill().both().add(l3, box);
		layout.grid().line(1).grid().column(0).fill().both().add(fontText, box);
		layout.grid().line(1).grid().column(1).fill().both().add(styleText, box);
		layout.grid().line(1).grid().column(2).fill().both().add(sizeText, box);
		JScrollPane sp1 = new JScrollPane(fontList);
		JScrollPane sp2 = new JScrollPane(styleList);
		JScrollPane sp3 = new JScrollPane(sizeList);
		sp2.setPreferredSize(new Dimension(90, 20));
		sp3.setPreferredSize(new Dimension(90, 20));
		layout.grid().line(2).grid().column(0).fill().both().add(sp1, box);
		layout.grid().line(2).grid().column(1).fill().both().add(sp2, box);
		layout.grid().line(2).grid().column(2).fill().both().add(sp3, box);
		JPanel previewPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createTitledBorder("预览"));
		previewPanel.add(previewText);
		previewPanel.setBackground(Color.WHITE);
		previewPanel.setPreferredSize(new Dimension(300, 120));
		layout.grid().line(3).grid().column(0).fill().both().grid().colspan(3).add(previewPanel, box);
		Box box7 = Box.createHorizontalBox();
		box7.add(Box.createHorizontalGlue());
		box7.add(approveButton);
		box7.add(Box.createHorizontalStrut(5));
		box7.add(cancelButton);
		layout.grid().line(4).grid().column(1).fill().both().align().bottomRight().add(approveButton, box);
		layout.grid().line(4).grid().column(2).fill().both().align().bottomRight().add(cancelButton, box);
		getContentPane().add(box);
	}
	/**
	 * 按照预设字体显示
	 */
	private void setup() {
		String fontName = font.getFamily();
		int fontStyle = font.getStyle();
		int fontSize = font.getSize();
		/*
		 * 如果预设的文字大小在选择列表中，则通过选择该列表中的某项进行设值，否则直接将预设文字大小写入文本框
		 */
		boolean b = false;
		for (int i = 0; i < sizeArray.length; i++) {
			if (sizeArray[i].equals(String.valueOf(fontSize))) {
				b = true;
				break;
			}
		}
		if(b){
			// 选择文字大小列表中的某项
			sizeList.setSelectedValue(String.valueOf(fontSize), true);
		}else{
			sizeText.setText(String.valueOf(fontSize));
		}
		// 选择字体列表中的某项
		fontList.setSelectedValue(fontName, true);
		// 选择样式列表中的某项
		styleList.setSelectedIndex(fontStyle);
		// 显示预览
		setPreview();
	}
	/**
	 * 添加所需的事件监听器
	 */
	private void addListener() {
		sizeText.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				setPreview();
			}
			public void focusGained(FocusEvent e) {
				sizeText.selectAll();
			}
		});
		// 字体列表发生选择事件的监听器
		fontList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					fontText.setText(String.valueOf(fontList.getSelectedValue()));
					// 设置预览
					setPreview();
				}
			}
		});
		styleList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					styleText.setText(String.valueOf(styleList.getSelectedValue()));
					// 设置预览
					setPreview();
				}
			}
		});
		sizeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if(!sizeText.isFocusOwner()){
						sizeText.setText(String.valueOf(sizeList.getSelectedValue()));
					}
					// 设置预览
					setPreview();
				}
			}
		});
		// 确定按钮的事件监听
		approveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 组合字体
				font = groupFont();
				// 设置返回值
				returnValue = APPROVE_OPTION;
				// 关闭窗口
				disposeDialog();
			}
		});
		// 取消按钮事件监听
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disposeDialog();
			}
		});
	}
	/**
	 * 显示字体选择器
	 * @param owner 上层所有者
	 * @return 该整形返回值表示用户点击了字体选择器的确定按钮或取消按钮，参考本类常量字段APPROVE_OPTION和CANCEL_OPTION
	 */
	public final int showFontDialog(JFrame owner) {
		setLocationRelativeTo(owner);
		setVisible(true);
		return returnValue;
	}
	/**
	 * 返回选择的字体对象
	 * @return 字体对象
	 */
	public final Font getSelectFont() {
		return font;
	}
	/**
	 * 关闭窗口
	 */
	private void disposeDialog() {
		FontChooser.this.removeAll();
		FontChooser.this.dispose();
	}
	
	/**
	 * 显示错误消息
	 * @param errorMessage 错误消息
	 */
	private void showErrorDialog(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "错误", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * 设置预览
	 */
	private void setPreview() {
		previewText.setText(VIEW_STRING);
		Font f = groupFont();
		previewText.setFont(f);
	}
	/**
	 * 按照选择组合字体
	 * @return 字体
	 */
	private Font groupFont() {
		String fontName = fontText.getText();
		int fontStyle = styleList.getSelectedIndex();
		String sizeStr = sizeText.getText().trim();
		// 如果没有输入
		if(sizeStr.length() == 0) {
			showErrorDialog("字体（大小）必须是有效“数值！");
			return null;
		}
		int fontSize = 0;
		// 通过循环对比文字大小输入是否在现有列表内
		for (int i = 0; i < sizeArray.length; i++) {
			if(sizeStr.equals(sizeArray[i])){
				fontSize = sizeIntArray[i];
				break;
			}
		}
		// 没有在列表内
		if (fontSize == 0) {
			try{
				fontSize = Integer.parseInt(sizeStr);
				if(fontSize < 1){
					showErrorDialog("字体（大小）必须是有效“数值”！");
					return null;
				}
			}catch (NumberFormatException nfe) {
				showErrorDialog("字体（大小）必须是有效“数值”！");
				return null;
			}
		}
		return new Font(fontName, fontStyle, fontSize);
	}
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			//设置皮肤
			String skin = "javax.swing.plaf.nimbus.NimbusLookAndFeel";//该主题影响右键弹出框
			UIManager.setLookAndFeel(skin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		JTextArea text = new JTextArea("五星红旗迎风飘扬，/n胜利歌声多么响亮；/n歌唱我们亲爱的祖国，/n从今走向繁荣富强。/n歌唱我们亲爱的祖国，/n从今走向繁荣富强。/n/n越过高山，/n越过平原，/n跨过奔腾的黄河长江；/n宽广美丽的土地，/n是我们亲爱的家乡，/n英雄的人民站起来了！/n我们团结友爱坚强如钢。");
		System.out.println(text.getFont());
		JButton button = new JButton("设置字体");
		// 构造字体选择器，参数字体为预设值
		FontChooser fontChooser = new FontChooser(text.getFont());
		// 打开一个字体选择器窗口，参数为父级所有者窗体。返回一个整型，代表设置字体时按下了确定或是取消，可参考MQFontChooser.APPROVE_OPTION和MQFontChooser.CANCEL_OPTION
		int returnValue = fontChooser.showFontDialog(frame);
		// 如果按下的是确定按钮
		if (returnValue == FontChooser.APPROVE_OPTION) {
			// 获取选择的字体
			Font font = fontChooser.getSelectFont();
			// 将字体设置到JTextArea中
			text.setFont(font);
		}
		frame.getContentPane().add(new JScrollPane(text));
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
